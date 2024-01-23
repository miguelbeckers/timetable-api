package ipb.pt.timetableapi.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SubjectCourse {
    @Id
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;
    @ManyToOne(cascade = CascadeType.ALL)
    private Subject subject;
    @ManyToOne(cascade = CascadeType.ALL)
    private Period period;
}
