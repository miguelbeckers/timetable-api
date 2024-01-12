package ipb.pt.timetableapi.optimizer.service;

import ipb.pt.timetableapi.optimizer.model.Subject;
import ipb.pt.timetableapi.optimizer.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> findAll(){
        return subjectRepository.findAll();
    }

    public Optional<Subject> findById(Long id){
        return subjectRepository.findById(id);
    }

    public Subject create(Subject subject){
        return subjectRepository.save(subject);
    }

    public Subject update(Subject subject){
        return subjectRepository.save(subject);
    }

    public void delete(Subject subject){
        subjectRepository.delete(subject);
    }
}
