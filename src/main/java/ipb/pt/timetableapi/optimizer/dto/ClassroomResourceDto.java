package ipb.pt.timetableapi.optimizer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassroomResourceDto {
    @NotNull(message = "Null quantity")
    private Integer quantity;
    @NotNull(message = "Null resourceType")
    private Long resourceTypeId;
}
