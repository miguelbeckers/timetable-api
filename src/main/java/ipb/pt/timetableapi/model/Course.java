package ipb.pt.timetableapi.model;

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
public class Course {
    @Id
    private Long id;
    private String name;
    private String abbreviation;
    @ManyToOne(cascade = CascadeType.ALL)
    private Department department;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Timeslot> unavailability = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Period> coursePeriod = new ArrayList<>();
}
