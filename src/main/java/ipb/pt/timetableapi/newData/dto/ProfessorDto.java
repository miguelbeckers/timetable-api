package ipb.pt.timetableapi.newData.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfessorDto {
    @NotEmpty(message = "Empty name")
    private String name;
    @NotNull(message = "Null departmentId")
    private Long departmentId;
    private List<Long> unavailabilityIds = new ArrayList<>();
}
