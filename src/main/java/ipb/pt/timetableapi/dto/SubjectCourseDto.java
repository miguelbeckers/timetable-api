package ipb.pt.timetableapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectCourseDto {
    @NotNull(message = "Null courseId")
    private Long courseId;
    @NotNull(message = "Null subjectId")
    private Long subjectId;
}
