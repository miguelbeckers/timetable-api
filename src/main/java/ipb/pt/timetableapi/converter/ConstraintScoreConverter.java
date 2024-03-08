package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.ConstraintScoreDto;
import ipb.pt.timetableapi.model.ConstraintScore;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConstraintScoreConverter {
    public ConstraintScoreDto toDto(ConstraintScore checkpoint) {
        ConstraintScoreDto checkpointDto = new ConstraintScoreDto();
        BeanUtils.copyProperties(checkpoint, checkpointDto);
        return checkpointDto;
    }

    public List<ConstraintScoreDto> toDto(List<ConstraintScore> checkpoints) {
        return checkpoints.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ConstraintScore toModel(ConstraintScoreDto checkpointDto) {
        ConstraintScore checkpoint = new ConstraintScore();
        BeanUtils.copyProperties(checkpointDto, checkpoint);
        return checkpoint;
    }

    public List<ConstraintScore> toModel(List<ConstraintScoreDto> checkpointDtos) {
        return checkpointDtos.stream().map(this::toModel).collect(Collectors.toList());
    }
}
