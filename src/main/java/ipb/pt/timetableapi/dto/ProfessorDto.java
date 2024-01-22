package ipb.pt.timetableapi.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfessorDto {
    private Long id;
    private String name;
    private String abbreviation;
    private Long departmentId;
    private List<Long> unavailabilityIds = new ArrayList<>();
}
