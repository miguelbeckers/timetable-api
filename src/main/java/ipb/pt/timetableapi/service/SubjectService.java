package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.SubjectDto;
import ipb.pt.timetableapi.model.Subject;
import ipb.pt.timetableapi.repository.SubjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject findById(Long id) {
        return subjectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));
    }

    public Subject create(SubjectDto subjectDto) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectDto, subject);
        return subjectRepository.save(subject);
    }

    public Subject update(SubjectDto subjectDto, Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        BeanUtils.copyProperties(subjectDto, subject);
        return subjectRepository.save(subject);
    }

    public void delete(Long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not found"));

        subjectRepository.delete(subject);
    }
}

