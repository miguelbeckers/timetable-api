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

    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    public Classroom findById(Long id) {
        return classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));
    }

    public Classroom create(ClassroomDto classroomDto) {
        Classroom classroom = classroomConverter.toModel(classroomDto);
        return classroomRepository.save(classroom);
    }

    public Classroom update(ClassroomDto classroomDto, Long id) {
        classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        Classroom classroom = classroomConverter.toModel(classroomDto);
        return classroomRepository.save(classroom);
    }

    public void delete(Long id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        classroomRepository.delete(classroom);
    }

    public void deleteAll() {
        classroomRepository.deleteAll();
    }

    public void saveAll(List<ClassroomDto> classroomDtos) {
        List<Classroom> classrooms = classroomConverter.toModel(classroomDtos);
        classroomRepository.saveAll(classrooms);
    }
}
