package ipb.pt.timetableapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResourceDto {
    @NotNull(message = "Null quantity")
    private Integer quantity;
    @NotNull(message = "Null resourceType")
    private Long resourceTypeId;
}
