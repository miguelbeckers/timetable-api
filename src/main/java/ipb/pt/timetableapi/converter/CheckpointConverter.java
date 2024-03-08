package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.CheckpointDto;
import ipb.pt.timetableapi.model.Checkpoint;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CheckpointConverter {
    public CheckpointDto toDto(Checkpoint checkpoint) {
        CheckpointDto checkpointDto = new CheckpointDto();
        BeanUtils.copyProperties(checkpoint, checkpointDto);
        return checkpointDto;
    }

    public List<CheckpointDto> toDto(List<Checkpoint> checkpoints) {
        return checkpoints.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Checkpoint toModel(CheckpointDto checkpointDto) {
        Checkpoint checkpoint = new Checkpoint();
        BeanUtils.copyProperties(checkpointDto, checkpoint);
        return checkpoint;
    }

    public List<Checkpoint> toModel(List<CheckpointDto> checkpointDtos) {
        return checkpointDtos.stream().map(this::toModel).collect(Collectors.toList());
    }
}
