package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.*;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;
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
                resourceAvailability(constraintFactory),
                classroomAvailability(constraintFactory),
                professorAvailability(constraintFactory),
                courseAvailability(constraintFactory),
                courseLessonsConflict(constraintFactory),
                studentGroupConflict(constraintFactory),

                // Soft constraints
                professorTimeEfficiency(constraintFactory),
                lessonBlockSizeEfficiency(constraintFactory),
                lessonTimeEfficiency(constraintFactory),
                lessonClassroomEfficiency(constraintFactory)
        };
    }

    // Hard constraints

    private Constraint roomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class,
                        Joiners.equal(LessonUnit::getTimeslot),
                        Joiners.equal(LessonUnit::getClassroom),
                        Joiners.lessThan(LessonUnit::getId))
                .penalize(HardSoftScore.ONE_HARD)
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
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
    }

    private Constraint resourceAvailability(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .filter(lessonUnit -> lessonUnit.getClassroom() != null)
                .filter(lessonUnit -> !lessonUnit.getLesson().getLessonResources().isEmpty())
                .filter(this::checkResourceAvailability)
                .penalize(HardSoftScore.ONE_HARD)
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
                .penalize(HardSoftScore.ONE_HARD)
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
                .penalize(HardSoftScore.ONE_HARD)
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
                .penalize(HardSoftScore.ofHard(50))
                .asConstraint("Course unavailability conflict");
    }

    private boolean hasCourseUnavailabilityConflict(LessonUnit conflictingLessonUnit) {
        SubjectCourse subjectCourse = conflictingLessonUnit.getLesson().getSubjectCourse();
        Course course = subjectCourse.getCourse();
        List<Timeslot> courseUnavailability = course.getUnavailability();
        return hasUnavailabilityConflict(conflictingLessonUnit, courseUnavailability);
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
                .reward(HardSoftScore.ONE_HARD)
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
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student group conflict");
    }

    // Soft constraints
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

                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher time efficiency");
    }

    private Constraint lessonClassroomEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .join(LessonUnit.class, Joiners.equal(LessonUnit::getLesson))
                .filter((lessonUnit1, lessonUnit2) -> lessonUnit1.getClassroom().equals(lessonUnit2.getClassroom())
                        && !lessonUnit1.getTimeslot().equals(lessonUnit2.getTimeslot()))
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Lesson classroom efficiency");
    }

    private Constraint lessonBlockSizeEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(LessonUnit.class)
                .groupBy(LessonUnit::getLesson, lessonUnit -> lessonUnit.getTimeslot().getDayOfWeek(), count())
                .filter((lesson, dayOfWeek, count) -> count == lesson.getHoursPerWeek() / lesson.getBlocks())
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Lesson block size efficiency");
    }

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
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Lesson time efficiency");
    }
}
