package ipb.pt.timetableapi.solver;

import lombok.Data;
import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@Data
@ConstraintConfiguration
public class TimetableConstraintConfiguration {
    @ConstraintWeight(TimetableConstraintConstants.ROOM_CONFLICT)
    private HardSoftScore roomConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.PROFESSOR_CONFLICT)
    private HardSoftScore professorConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.COURSE_LESSONS_CONFLICT)
    private HardSoftScore courseLessonsConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.STUDENT_GROUP_CONFLICT)
    private HardSoftScore studentGroupConflict = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.RESOURCE_AVAILABILITY)
    private HardSoftScore resourceAvailability = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.CLASSROOM_AVAILABILITY)
    private HardSoftScore classroomAvailability = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.PROFESSOR_AVAILABILITY)
    private HardSoftScore professorAvailability = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.COURSE_AVAILABILITY)
    private HardSoftScore courseAvailability = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.LESSON_BLOCK_SIZE_EFFICIENCY)
    private HardSoftScore lessonBlockSizeEfficiency = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.LESSON_TIME_EFFICIENCY)
    private HardSoftScore lessonTimeEfficiency = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.LESSON_CLASSROOM_EFFICIENCY)
    private HardSoftScore lessonClassroomEfficiency = HardSoftScore.ofHard(1);

    @ConstraintWeight(TimetableConstraintConstants.PROFESSOR_TIME_EFFICIENCY)
    private HardSoftScore professorTimeEfficiency = HardSoftScore.ofSoft(1);

    @ConstraintWeight(TimetableConstraintConstants.START_TIME_BETWEEN_TEN_AND_TWO)
    private HardSoftScore startTimeBetweenTenAndTwo = HardSoftScore.ofSoft(1);
}
