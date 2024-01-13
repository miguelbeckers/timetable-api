package ipb.pt.timetableapi.newData.service;

import ipb.pt.timetableapi.newData.model.SubjectType;
import ipb.pt.timetableapi.newData.repository.SubjectTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectTypeService {
    @Autowired
    private SubjectTypeRepository subjectTypeRepository;

    public List<SubjectType> findAll(){
        return subjectTypeRepository.findAll();
    }

    public Optional<SubjectType> findById(Long id){
        return subjectTypeRepository.findById(id);
    }

    public SubjectType create(SubjectType subjectType){
        return subjectTypeRepository.save(subjectType);
    }

    public SubjectType update(SubjectType subjectType){
        return subjectTypeRepository.save(subjectType);
    }

    public void delete(SubjectType subjectType){
        subjectTypeRepository.delete(subjectType);
    }
}
