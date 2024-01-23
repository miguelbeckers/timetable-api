package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LessonResource {
    @Id
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Resource resource;
    private double quantity;
}
