package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.ClassroomTypeConverter;
import ipb.pt.timetableapi.dto.ClassroomTypeDto;
import ipb.pt.timetableapi.model.ClassroomType;
import ipb.pt.timetableapi.repository.ClassroomTypeRepository;
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

    public List<ClassroomTypeDto> findAll() {
        return classroomTypeConverter.toDto(classroomTypeRepository.findAll());
    }

    public ClassroomTypeDto findById(Long id) {
        return classroomTypeConverter.toDto(classroomTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomType not found")));
    }

    public ClassroomTypeDto create(ClassroomTypeDto classroomTypeDto) {
        ClassroomType classroomType = classroomTypeConverter.toModel(classroomTypeDto);
        return classroomTypeConverter.toDto(classroomTypeRepository.save(classroomType));
    }

    public ClassroomTypeDto update(ClassroomTypeDto classroomTypeDto, Long id) {
        classroomTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomType not found"));

        ClassroomType classroomType = classroomTypeConverter.toModel(classroomTypeDto);
        return classroomTypeConverter.toDto(classroomTypeRepository.save(classroomType));
    }

    public void delete(Long id) {
        ClassroomType classroomType = classroomTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomType not found"));

        classroomTypeRepository.delete(classroomType);
    }

    public void deleteAll() {
        classroomTypeRepository.deleteAll();
    }

    public List<ClassroomTypeDto> saveAll(List<ClassroomTypeDto> classroomTypeDtos) {
        List<ClassroomType> classroomTypes = classroomTypeConverter.toModel(classroomTypeDtos);
        return classroomTypeConverter.toDto(classroomTypeRepository.saveAll(classroomTypes));
    }
}

