package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.ClassroomType;
import ipb.pt.timetableapi.repository.ClassroomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomTypeService {
    @Autowired
    private ClassroomTypeRepository classroomTypeRepository;

    public List<ClassroomType> findAll(){
        return classroomTypeRepository.findAll();
    }

    public Optional<ClassroomType> findById(Long id){
        return classroomTypeRepository.findById(id);
    }

    public ClassroomType create(ClassroomType classroomType){
        return classroomTypeRepository.save(classroomType);
    }

    public ClassroomType update(ClassroomType classroomType){
        return classroomTypeRepository.save(classroomType);
    }

    public void delete(ClassroomType classroomType){
        classroomTypeRepository.delete(classroomType);
    }
}
