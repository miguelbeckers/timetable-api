package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String subject;
    private String teacher;
    private String studentGroup;
    private String color;

    // Initialized/Changed by AI
    @ManyToOne
    private Timeslot timeslot;
    @ManyToOne
    private Classroom classroom;

    @Override
    public String toString() {
        return subject + " [" + id + "]";
    }
}
