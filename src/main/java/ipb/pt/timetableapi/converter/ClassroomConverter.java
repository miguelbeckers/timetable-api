package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.ClassroomDto;
import ipb.pt.timetableapi.model.Classroom;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClassroomConverter {
    public ClassroomDto toDto(Classroom classroom) {
        ClassroomDto classroomDto = new ClassroomDto();
        BeanUtils.copyProperties(classroom, classroomDto);
        return classroomDto;
    }

    public List<ClassroomDto> toDto(List<Classroom> classrooms) {
        List<ClassroomDto> classroomDtos = new ArrayList<>();
        for (Classroom classroom : classrooms) {
            classroomDtos.add(toDto(classroom));
        }

        return classroomDtos;
    }

    public Classroom toModel(ClassroomDto classroomDto) {
        Classroom classroom = new Classroom();
        BeanUtils.copyProperties(classroomDto, classroom);
        return classroom;
    }

    public List<Classroom> toModel(List<ClassroomDto> classroomDtos) {
        List<Classroom> classrooms = new ArrayList<>();
        for (ClassroomDto classroomDto : classroomDtos) {
            classrooms.add(toModel(classroomDto));
        }

        return classrooms;
    }
}
