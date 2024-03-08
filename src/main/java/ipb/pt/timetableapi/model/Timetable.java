package ipb.pt.timetableapi.model;

import ipb.pt.timetableapi.solver.TimetableConstraintConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.constraintweight.ConstraintConfigurationProvider;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@PlanningSolution
public class Timetable {
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "timeslotRange")
    private List<Timeslot> timeslots;
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "classroomRange")
    private List<Classroom> classrooms;
    @PlanningEntityCollectionProperty
    private List<LessonUnit> lessonUnits;

    @PlanningScore
    private HardSoftScore score;

    @ConstraintConfigurationProvider
    private TimetableConstraintConfiguration timetableConfiguration;
}
