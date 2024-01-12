package ipb.pt.timetableapi.optimizer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonResourceDto {
    @NotNull(message = "Null lesson")
    private Long lessonId;
    @NotNull(message = "Null resource")
    private Long resourceId;
}
