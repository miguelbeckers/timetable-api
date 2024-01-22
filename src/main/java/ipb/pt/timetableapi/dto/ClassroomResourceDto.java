package ipb.pt.timetableapi.dto;

import lombok.Data;

@Data
public class ClassroomResourceDto {
    private Long id;
    private Integer quantity;
    private Long resourceId;
}
