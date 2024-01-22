package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Classroom {
    @Id
    private Long id;
    private String name;
    private String abbreviation;
    @OneToMany
    private List<Timeslot> unavailability = new ArrayList<>();
    @OneToMany
    private List<ClassroomResource> classroomResources = new ArrayList<>();
    @ManyToOne
    private ClassroomType classroomType;
}
