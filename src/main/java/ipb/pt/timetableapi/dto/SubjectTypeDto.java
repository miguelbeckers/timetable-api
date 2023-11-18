package ipb.pt.timetableapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SubjectTypeDto {
    @NotEmpty(message = "empty name")
    private String name;
}
