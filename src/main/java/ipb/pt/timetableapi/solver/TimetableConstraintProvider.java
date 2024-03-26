package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.*;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.toList;

public class TimetableConstraintProvider implements ConstraintProvider {

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

                lessonBlockEfficiency(constraintFactory),
                lessonClassroomEfficiency(constraintFactory),

                // Soft constraints
                startTimeEfficiencyHigh(constraintFactory),
                startTimeEfficiencyMediumHigh(constraintFactory),
                startTimeEfficiencyMedium(constraintFactory),
                startTimeEfficiencyMediumLow(constraintFactory),
                startTimeEfficiencyLow(constraintFactory),

                endTimeEfficiencyHigh(constraintFactory),
                endTimeEfficiencyMediumHigh(constraintFactory),
                endTimeEfficiencyMedium(constraintFactory),
                endTimeEfficiencyMediumLow(constraintFactory),
                endTimeEfficiencyLow(constraintFactory)

        };
    }

    private Constraint roomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.equal(LessonUnit::getClassroom),
                        Joiners.lessThan(LessonUnit::getId))
                .penalizeConfigurable(TimetableConstraintConstant.ROOM_CONFLICT);
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
                .penalizeConfigurable(TimetableConstraintConstant.PROFESSOR_CONFLICT);
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
                .penalizeConfigurable(TimetableConstraintConstant.COURSE_LESSONS_CONFLICT);
    }

    private Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
//                .filter(lessonUnit -> lessonUnit.getLesson().getGroupCount() > 0)
                .filter(lessonUnit -> lessonUnit.getLesson().getName() != null)
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
                .penalizeConfigurable(TimetableConstraintConstant.STUDENT_GROUP_CONFLICT);
    }

    private Constraint resourceAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> lessonUnit.getClassroom() != null)
                .filter(lessonUnit -> !lessonUnit.getLesson().getLessonResources().isEmpty())
                .filter(this::checkResourceAvailability)
                .penalizeConfigurable(TimetableConstraintConstant.RESOURCE_AVAILABILITY);
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
                .penalizeConfigurable(TimetableConstraintConstant.CLASSROOM_AVAILABILITY);
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
                .penalizeConfigurable(TimetableConstraintConstant.PROFESSOR_AVAILABILITY);
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
                .penalizeConfigurable(TimetableConstraintConstant.COURSE_AVAILABILITY);
    }

    private boolean hasCourseUnavailabilityConflict(LessonUnit conflictingLessonUnit) {
        SubjectCourse subjectCourse = conflictingLessonUnit.getLesson().getSubjectCourse();
        Course course = subjectCourse.getCourse();
        List<Timeslot> courseUnavailability = course.getUnavailability();
        return hasUnavailabilityConflict(conflictingLessonUnit, courseUnavailability);
    }

    private Constraint lessonBlockEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .groupBy(LessonUnit::getLesson, lessonUnit -> lessonUnit.getTimeslot().getDayOfWeek(), toList())
                .filter((lesson, dayOfWeek, lessonUnitsInDay) -> checkIfTheUnitsAreConsecutive(lessonUnitsInDay))
                .penalizeConfigurable(TimetableConstraintConstant.LESSON_BLOCK_EFFICIENCY);
    }

    private boolean checkIfTheUnitsAreConsecutive(List<LessonUnit> lessonUnitsInDay) {
        lessonUnitsInDay.sort(Comparator.comparing(unit -> unit.getTimeslot().getId()));

        for (int i = 0; i < lessonUnitsInDay.size(); i++) {
            LessonUnit currentLesson = lessonUnitsInDay.get(i);

            if (i > 0) {
                LessonUnit previousLesson = lessonUnitsInDay.get(i - 1);
                if (isAdjacentTimeslot(currentLesson, previousLesson)) {
                    return false;
                }
            }

            if (i < lessonUnitsInDay.size() - 1) {
                LessonUnit nextLesson = lessonUnitsInDay.get(i + 1);
                if (isAdjacentTimeslot(currentLesson, nextLesson)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isAdjacentTimeslot(LessonUnit lesson1, LessonUnit lesson2) {
        Long timeslotId1 = lesson1.getTimeslot().getId();
        Long timeslotId2 = lesson2.getTimeslot().getId();
        return Math.abs(timeslotId1 - timeslotId2) == 1;
    }

    private Constraint lessonClassroomEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal(LessonUnit::getLesson))
                .filter((lessonUnit1, lessonUnit2) -> !lessonUnit1.getClassroom().equals(lessonUnit2.getClassroom())
                        && lessonUnit1.getTimeslot().getDayOfWeek().equals(lessonUnit2.getTimeslot().getDayOfWeek()))
                .penalizeConfigurable(TimetableConstraintConstant.LESSON_CLASSROOM_EFFICIENCY);
    }

    private Constraint startTimeEfficiencyHigh(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> startTimeHigh(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_HIGH);
    }

    private Constraint startTimeEfficiencyMediumHigh(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> startTimeMediumHigh(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_MEDIUM_HIGH);
    }

    private Constraint startTimeEfficiencyMedium(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> startTimeMedium(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_MEDIUM);
    }

    private Constraint startTimeEfficiencyMediumLow(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> startTimeMediumLow(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_MEDIUM_LOW);
    }

    private Constraint startTimeEfficiencyLow(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> startTimeLow(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_LOW);
    }

    private Constraint endTimeEfficiencyHigh(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> endTimeHigh(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_HIGH);
    }

    private Constraint endTimeEfficiencyMediumHigh(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> endTimeMediumHigh(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_MEDIUM_HIGH);
    }

    private Constraint endTimeEfficiencyMedium(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> endTimeMedium(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_MEDIUM);
    }

    private Constraint endTimeEfficiencyMediumLow(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> endTimeMediumLow(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_MEDIUM_LOW);
    }

    private Constraint endTimeEfficiencyLow(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> endTimeLow(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_LOW);
    }

    private boolean startTimeHigh(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("13:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("18:00"));
    }

    private boolean startTimeMediumHigh(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("13:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("14:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("18:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("19:00"));
    }

    private boolean startTimeMedium(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("14:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("15:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("19:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("20:00"));
    }

    private boolean startTimeMediumLow(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("15:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("16:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("20:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("21:00"));
    }

    private boolean startTimeLow(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("16:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("17:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("21:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("22:00"));
    }

    private boolean endTimeHigh(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("13:00"));
    }

    private boolean endTimeMediumHigh(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("12:00"))
                && timeslot.getEndTime().isAfter(LocalTime.parse("12:30"));
    }

    private boolean endTimeMedium(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("11:00"))
                && timeslot.getEndTime().isAfter(LocalTime.parse("11:30"));
    }

    private boolean endTimeMediumLow(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("10:00"))
                && timeslot.getEndTime().isAfter(LocalTime.parse("10:30"));
    }

    private boolean endTimeLow(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("09:00"))
                && timeslot.getEndTime().isAfter(LocalTime.parse("09:30"));
    }
}
