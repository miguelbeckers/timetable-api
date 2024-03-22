package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.entity.PlanningPin;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Entity
@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class LessonUnit {
    @Id
    @PlanningId
    private Long id;
    @ManyToOne
    private Lesson lesson;
    private Double blockSize;
    @PlanningPin
    private Boolean isPinned = false;

    // Initialized/Changed during planning
    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "timeslotRange")
    private Timeslot timeslot;
    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "classroomRange")
    private Classroom classroom;
}
