package ipb.pt.timetableapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassroomDto {
    @NotEmpty(message = "Empty name")
    private String name;
    private List<Long> unavailabilityIds = new ArrayList<>();
    private List<Long> resourceIds = new ArrayList<>();
}
