package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.SubjectTypeConverter;
import ipb.pt.timetableapi.dto.SubjectTypeDto;
import ipb.pt.timetableapi.model.SubjectType;
import ipb.pt.timetableapi.repository.SubjectTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubjectTypeService {
    private final SubjectTypeRepository subjectTypeRepository;
    private final SubjectTypeConverter subjectTypeConverter;

    @Autowired
    public SubjectTypeService(SubjectTypeRepository subjectTypeRepository, SubjectTypeConverter subjectTypeConverter) {
        this.subjectTypeRepository = subjectTypeRepository;
        this.subjectTypeConverter = subjectTypeConverter;
    }

    public List<SubjectTypeDto> findAll() {
        return subjectTypeConverter.toDto(subjectTypeRepository.findAll());
    }

    public SubjectTypeDto findById(Long id) {
        return subjectTypeConverter.toDto(subjectTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectType not found")));
    }

    public SubjectTypeDto create(SubjectTypeDto subjectTypeDto) {
        SubjectType subjectType = subjectTypeConverter.toModel(subjectTypeDto);
        return subjectTypeConverter.toDto(subjectTypeRepository.save(subjectType));
    }

    public SubjectTypeDto update(SubjectTypeDto subjectTypeDto, Long id) {
        subjectTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectType not found"));

        SubjectType subjectType = subjectTypeConverter.toModel(subjectTypeDto);
        return subjectTypeConverter.toDto(subjectTypeRepository.save(subjectType));
    }

    public void delete(Long id) {
        SubjectType subjectType = subjectTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectType not found"));

        subjectTypeRepository.delete(subjectType);
    }

    public void deleteAll() {
        subjectTypeRepository.deleteAll();
    }

    public List<SubjectTypeDto> saveAll(List<SubjectTypeDto> subjectTypeDtos) {
        List<SubjectType> subjectTypes = subjectTypeConverter.toModel(subjectTypeDtos);
        return subjectTypeConverter.toDto(subjectTypeRepository.saveAll(subjectTypes));
    }
}

