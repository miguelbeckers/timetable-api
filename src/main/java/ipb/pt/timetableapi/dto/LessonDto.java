package ipb.pt.timetableapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LessonDto {
    @NotEmpty(message = "Empty name")
    private String subject;
    @NotEmpty(message = "Empty teacher")
    private String teacher;
    @NotEmpty(message = "Empty student group")
    private String studentGroup;
}