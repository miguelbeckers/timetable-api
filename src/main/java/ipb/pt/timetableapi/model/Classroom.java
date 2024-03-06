package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Classroom {
    @Id
    @PlanningId
    private Long id;
    private String name;
    private String abbreviation;
    @ManyToOne
    private ClassroomType classroomType;
    @ManyToMany
    private List<Timeslot> unavailability = new ArrayList<>();
    @OneToMany
    private List<ClassroomResource> classroomResources = new ArrayList<>();
}
