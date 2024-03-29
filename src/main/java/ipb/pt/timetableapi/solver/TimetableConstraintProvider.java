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
                unassignedLessons(constraintFactory),
                classroomConflict(constraintFactory),
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
                startTimeHighZone(constraintFactory),
                startTimeMediumHighZone(constraintFactory),
                startTimeMediumZone(constraintFactory),
                startTimeMediumLowZone(constraintFactory),
                startTimeLowZone(constraintFactory),
                endTimeHighZone(constraintFactory),
                endTimeMediumHighZone(constraintFactory),
                endTimeMediumZone(constraintFactory),
                endTimeMediumLowZone(constraintFactory),
                endTimeLowZone(constraintFactory)
        };
    }

    private Constraint unassignedLessons(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> lessonUnit.getTimeslot() == null || lessonUnit.getClassroom() == null)
                .penalizeConfigurable(TimetableConstraintConstant.UNASSIGNED_LESSON);
    }

    private Constraint classroomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.equal(LessonUnit::getClassroom),
                        Joiners.lessThan(LessonUnit::getId))
                .penalizeConfigurable(TimetableConstraintConstant.CLASSROOM_CONFLICT);
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
                .join(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.lessThan(LessonUnit::getId))
                .filter(this::isEvenAndOdd)
                .penalizeConfigurable(TimetableConstraintConstant.COURSE_LESSONS_CONFLICT);
    }

    private Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectCourse().getCourse()),
                        Joiners.equal(lessonUnit -> lessonUnit.getLesson().getSubjectCourse().getPeriod()),
                        Joiners.lessThan(LessonUnit::getId))
                .filter(this::hasStudentGroupConflict)
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

    private Constraint lessonBlockEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .groupBy(LessonUnit::getLesson, lessonUnit -> lessonUnit.getTimeslot().getDayOfWeek(), toList())
                .filter((lesson, dayOfWeek, lessonUnitsInDay) -> checkIfTheUnitsAreConsecutive(lessonUnitsInDay))
                .penalizeConfigurable(TimetableConstraintConstant.LESSON_BLOCK_EFFICIENCY);
    }

    private Constraint lessonClassroomEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal(LessonUnit::getLesson))
                .filter((lessonUnit1, lessonUnit2) -> !lessonUnit1.getClassroom().equals(lessonUnit2.getClassroom())
                        && lessonUnit1.getTimeslot().getDayOfWeek().equals(lessonUnit2.getTimeslot().getDayOfWeek()))
                .penalizeConfigurable(TimetableConstraintConstant.LESSON_CLASSROOM_EFFICIENCY);
    }

    private Constraint startTimeHighZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isStartTimeHighZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_HIGH_ZONE);
    }

    private Constraint startTimeMediumHighZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isStartTimeMediumHighZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_MEDIUM_HIGH_ZONE);
    }

    private Constraint startTimeMediumZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isStartTimeMediumZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_MEDIUM_ZONE);
    }

    private Constraint startTimeMediumLowZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isStartTimeMediumLowZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_MEDIUM_LOW_ZONE);
    }

    private Constraint startTimeLowZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isStartTimeLowZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.START_TIME_LOW_ZONE);
    }

    private Constraint endTimeHighZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isEndTimeHighZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_HIGH_ZONE);
    }

    private Constraint endTimeMediumHighZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isEndTimeMediumHighZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_MEDIUM_HIGH_ZONE);
    }

    private Constraint endTimeMediumZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isEndTimeMediumZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_MEDIUM_ZONE);
    }

    private Constraint endTimeMediumLowZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isEndTimeMediumLowZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_MEDIUM_LOW_ZONE);
    }

    private Constraint endTimeLowZone(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> isEndTimeLowZone(lessonUnit.getTimeslot()))
                .rewardConfigurable(TimetableConstraintConstant.END_TIME_LOW_ZONE);
    }

    private boolean isEvenAndOdd(LessonUnit lessonUnit1, LessonUnit lessonUnit2) {
        SubjectCourse subjectCourse1 = lessonUnit1.getLesson().getSubjectCourse();
        SubjectCourse subjectCourse2 = lessonUnit2.getLesson().getSubjectCourse();

        boolean isSameCourse = subjectCourse1.getCourse().equals(subjectCourse2.getCourse());
        if (!isSameCourse) return false;

        boolean areTheyOddPeriods = Integer.parseInt(subjectCourse1.getPeriod().getAbbreviation()) % 2 != 0
                && Integer.parseInt(subjectCourse2.getPeriod().getAbbreviation()) % 2 != 0;
        boolean areTheyEvenPeriods = Integer.parseInt(subjectCourse1.getPeriod().getAbbreviation()) % 2 == 0
                && Integer.parseInt(subjectCourse2.getPeriod().getAbbreviation()) % 2 == 0;

        return !areTheyOddPeriods || !areTheyEvenPeriods;
    }

    private boolean hasStudentGroupConflict(LessonUnit lessonUnit1, LessonUnit lessonUnit2) {
        Lesson lesson1 = lessonUnit1.getLesson();
        Lesson lesson2 = lessonUnit2.getLesson();

        if (lesson1.getGroupCount() == 0 || lesson2.getGroupCount() == 0) {
            return true;
        }

        if (lesson1.getGroupCount().equals(lesson2.getGroupCount())) {
            return lesson1.getGroupNumber().equals(lesson2.getGroupNumber());
        }

        return hasStudentGroupConflict(lesson1, lesson2);
    }

    private boolean hasStudentGroupConflict(Lesson lesson1, Lesson lesson2) {
        Lesson first = lesson1.getGroupCount() < lesson2.getGroupCount() ? lesson1 : lesson2;
        Lesson second = lesson1.getGroupCount() > lesson2.getGroupCount() ? lesson1 : lesson2;

        double ratio = (double) second.getGroupCount() / first.getGroupCount();
        boolean isMultiple = ratio % 1 == 0;

        int n1 = first.getGroupNumber();
        int n2 = second.getGroupNumber();

        if (isMultiple) {
            for (int i = 1; i < first.getGroupCount(); i++) {
                int n3 = (int) ratio * i;
                int n4 = (int) (ratio * (i + 1));

                if (i == 1 && n1 <= i && n2 <= n3) return true;
                if (n1 > i && n2 > n3 && n1 <= (i + 1) && n2 <= n4) return true;
            }
        } else {
            for (int i = 1; i < first.getGroupCount(); i++) {
                int n3 = (int) Math.ceil(ratio * i);
                int n4 = (int) Math.ceil(ratio * (i + 1));

                if (i == 1 && n1 <= i && n2 <= n3) return true;
                if (n1 > i && n2 >= n3 && n1 <= (i + 1) && n2 <= n4) return true;
            }
        }

        return false;
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

    private boolean hasCourseUnavailabilityConflict(LessonUnit conflictingLessonUnit) {
        SubjectCourse subjectCourse = conflictingLessonUnit.getLesson().getSubjectCourse();
        Course course = subjectCourse.getCourse();
        List<Timeslot> courseUnavailability = course.getUnavailability();
        return hasUnavailabilityConflict(conflictingLessonUnit, courseUnavailability);
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

    private boolean isStartTimeHighZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("13:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("18:00"));
    }

    private boolean isStartTimeMediumHighZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("13:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("14:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("18:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("19:00"));
    }

    private boolean isStartTimeMediumZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("14:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("15:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("19:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("20:00"));
    }

    private boolean isStartTimeMediumLowZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("15:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("16:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("20:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("21:00"));
    }

    private boolean isStartTimeLowZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getStartTime().isAfter(LocalTime.parse("16:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("17:00"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("21:30"))
                && timeslot.getStartTime().isAfter(LocalTime.parse("22:00"));
    }

    private boolean isEndTimeHighZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("13:00"));
    }

    private boolean isEndTimeMediumHighZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("12:00"))
                && timeslot.getEndTime().isAfter(LocalTime.parse("12:30"));
    }

    private boolean isEndTimeMediumZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("11:00"))
                && timeslot.getEndTime().isAfter(LocalTime.parse("11:30"));
    }

    private boolean isEndTimeMediumLowZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("10:00"))
                && timeslot.getEndTime().isAfter(LocalTime.parse("10:30"));
    }

    private boolean isEndTimeLowZone(Timeslot timeslot) {
        return timeslot != null
                && timeslot.getEndTime().isAfter(LocalTime.parse("09:00"))
                && timeslot.getEndTime().isAfter(LocalTime.parse("09:30"));
    }
}
