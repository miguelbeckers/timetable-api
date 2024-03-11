package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.*;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
//import static org.optaplanner.core.api.score.stream.ConstraintCollectors.toList;

public class TimetableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                roomConflict(constraintFactory),
                professorConflict(constraintFactory),
                courseLessonsConflict(constraintFactory), // TODO: Look for the year -> solve first
                studentGroupConflict(constraintFactory), // TODO: check the groups

                resourceAvailability(constraintFactory),
                classroomAvailability(constraintFactory),
                professorAvailability(constraintFactory),
                courseAvailability(constraintFactory),

                lessonBlockSizeEfficiency(constraintFactory), // TODO: check blocks with different sizes
                lessonTimeEfficiency(constraintFactory),
                lessonClassroomEfficiency(constraintFactory),

                // Soft constraints
                professorTimeEfficiency(constraintFactory),
                startTimeBetweenTenAndTwo(constraintFactory),
        };
    }

    private Constraint roomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.equal(LessonUnit::getClassroom),
                        Joiners.lessThan(LessonUnit::getId))
                .penalizeConfigurable(TimetableConstraintConstants.ROOM_CONFLICT);
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
                .penalizeConfigurable(TimetableConstraintConstants.PROFESSOR_CONFLICT);
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
                .penalizeConfigurable(TimetableConstraintConstants.COURSE_LESSONS_CONFLICT);
    }

    private Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
//                .filter(lessonUnit -> lessonUnit.getLesson().getGroupCount() > 0)
                .join(LessonUnit.class,
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectCourse().getCourse()),
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectCourse().getPeriod()),
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectCourse().getSubject()),
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectType()))
                .filter((lessonUnit1, lessonUnit2) -> (
                        !lessonUnit1.getLesson().equals(lessonUnit2.getLesson())
                                && lessonUnit1.getLesson().getName().equals(lessonUnit2.getLesson().getName())
                                && lessonUnit1.getTimeslot().equals(lessonUnit2.getTimeslot())
                ))
                .penalizeConfigurable(TimetableConstraintConstants.STUDENT_GROUP_CONFLICT);
    }

    private Constraint resourceAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> lessonUnit.getClassroom() != null)
                .filter(lessonUnit -> !lessonUnit.getLesson().getLessonResources().isEmpty())
                .filter(this::checkResourceAvailability)
                .penalizeConfigurable(TimetableConstraintConstants.RESOURCE_AVAILABILITY);
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
                .penalizeConfigurable(TimetableConstraintConstants.CLASSROOM_AVAILABILITY);
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
                .penalizeConfigurable(TimetableConstraintConstants.PROFESSOR_AVAILABILITY);
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
                .penalizeConfigurable(TimetableConstraintConstants.COURSE_AVAILABILITY);
    }

    private boolean hasCourseUnavailabilityConflict(LessonUnit conflictingLessonUnit) {
        SubjectCourse subjectCourse = conflictingLessonUnit.getLesson().getSubjectCourse();
        Course course = subjectCourse.getCourse();
        List<Timeslot> courseUnavailability = course.getUnavailability();
        return hasUnavailabilityConflict(conflictingLessonUnit, courseUnavailability);
    }

    private Constraint lessonBlockSizeEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .groupBy(LessonUnit::getLesson, lessonUnit -> lessonUnit.getTimeslot().getDayOfWeek(), count())
                .filter((lesson, dayOfWeek, count) -> (
                        count != lesson.getHoursPerWeek()
                                * TimetableConstraintConstants.HOUR / TimetableConstraintConstants.UNIT / lesson.getBlocks()))
                .penalizeConfigurable(TimetableConstraintConstants.LESSON_BLOCK_SIZE_EFFICIENCY);
    }

    private Constraint lessonTimeEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal(LessonUnit::getLesson))
                .filter(TimetableConstraintProvider::checkIfTheLessonsAreOutOfTheBlock)
                .penalizeConfigurable(TimetableConstraintConstants.LESSON_TIME_EFFICIENCY);
    }

    public static boolean checkIfTheLessonsAreOutOfTheBlock(LessonUnit lessonUnit1, LessonUnit lessonUnit2) {
        if (lessonUnit1.getTimeslot().getDayOfWeek() != lessonUnit2.getTimeslot().getDayOfWeek())
            return false;

        int blocks = lessonUnit1.getLesson().getBlocks();
        double hoursPerWeek = lessonUnit1.getLesson().getHoursPerWeek();
        double unitsPerDay = hoursPerWeek * TimetableConstraintConstants.HOUR / TimetableConstraintConstants.UNIT / blocks;

        long minutesBetween = Duration.between(
                lessonUnit1.getTimeslot().getStartTime(),
                lessonUnit2.getTimeslot().getStartTime()
        ).abs().toMinutes();

        return minutesBetween > unitsPerDay * TimetableConstraintConstants.UNIT;
    }

    private Constraint lessonClassroomEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal(LessonUnit::getLesson))
                .filter((lessonUnit1, lessonUnit2) -> !lessonUnit1.getClassroom().equals(lessonUnit2.getClassroom())
                        && lessonUnit1.getTimeslot().getDayOfWeek().equals(lessonUnit2.getTimeslot().getDayOfWeek()))
                .penalizeConfigurable(TimetableConstraintConstants.LESSON_CLASSROOM_EFFICIENCY);
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
                .rewardConfigurable(TimetableConstraintConstants.PROFESSOR_TIME_EFFICIENCY);
    }

    private Constraint startTimeBetweenTenAndTwo(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isStartTimeBetweenTenAndTwo(lessonUnit.getTimeslot().getStartTime()))
                .rewardConfigurable(TimetableConstraintConstants.START_TIME_BETWEEN_TEN_AND_TWO);
    }

    private boolean isStartTimeBetweenTenAndTwo(LocalTime startTime) {
        return startTime != null
                && startTime.isAfter(LocalTime.of(10, 0))
                && startTime.isBefore(LocalTime.of(14, 0));
    }
}
