package ipb.pt.timetableapi.dto;

import lombok.Data;

@Data
public class LessonResourceDto {
    private Long id;
    private Long resourceId;
    private double quantity;
}
