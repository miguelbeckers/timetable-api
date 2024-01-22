package ipb.pt.timetableapi.model;

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
    @ManyToOne
    private Course course;
    @ManyToOne
    private Subject subject;
    @ManyToOne
    private Period period;
}
