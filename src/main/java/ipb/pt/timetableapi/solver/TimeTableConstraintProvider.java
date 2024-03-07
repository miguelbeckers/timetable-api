package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.*;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;

public class TimeTableConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                roomConflict(constraintFactory),
                professorConflict(constraintFactory),
                courseLessonsConflict(constraintFactory),
                studentGroupConflict(constraintFactory),

                resourceAvailability(constraintFactory),
                classroomAvailability(constraintFactory),
                professorAvailability(constraintFactory),
                courseAvailability(constraintFactory),

                lessonBlockSizeEfficiency(constraintFactory),
                lessonTimeEfficiency(constraintFactory),
                lessonClassroomEfficiency(constraintFactory),

                // Soft constraints
                professorTimeEfficiency(constraintFactory),
                startTimeBetweenTenAndTwo(constraintFactory),
        };
    }

    private static final HardSoftScore ROOM_CONFLICT_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore PROFESSOR_CONFLICT_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore COURSE_LESSONS_CONFLICT_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore STUDENT_GROUP_CONFLICT_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore RESOURCE_AVAILABILITY_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore CLASSROOM_AVAILABILITY_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore PROFESSOR_AVAILABILITY_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore COURSE_AVAILABILITY_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore LESSON_BLOCK_SIZE_EFFICIENCY_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore LESSON_TIME_EFFICIENCY_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore LESSON_CLASSROOM_EFFICIENCY_SCORE = HardSoftScore.ofHard(1);
    private static final HardSoftScore PROFESSOR_TIME_EFFICIENCY_SCORE = HardSoftScore.ofSoft(1);
    private static final HardSoftScore START_TIME_BETWEEN_TEN_AND_TWO_SCORE = HardSoftScore.ofSoft(1);


    private Constraint roomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.equal(LessonUnit::getClassroom),
                        Joiners.lessThan(LessonUnit::getId))
                .penalize(ROOM_CONFLICT_SCORE)
                .asConstraint("Room conflict");
    }

    private Constraint professorConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.lessThan(LessonUnit::getId),
                        Joiners.filtering((lessonUnit, otherLessonUnit) ->
                                !Collections.disjoint(
                                        lessonUnit.getLesson().getProfessors(),
                                        otherLessonUnit.getLesson().getProfessors()
                                )))
                .penalize(PROFESSOR_CONFLICT_SCORE)
                .asConstraint("Teacher conflict");
    }

    private Constraint courseLessonsConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal(LessonUnit::getTimeslot))
                .filter((lessonUnit1, lessonUnit2) -> (
                        lessonUnit1.getLesson().getSubjectCourse().getCourse()
                                .equals(lessonUnit2.getLesson().getSubjectCourse().getCourse())
                                && !lessonUnit1.getLesson().equals(lessonUnit2.getLesson())
                ))
                .penalize(COURSE_LESSONS_CONFLICT_SCORE)
                .asConstraint("Course lessons conflict");
    }

    private Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class,
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectCourse().getCourse()),
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectCourse().getPeriod()),
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectCourse().getSubject()),
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectType()))
                .filter((lessonUnit1, lessonUnit2) -> (
                        !lessonUnit1.getLesson().equals(lessonUnit2.getLesson())
                                && lessonUnit1.getLesson().getName() != null
                                && lessonUnit1.getLesson().getName().equals(lessonUnit2.getLesson().getName())
                                && lessonUnit1.getTimeslot().equals(lessonUnit2.getTimeslot())
                ))
                .penalize(STUDENT_GROUP_CONFLICT_SCORE)
                .asConstraint("Student group conflict");
    }

    private Constraint resourceAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> lessonUnit.getClassroom() != null)
                .filter(lessonUnit -> !lessonUnit.getLesson().getLessonResources().isEmpty())
                .filter(this::checkResourceAvailability)
                .penalize(RESOURCE_AVAILABILITY_SCORE)
                .asConstraint("Resource availability conflict");
    }

    private boolean checkResourceAvailability(LessonUnit conflictingLesson) {
        List<LessonResource> lessonResources = conflictingLesson.getLesson().getLessonResources();
        List<ClassroomResource> classroomResources = conflictingLesson.getClassroom().getClassroomResources();

        return lessonResources.stream().anyMatch(lessonResource -> classroomResources.stream()
                .anyMatch(classroomResource -> isResourceAvailable(lessonResource, classroomResource)));
    }

    private boolean isResourceAvailable(LessonResource lessonResource, ClassroomResource classroomResource) {
        return lessonResource.getResource().getId().equals(classroomResource.getResource().getId())
                && lessonResource.getQuantity() <= classroomResource.getQuantity();
    }

    private Constraint classroomAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> lessonUnit.getClassroom() != null)
                .ifExists(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.lessThan(LessonUnit::getId))
                .filter(this::hasClassroomUnavailabilityConflict)
                .penalize(CLASSROOM_AVAILABILITY_SCORE)
                .asConstraint("Classroom unavailability conflict");
    }

    private boolean hasClassroomUnavailabilityConflict(LessonUnit conflictingLesson) {
        Classroom classroom = conflictingLesson.getClassroom();
        List<Timeslot> lessonUnavailability = classroom.getUnavailability();

        return hasUnavailabilityConflict(conflictingLesson, lessonUnavailability);
    }

    private boolean hasUnavailabilityConflict(LessonUnit conflictingLesson, List<Timeslot> unavailability) {
        if (unavailability != null && !unavailability.isEmpty()) {
            Timeslot lessonTimeslot = conflictingLesson.getTimeslot();
            for (Timeslot unavailabilityTimeslot : unavailability) {
                if (unavailabilityTimeslot.equals(lessonTimeslot)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Constraint professorAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> !lessonUnit.getLesson().getProfessors().isEmpty())
                .ifExists(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.lessThan(LessonUnit::getId))
                .filter(this::hasProfessorUnavailabilityConflict)
                .penalize(PROFESSOR_AVAILABILITY_SCORE)
                .asConstraint("Professor unavailability conflict");
    }

    private boolean hasProfessorUnavailabilityConflict(LessonUnit conflictingLessonUnit) {
        List<Professor> professors = conflictingLessonUnit.getLesson().getProfessors();

        for (Professor professor : professors) {
            List<Timeslot> professorUnavailability = professor.getUnavailability();
            if (!professorUnavailability.isEmpty() && hasUnavailabilityConflict(conflictingLessonUnit, professorUnavailability)) {
                return true;
            }
        }

        return false;
    }

    private Constraint courseAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> lessonUnit.getLesson().getSubjectCourse() != null)
                .ifExists(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.lessThan(LessonUnit::getId))
                .filter(this::hasCourseUnavailabilityConflict)
                .penalize(COURSE_AVAILABILITY_SCORE)
                .asConstraint("Course unavailability conflict");
    }

    private boolean hasCourseUnavailabilityConflict(LessonUnit conflictingLessonUnit) {
        SubjectCourse subjectCourse = conflictingLessonUnit.getLesson().getSubjectCourse();
        Course course = subjectCourse.getCourse();
        List<Timeslot> courseUnavailability = course.getUnavailability();
        return hasUnavailabilityConflict(conflictingLessonUnit, courseUnavailability);
    }

    private Constraint lessonBlockSizeEfficiency(ConstraintFactory constraintFactory) {
        int UNITS_PER_HOUR = 2;

        return constraintFactory
                .forEach(LessonUnit.class)
                .groupBy(LessonUnit::getLesson, lessonUnit -> lessonUnit.getTimeslot().getDayOfWeek(), count())
                .filter((lesson, dayOfWeek, count) -> count != lesson.getHoursPerWeek() * UNITS_PER_HOUR / lesson.getBlocks())
                .penalize(LESSON_BLOCK_SIZE_EFFICIENCY_SCORE)
                .asConstraint("Lesson block size efficiency");
    }

    //FIXME: change to hard constraint
    private Constraint lessonTimeEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal(LessonUnit::getLesson))
                .filter((lessonUnit1, lessonUnit2) -> {
                    Duration between = Duration.between(
                            lessonUnit1.getTimeslot().getEndTime(),
                            lessonUnit2.getTimeslot().getStartTime()
                    );

                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .reward(LESSON_TIME_EFFICIENCY_SCORE)
                .asConstraint("Lesson time efficiency");
    }

    //FIXME: change to hard constraint
    private Constraint lessonClassroomEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal(LessonUnit::getLesson))
                .filter((lessonUnit1, lessonUnit2) -> lessonUnit1.getClassroom().equals(lessonUnit2.getClassroom())
                        && !lessonUnit1.getTimeslot().equals(lessonUnit2.getTimeslot()))
                .reward(LESSON_CLASSROOM_EFFICIENCY_SCORE)
                .asConstraint("Lesson classroom efficiency");
    }

    private Constraint professorTimeEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal((lessonUnit) -> lessonUnit.getLesson().getProfessors()))
                .filter((lessonUnit1, lessonUnit2) -> {
                    Duration between = Duration.between(
                            lessonUnit1.getTimeslot().getEndTime(),
                            lessonUnit2.getTimeslot().getStartTime()
                    );

                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })

                .reward(PROFESSOR_TIME_EFFICIENCY_SCORE)
                .asConstraint("Teacher time efficiency");
    }

    private Constraint startTimeBetweenTenAndTwo(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isStartTimeBetweenTenAndTwo(lessonUnit.getTimeslot().getStartTime()))
                .reward(START_TIME_BETWEEN_TEN_AND_TWO_SCORE)
                .asConstraint("Start time between 10 and 14");
    }

    private boolean isStartTimeBetweenTenAndTwo(LocalTime startTime) {
        return startTime != null
                && startTime.isAfter(LocalTime.of(10, 0))
                && startTime.isBefore(LocalTime.of(14, 0));
    }
}
