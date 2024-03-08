package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Department {
    @Id
    private Long id;
    private String name;
    private String abbreviation;
}
