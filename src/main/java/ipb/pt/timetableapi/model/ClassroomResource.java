package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ClassroomResource {
    @Id
    private Long id;
    private Integer quantity;
    @ManyToOne
    private Resource resource;
}
