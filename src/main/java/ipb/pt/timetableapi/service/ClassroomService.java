package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.ClassroomConverter;
import ipb.pt.timetableapi.dto.ClassroomDto;
import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassroomConverter classroomConverter;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, ClassroomConverter classroomConverter) {
        this.classroomRepository = classroomRepository;
        this.classroomConverter = classroomConverter;
    }

    public List<ClassroomDto> findAll() {
        return classroomConverter.toDto(classroomRepository.findAll());
    }

    public ClassroomDto findById(Long id) {
        return classroomConverter.toDto(classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found")));
    }

    public ClassroomDto create(ClassroomDto classroomDto) {
        Classroom classroom = classroomConverter.toModel(classroomDto);
        return classroomConverter.toDto(classroomRepository.save(classroom));
    }

    public ClassroomDto update(ClassroomDto classroomDto, Long id) {
        classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        Classroom classroom = classroomConverter.toModel(classroomDto);
        return classroomConverter.toDto(classroomRepository.save(classroom));
    }

    public void delete(Long id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        classroomRepository.delete(classroom);
    }

    public void deleteAll() {
        classroomRepository.deleteAll();
    }

    public List<ClassroomDto> saveAll(List<ClassroomDto> classroomDtos) {
        List<Classroom> classrooms = classroomConverter.toModel(classroomDtos);
        return classroomConverter.toDto(classroomRepository.saveAll(classrooms));
    }
}
