package ipb.pt.timetableapi.newData.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StudentDto {
    @NotEmpty(message = "Empty name")
    private String name;
    @NotEmpty(message = "Empty code")
    private String code;
}
