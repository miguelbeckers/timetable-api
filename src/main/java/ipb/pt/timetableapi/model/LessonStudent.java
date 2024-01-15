package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LessonStudent {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //TODO: change to Lesson
//    @ManyToOne
//    private Lesson lesson;
    @ManyToOne
    private Student student;
}
