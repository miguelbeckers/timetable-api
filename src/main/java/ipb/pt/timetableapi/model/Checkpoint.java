package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Checkpoint {
    @Id
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private int hardScore;
    private int softScore;

    public long getDuration() {
        return endTime.toSecondOfDay() - startTime.toSecondOfDay();
    }
}
