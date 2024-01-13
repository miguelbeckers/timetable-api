package ipb.pt.timetableapi.newData.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany
    @JsonIgnore
    private List<Timeslot> unavailability = new ArrayList<>();
    @OneToMany
    @JsonIgnore
    private List<ClassroomResource> classroomResources = new ArrayList<>();
}
