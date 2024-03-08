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
public class ConstraintScore {
    @Id
    private Long id;
    private String name;
    private int roomConflict;
    private int professorConflict;
    private int courseLessonsConflict;
    private int studentGroupConflict;
    private int resourceAvailability;
    private int classroomAvailability;
    private int professorAvailability;
    private int courseAvailability;
    private int lessonBlockSizeEfficiency;
    private int lessonTimeEfficiency;
    private int lessonClassroomEfficiency;
    private int professorTimeEfficiency;
    private int startTimeBetweenTenAndTwo;
}
