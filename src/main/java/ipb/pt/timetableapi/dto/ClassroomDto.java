package ipb.pt.timetableapi.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassroomDto {
    private Long id;
    private String name;
    private String abbreviation;
    private List<Long> unavailabilityIds = new ArrayList<>();
    private List<Long> classroomResourceIds = new ArrayList<>();
    private Long classroomTypeId;
}
