package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.ConstraintScoreConverter;
import ipb.pt.timetableapi.dto.ConstraintScoreDto;
import ipb.pt.timetableapi.model.ConstraintScore;
import ipb.pt.timetableapi.repository.ConstraintScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ConstraintScoreService {
    private final ConstraintScoreRepository constraintScoreRepository;
    private final ConstraintScoreConverter constraintScoreConverter;

    @Autowired
    public ConstraintScoreService(ConstraintScoreRepository constraintScoreRepository,
                                  ConstraintScoreConverter constraintScoreConverter) {
        this.constraintScoreRepository = constraintScoreRepository;
        this.constraintScoreConverter = constraintScoreConverter;
    }

    public List<ConstraintScoreDto> findAll() {
        return constraintScoreConverter.toDto(constraintScoreRepository.findAll());
    }

    public ConstraintScoreDto findById(Long id) {
        return constraintScoreConverter.toDto(constraintScoreRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ConstraintScore not found")));
    }

    public ConstraintScoreDto create(ConstraintScoreDto constraintScoreDto) {
        ConstraintScore constraintScore = constraintScoreConverter.toModel(constraintScoreDto);
        return constraintScoreConverter.toDto(constraintScoreRepository.save(constraintScore));
    }

    public ConstraintScoreDto update(ConstraintScoreDto constraintScoreDto, Long id) {
        constraintScoreRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ConstraintScore not found"));

        ConstraintScore constraintScore = constraintScoreConverter.toModel(constraintScoreDto);
        return constraintScoreConverter.toDto(constraintScoreRepository.save(constraintScore));
    }

    public void delete(Long id) {
        ConstraintScore constraintScore = constraintScoreRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ConstraintScore not found"));

        constraintScoreRepository.delete(constraintScore);
    }

    public void deleteAll() {
        constraintScoreRepository.deleteAll();
    }
}
