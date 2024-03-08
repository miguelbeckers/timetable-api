package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.CheckpointConverter;
import ipb.pt.timetableapi.dto.CheckpointDto;
import ipb.pt.timetableapi.model.Checkpoint;
import ipb.pt.timetableapi.repository.CheckpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CheckpointService {
    private final CheckpointRepository checkpointRepository;
    private final CheckpointConverter checkpointConverter;

    @Autowired
    public CheckpointService(CheckpointRepository checkpointRepository,
                             CheckpointConverter checkpointConverter) {
        this.checkpointRepository = checkpointRepository;
        this.checkpointConverter = checkpointConverter;
    }

    public List<CheckpointDto> findAll() {
        return checkpointConverter.toDto(checkpointRepository.findAll());
    }

    public CheckpointDto findById(Long id) {
        return checkpointConverter.toDto(checkpointRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checkpoint not found")));
    }

    public CheckpointDto create(CheckpointDto checkpointDto) {
        Checkpoint checkpoint = checkpointConverter.toModel(checkpointDto);
        return checkpointConverter.toDto(checkpointRepository.save(checkpoint));
    }

    public CheckpointDto update(CheckpointDto checkpointDto, Long id) {
        checkpointRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checkpoint not found"));

        Checkpoint checkpoint = checkpointConverter.toModel(checkpointDto);
        return checkpointConverter.toDto(checkpointRepository.save(checkpoint));
    }

    public void delete(Long id) {
        Checkpoint checkpoint = checkpointRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checkpoint not found"));

        checkpointRepository.delete(checkpoint);
    }

    public void deleteAll() {
        checkpointRepository.deleteAll();
    }
}
