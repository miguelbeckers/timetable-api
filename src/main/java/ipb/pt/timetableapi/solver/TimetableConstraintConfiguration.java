package ipb.pt.timetableapi.solver;

import lombok.Data;
import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@Data
@ConstraintConfiguration
public class TimetableConstraintConfiguration {
    @ConstraintWeight("Room conflict")
    private HardSoftScore roomConflict = HardSoftScore.ofHard(1);
}
