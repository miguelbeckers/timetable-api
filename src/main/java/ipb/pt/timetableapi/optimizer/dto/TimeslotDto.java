package ipb.pt.timetableapi.optimizer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class TimeslotDto {
    @NotNull(message = "Empty day of the week")
    private DayOfWeek dayOfWeek;
    @NotNull(message = "Empty start time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    @NotNull(message = "Empty end time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;
}
