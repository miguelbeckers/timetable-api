package ipb.pt.timetableapi.solver;

import lombok.Data;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@Data
@org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration
public class TimetableConstraintConfiguration {
    @ConstraintWeight(TimetableConstraintConstant.UNASSIGNED_LESSON)
    private HardSoftScore unassignedLesson = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.CLASSROOM_CONFLICT)
    private HardSoftScore classroomConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.PROFESSOR_CONFLICT)
    private HardSoftScore professorConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.COURSE_LESSONS_CONFLICT)
    private HardSoftScore courseLessonsConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.STUDENT_GROUP_CONFLICT)
    private HardSoftScore studentGroupConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.RESOURCE_AVAILABILITY)
    private HardSoftScore resourceAvailability = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.CLASSROOM_AVAILABILITY)
    private HardSoftScore classroomAvailability = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.PROFESSOR_AVAILABILITY)
    private HardSoftScore professorAvailability = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.COURSE_AVAILABILITY)
    private HardSoftScore courseAvailability = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.LESSON_BLOCK_EFFICIENCY)
    private HardSoftScore lessonBlockEfficiency = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.LESSON_CLASSROOM_EFFICIENCY)
    private HardSoftScore lessonClassroomEfficiency = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstant.PROFESSOR_TIME_EFFICIENCY)
    private HardSoftScore professorTimeEfficiency = HardSoftScore.ofSoft(1);

    @ConstraintWeight(TimetableConstraintConstant.START_TIME_HIGH_ZONE)
    private HardSoftScore startTimeEfficiencyHigh = HardSoftScore.ofSoft(5);

    @ConstraintWeight(TimetableConstraintConstant.START_TIME_MEDIUM_HIGH_ZONE)
    private HardSoftScore startTimeEfficiencyMediumHigh = HardSoftScore.ofSoft(4);

    @ConstraintWeight(TimetableConstraintConstant.START_TIME_MEDIUM_ZONE)
    private HardSoftScore startTimeEfficiencyMedium = HardSoftScore.ofSoft(3);

    @ConstraintWeight(TimetableConstraintConstant.START_TIME_MEDIUM_LOW_ZONE)
    private HardSoftScore startTimeEfficiencyMediumLow = HardSoftScore.ofSoft(2);

    @ConstraintWeight(TimetableConstraintConstant.START_TIME_LOW_ZONE)
    private HardSoftScore startTimeEfficiencyLow = HardSoftScore.ofSoft(1);

    @ConstraintWeight(TimetableConstraintConstant.END_TIME_HIGH_ZONE)
    private HardSoftScore endTimeEfficiencyHigh = HardSoftScore.ofSoft(5);

    @ConstraintWeight(TimetableConstraintConstant.END_TIME_MEDIUM_HIGH_ZONE)
    private HardSoftScore endTimeEfficiencyMediumHigh = HardSoftScore.ofSoft(4);

    @ConstraintWeight(TimetableConstraintConstant.END_TIME_MEDIUM_ZONE)
    private HardSoftScore endTimeEfficiencyMedium = HardSoftScore.ofSoft(3);

    @ConstraintWeight(TimetableConstraintConstant.END_TIME_MEDIUM_LOW_ZONE)
    private HardSoftScore endTimeEfficiencyMediumLow = HardSoftScore.ofSoft(2);

    @ConstraintWeight(TimetableConstraintConstant.END_TIME_LOW_ZONE)
    private HardSoftScore endTimeEfficiencyLow = HardSoftScore.ofSoft(1);
}
