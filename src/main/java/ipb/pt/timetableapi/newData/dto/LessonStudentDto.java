package ipb.pt.timetableapi.newData.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonStudentDto {
    @NotNull(message = "Null lesson")
    private Long lessonId;
    @NotNull(message = "Null student")
    private Long studentId;
}
