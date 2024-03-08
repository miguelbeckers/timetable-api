package ipb.pt.timetableapi.dto;

import lombok.Data;

@Data
public class ConstraintScoreDto {
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
