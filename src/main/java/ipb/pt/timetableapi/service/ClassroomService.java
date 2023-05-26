package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;

    public List<Classroom> findAll(){
        return classroomRepository.findAll();
    }

    public Optional<Classroom> findById(Long id){
        return classroomRepository.findById(id);
    }

    public Classroom create(Classroom classroom){
        return classroomRepository.save(classroom);
    }

    public Classroom update(Classroom classroom){
        return classroomRepository.save(classroom);
    }

    public void delete(Classroom classroom){
        classroomRepository.delete(classroom);
    }
}