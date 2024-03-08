package ipb.pt.timetableapi.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class CheckpointDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private int hardScore;
    private int softScore;
}
