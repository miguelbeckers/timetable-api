package ipb.pt.timetableapi.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LessonDto {
    private Long id;
    private String name;
//    private int groupNumber;
//    private int groupCount;
    private String color;
    private Double hoursPerWeek;
    private Integer blocks;
    private Long subjectCourseId;
    private Long subjectTypeId;
    private List<Long> professorIds = new ArrayList<>();
    private List<Long> lessonResourceIds = new ArrayList<>();
    private List<Long> studentIds = new ArrayList<>();
}
