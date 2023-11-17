package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@PlanningEntity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String subject;
    @ManyToOne
    private Professor professor;
    private String studentGroup;
    private String color;
    private Integer groupSize;

    // Initialized/Changed during planning
    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "timeslotRange")
    private Timeslot timeslot;

    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "classroomRange")
    private Classroom classroom;
}
