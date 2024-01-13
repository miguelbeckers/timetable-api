package ipb.pt.timetableapi.newData.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DepartmentDto {
    @NotEmpty(message = "Empty name")
    private String name;
}
