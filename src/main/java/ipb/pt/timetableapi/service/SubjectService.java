package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.SubjectConverter;
import ipb.pt.timetableapi.dto.SubjectDto;
import ipb.pt.timetableapi.model.Subject;
import ipb.pt.timetableapi.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectConverter subjectConverter;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, SubjectConverter subjectConverter) {
        this.subjectRepository = subjectRepository;
        this.subjectConverter = subjectConverter;
    }

    public List<SubjectDto> findAll() {
        return subjectConverter.toDto(subjectRepository.findAll());
    }

    public SubjectDto findById(Long id) {
        return subjectConverter.toDto(subjectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found")));
    }

    public SubjectDto create(SubjectDto subjectDto) {
        Subject subject = subjectConverter.toModel(subjectDto);
        return subjectConverter.toDto(subjectRepository.save(subject));
    }

    public SubjectDto update(SubjectDto subjectDto, Long id) {
        subjectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        Subject subject = subjectConverter.toModel(subjectDto);
        return subjectConverter.toDto(subjectRepository.save(subject));
    }

    public void delete(Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        subjectRepository.delete(subject);
    }

    public void deleteAll() {
        subjectRepository.deleteAll();
    }

    public List<SubjectDto> saveAll(List<SubjectDto> subjectDtos) {
        List<Subject> subjects = subjectConverter.toModel(subjectDtos);
        return subjectConverter.toDto(subjectRepository.saveAll(subjects));
    }
}

