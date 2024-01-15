package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.ClassroomResource;
import ipb.pt.timetableapi.repository.ClassroomResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomResourceService {
    @Autowired
    private ClassroomResourceRepository classroomResourceRepository;

    public List<ClassroomResource> findAll(){
        return classroomResourceRepository.findAll();
    }

    public Optional<ClassroomResource> findById(Long id){
        return classroomResourceRepository.findById(id);
    }

    public ClassroomResource create(ClassroomResource classroomResource){
        return classroomResourceRepository.save(classroomResource);
    }

    public ClassroomResource update(ClassroomResource classroomResource){
        return classroomResourceRepository.save(classroomResource);
    }

    public void delete(ClassroomResource classroomResource){
        classroomResourceRepository.delete(classroomResource);
    }
}
