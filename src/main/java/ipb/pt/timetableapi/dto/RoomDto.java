package ipb.pt.timetableapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RoomDto {
    @NotEmpty(message = "Empty name")
    private String name;
}
