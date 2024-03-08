package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Score {
    @Id
    private Long id;
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
