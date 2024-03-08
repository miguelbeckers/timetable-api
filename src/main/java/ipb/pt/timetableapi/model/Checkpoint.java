package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Checkpoint {
    @Id
    private Long id;
    private UUID problemId;
    private LocalTime startTime;
    private LocalTime endTime;
    private int hardScore;
    private int softScore;
}
