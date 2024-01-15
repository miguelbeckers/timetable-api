package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.*;
import ipb.pt.timetableapi.newData.model.*;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;
import java.util.List;

public class TimeTableConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                roomConflict(constraintFactory),
//                studentConflict(constraintFactory),
                professorConflict(constraintFactory),
                resourceAvailability(constraintFactory),
                classroomAvailability(constraintFactory),
                professorAvailability(constraintFactory),
                courseAvailability(constraintFactory),
                // verificar se nao há conflito entre as turmas de um mesmo ano e curso

                // verificar se não há conflito de aluno

                // Soft constraints
                professorTimeEfficiency(constraintFactory) // incluir flag
                // distribuir a partir do centro do dia - antes do meio dia ou depois das 14h
        };
    }

//    UC1 pode dividir em Ta Tb
//    UC2 pode dividir em turma Ta Tb Tc Td
//    nao pode haver conflitos entre UC1ta e UC2ta/tb, UC1tb e UC2tc/td
//    criar alunos ficticios ao criar as turmas para testar
//    turno -> lesson

    private Constraint roomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getClassroom),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Room conflict");
    }

    //TODO: checar antes de descomentar
//    private Constraint studentConflict(ConstraintFactory constraintFactory) {
//        return constraintFactory
//                .forEach(Lesson.class)
//                .join(Lesson.class,
//                        Joiners.equal(Lesson::getTimeslot),
//                        Joiners.lessThan(Lesson::getId))
//                .filter(this::haveStudentConflict)
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Student conflict");
//    }
//
//    private boolean haveStudentConflict(Lesson lesson1, Lesson lesson2) {
//        List<Student> students1 = getStudentsInLesson(lesson1);
//        List<Student> students2 = getStudentsInLesson(lesson2);
//
//        for (Student student1 : students1) {
//            if (students2.contains(student1)) {
//                return true; // Student is in two places at the same time
//            }
//        }
//
//        return false;
//    }
//
//    private List<Student> getStudentsInLesson(Lesson lesson) {
//        return lesson.getLessonStudents().stream()
//                .map(LessonStudent::getStudent)
//                .collect(Collectors.toList());
//    }

    private Constraint professorConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getProfessor),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
    }

    private Constraint resourceAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter(lesson -> lesson.getClassroom() != null)
                .filter(lesson -> !lesson.getLessonResources().isEmpty())
                .filter(this::checkResourceAvailability)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Resource availability conflict");
    }

    private boolean checkResourceAvailability(Lesson conflictingLesson) {
        List<Resource> resourcesOfTheLesson = conflictingLesson.getLessonResources().stream()
                .map(LessonResource::getResource)
                .toList();

        List<Resource> resourcesOfTheClassroom = conflictingLesson.getClassroom().getClassroomResources().stream()
                .map(ClassroomResource::getResource)
                .toList();

        // Checks that at least one lesson resource is not present in the classroom
        if (resourcesOfTheLesson.stream().anyMatch(resource -> !resourcesOfTheClassroom.contains(resource))) {
            return true;
        }

        // Checks if the number of available resources is less than the required number
        return conflictingLesson.getClassroom().getClassroomResources().stream()
                .filter(classroomResource -> resourcesOfTheLesson.contains(classroomResource.getResource()))
                .anyMatch(classroomResource -> classroomResource.getQuantity() < conflictingLesson.getLessonStudents().size());
    }

    private Constraint classroomAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter(lesson -> lesson.getClassroom() != null) // Only for lessons with an assigned classroom
                .ifExists(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.lessThan(Lesson::getId))
                .filter(this::hasClassroomUnavailabilityConflict)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Classroom unavailability conflict");
    }

    private boolean hasClassroomUnavailabilityConflict(Lesson conflictingLesson) {
        Classroom classroom = conflictingLesson.getClassroom();
        List<Timeslot> lessonUnavailability = classroom.getUnavailability();
        return hasUnavailabilityConflict(conflictingLesson, lessonUnavailability);
    }

    private boolean hasUnavailabilityConflict(Lesson conflictingLesson, List<Timeslot> unavailabilityList) {
        if (unavailabilityList != null && !unavailabilityList.isEmpty()) {
            Timeslot lessonTimeslot = conflictingLesson.getTimeslot();
            for (Timeslot unavailabilityTimeslot : unavailabilityList) {
                if (unavailabilityTimeslot.equals(lessonTimeslot)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Constraint professorAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter(lesson -> lesson.getProfessor() != null) // Only for lessons with an assigned professor
                .ifExists(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.lessThan(Lesson::getId))
                .filter(this::hasProfessorUnavailabilityConflict)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Professor unavailability conflict");
    }

    private boolean hasProfessorUnavailabilityConflict(Lesson conflictingLesson) {
        Professor professor = conflictingLesson.getProfessor();
        List<Timeslot> professorUnavailability = professor.getUnavailability();
        return hasUnavailabilityConflict(conflictingLesson, professorUnavailability);
    }

    private Constraint courseAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter(lesson -> lesson.getSubjectCourse() != null)
                .ifExists(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.lessThan(Lesson::getId))
                .filter(this::hasCourseUnavailabilityConflict)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Course unavailability conflict");
    }

    private boolean hasCourseUnavailabilityConflict(Lesson conflictingLesson) {
        SubjectCourse subjectCourse = conflictingLesson.getSubjectCourse();
        Course course = subjectCourse.getCourse();
        List<Timeslot> courseUnavailability = course.getUnavailability();
        return hasUnavailabilityConflict(conflictingLesson, courseUnavailability);
    }

    private Constraint professorTimeEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getProfessor))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher time efficiency");
    }
}
