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
public class Course {
    @Id
    private Long id;
    private String name;
    private String abbreviation;
    @ManyToOne
    private Department department;
    @OneToMany
    private List<Timeslot> unavailability = new ArrayList<>();
    @OneToMany
    private List<Period> coursePeriod = new ArrayList<>();
}
