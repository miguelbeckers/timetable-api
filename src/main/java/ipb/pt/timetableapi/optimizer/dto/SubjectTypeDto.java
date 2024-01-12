package ipb.pt.timetableapi.optimizer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SubjectTypeDto {
    @NotEmpty(message = "empty name")
    private String name;
}
