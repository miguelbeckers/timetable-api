package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClassroomResource {
    @Id
    private Long id;
    private Integer quantity;
    @ManyToOne(cascade = CascadeType.ALL)
    private Resource resource;
}
