package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.SubjectTypeDto;
import ipb.pt.timetableapi.model.SubjectType;
import ipb.pt.timetableapi.repository.SubjectTypeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubjectTypeService {
    @Autowired
    private SubjectTypeRepository subjectTypeRepository;

    public List<SubjectType> findAll() {
        return subjectTypeRepository.findAll();
    }

    public SubjectType findById(Long id) {
        return subjectTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectType not found"));
    }

    public SubjectType create(SubjectTypeDto subjectTypeDto) {
        SubjectType subjectType = new SubjectType();
        BeanUtils.copyProperties(subjectTypeDto, subjectType);
        return subjectTypeRepository.save(subjectType);
    }

    public SubjectType update(SubjectTypeDto subjectTypeDto, Long id) {
        SubjectType subjectType = subjectTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectType not found"));

        BeanUtils.copyProperties(subjectTypeDto, subjectType);
        return subjectTypeRepository.save(subjectType);
    }

    public void delete(Long id) {
        SubjectType subjectType = subjectTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectType not found"));

        subjectTypeRepository.delete(subjectType);
    }
}

