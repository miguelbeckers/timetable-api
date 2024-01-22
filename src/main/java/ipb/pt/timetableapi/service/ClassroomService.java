package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.ClassroomDto;
import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.repository.ClassroomRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClassroomService {
    @Autowired
    private ClassroomRepository classroomRepository;

    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    public Classroom findById(Long id) {
        return classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));
    }

    public Classroom create(ClassroomDto classroomDto) {
        Classroom classroom = new Classroom();
        BeanUtils.copyProperties(classroomDto, classroom);
        return classroomRepository.save(classroom);
    }

    public Classroom update(ClassroomDto classroomDto, Long id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        BeanUtils.copyProperties(classroomDto, classroom);
        return classroomRepository.save(classroom);
    }

    public void delete(Long id) {
        Classroom classroom = classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        classroomRepository.delete(classroom);
    }

    public void createMany(List<ClassroomDto> classroomDtos) {
        for (ClassroomDto classroomDto : classroomDtos) {
            Classroom classroom = new Classroom();
            BeanUtils.copyProperties(classroomDto, classroom);
            classroomRepository.save(classroom);
        }
    }
}
