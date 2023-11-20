package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.*;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class TimeTableConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                roomConflict(constraintFactory),
                studentConflict(constraintFactory),
                professorConflict(constraintFactory),
                resourceAvailability(constraintFactory), // missing test
                classroomAvailability(constraintFactory), // missing test
                professorAvailability(constraintFactory), // missing test
                courseAvailability(constraintFactory), // missing test
                // Soft constraints
                professorTimeEfficiency(constraintFactory)
        };
    }

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

    private Constraint studentConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.lessThan(Lesson::getId))
                .filter(this::haveStudentConflict)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student conflict");
    }

    private boolean haveStudentConflict(Lesson lesson1, Lesson lesson2) {
        List<Student> students1 = getStudentsInLesson(lesson1);
        List<Student> students2 = getStudentsInLesson(lesson2);

        for (Student student1 : students1) {
            if (students2.contains(student1)) {
                return true; // Student is in two places at the same time
            }
        }

        return false;
    }

    private List<Student> getStudentsInLesson(Lesson lesson) {
        return lesson.getLessonStudents().stream()
                .map(LessonStudent::getStudent)
                .collect(Collectors.toList());
    }

    private Constraint professorConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getProfessor),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
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

    private Constraint resourceAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .filter(lesson -> !lesson.getLessonResources().isEmpty()) // Only for lessons with resources
                .ifExists(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getClassroom),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Resource availability");
    }

    private int calculateResourceShortage(Lesson lesson, Lesson conflictingLesson) {
        int shortage = 0;
        for (LessonResource lessonResource : lesson.getLessonResources()) {
            Resource resource = lessonResource.getResource();
            int requiredQuantity = lesson.getLessonStudents().size(); // Number of students in the lesson
            int availableQuantity = getAvailableQuantity(resource, lesson.getClassroom());
            int conflictingQuantity = getRequiredQuantityInConflictingLesson(resource, conflictingLesson);
            int totalRequiredQuantity = requiredQuantity + conflictingQuantity;
            int shortfall = Math.max(0, totalRequiredQuantity - availableQuantity);
            shortage += shortfall;
        }
        return shortage;
    }

    private int getAvailableQuantity(Resource resource, Classroom classroom) {
        // Gets the available quantity of the resource in the classroom
        for (ClassroomResource classroomResource : classroom.getClassroomResources()) {
            if (classroomResource.getResource().equals(resource)) {
                return classroomResource.getQuantity();
            }
        }
        return 0;
    }

    private int getRequiredQuantityInConflictingLesson(Resource resource, Lesson conflictingLesson) {
        // Gets the required quantity of the resource in the conflicting lesson
        int conflictingQuantity = 0;
        for (LessonResource conflictingLessonResource : conflictingLesson.getLessonResources()) {
            if (conflictingLessonResource.getResource().equals(resource)) {
                conflictingQuantity += conflictingLesson.getLessonStudents().size();
            }
        }
        return conflictingQuantity;
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
                .filter(lesson -> lesson.getSubjectCourse() != null) //
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
}
