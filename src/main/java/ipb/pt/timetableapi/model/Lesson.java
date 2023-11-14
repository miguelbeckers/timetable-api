package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PlanningEntity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String subject;
    @ManyToOne
    private Professor professor;
    private String studentGroup;
    private String color;
    private Integer groupSize;

    // Initialized/Changed during planning
    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "timeslotRange")
    private Timeslot timeslot;

    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "classroomRange")
    private Classroom classroom;

    public Lesson(String subject, Professor professor, String studentGroup, String color, Integer groupSize) {
        this.subject = subject;
        this.professor = professor;
        this.studentGroup = studentGroup;
        this.color = color;
        this.groupSize = groupSize;
    }

    @Override
    public String toString() {
        return subject + " [" + id + "]";
    }
}
