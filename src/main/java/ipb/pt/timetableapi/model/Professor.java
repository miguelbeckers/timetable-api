package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Professor {
    @Id
    private Long id;
    private String name;
    private String abbreviation;
    @ManyToOne
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Timeslot> unavailability = new ArrayList<>();
}
