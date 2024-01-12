package ipb.pt.timetableapi.optimizer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PeriodDto {
    @NotEmpty(message = "Empty name")
    private String name;
}
