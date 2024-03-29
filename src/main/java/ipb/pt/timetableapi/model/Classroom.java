package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = "id")
public class Classroom {
    @Id
    @PlanningId
    private Long id;
    private String name;
    private String abbreviation;
    @ManyToOne
    private ClassroomType classroomType;
    @OneToMany(fetch = FetchType.EAGER)
    private List<ClassroomResource> classroomResources = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Timeslot> unavailability = new ArrayList<>();
}
