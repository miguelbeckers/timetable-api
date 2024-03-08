package ipb.pt.timetableapi.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class CheckpointDto {
    private Long id;
    private UUID problemId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int hardScore;
    private int softScore;
}
