package ipb.pt.timetableapi.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PlanningEntity
public class LessonUnit {
    @Id
    private Long id;
    @ManyToOne
    private Lesson lesson;

    // Initialized/Changed during planning
    @ManyToOne(cascade = CascadeType.ALL)
    @PlanningVariable(valueRangeProviderRefs = "timeslotRange")
    private Timeslot timeslot;
    @ManyToOne(cascade = CascadeType.ALL)
    @PlanningVariable(valueRangeProviderRefs = "classroomRange")
    private Classroom classroom;
}
