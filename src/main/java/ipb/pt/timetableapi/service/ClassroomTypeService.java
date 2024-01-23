package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.ClassroomTypeConverter;
import ipb.pt.timetableapi.dto.ClassroomTypeDto;
import ipb.pt.timetableapi.model.ClassroomType;
import ipb.pt.timetableapi.repository.ClassroomTypeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClassroomTypeService {
    private final ClassroomTypeRepository classroomTypeRepository;
    private final ClassroomTypeConverter classroomTypeConverter;

    @Autowired
    public ClassroomTypeService(ClassroomTypeRepository classroomTypeRepository, ClassroomTypeConverter classroomTypeConverter) {
        this.classroomTypeRepository = classroomTypeRepository;
        this.classroomTypeConverter = classroomTypeConverter;
    }

    public List<ClassroomType> findAll() {
        return classroomTypeRepository.findAll();
    }

    public ClassroomType findById(Long id) {
        return classroomTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomType not found"));
    }

    public ClassroomType create(ClassroomTypeDto classroomTypeDto) {
        ClassroomType classroomType = new ClassroomType();
        BeanUtils.copyProperties(classroomTypeDto, classroomType);
        return classroomTypeRepository.save(classroomType);
    }

    public ClassroomType update(ClassroomTypeDto classroomTypeDto, Long id) {
        ClassroomType classroomType = classroomTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomType not found"));

        BeanUtils.copyProperties(classroomTypeDto, classroomType);
        return classroomTypeRepository.save(classroomType);
    }

    public void delete(Long id) {
        ClassroomType classroomType = classroomTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomType not found"));

        classroomTypeRepository.delete(classroomType);
    }

    public void deleteAll() {
        classroomTypeRepository.deleteAll();
    }

    public void createMany(List<ClassroomTypeDto> classroomTypeDtos) {
        List<ClassroomType> classroomTypes = classroomTypeConverter.toModel(classroomTypeDtos);
        classroomTypeRepository.saveAll(classroomTypes);
    }
}

