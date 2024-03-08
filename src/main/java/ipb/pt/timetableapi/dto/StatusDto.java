package ipb.pt.timetableapi.dto;

import lombok.Data;

@Data
public class StatusDto {
    private String status;
    private String initialScore;
    private String startTime;
    private long seconds;
}
