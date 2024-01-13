package ipb.pt.timetableapi.newData.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LessonDto {
    @NotEmpty(message = "Empty name")
    private String name;
    @NotEmpty(message = "Empty color")
    private String color;
    @NotNull(message = "Null teacher")
    private Long teacherId;
    @NotNull(message = "Null subjectCourse")
    private Long subjectCourseId;
    @NotNull(message = "Null subjectType")
    private Long subjectTypeId;
    private List<Long> resourceIds = new ArrayList<>();
}
