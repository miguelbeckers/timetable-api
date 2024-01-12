package ipb.pt.timetableapi.optimizer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SubjectDto {
    @NotEmpty(message = "Empty name")
    private String name;
    @NotEmpty(message = "Empty code")
    private String code;
}
