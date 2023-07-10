package ipb.pt.timetableapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassroomDto {
    @NotEmpty(message = "Empty name")
    private String name;
    @NotNull(message = "Empty capacity")
    private Integer capacity;
}
