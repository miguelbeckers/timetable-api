package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.ClassroomDto;
import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.ClassroomResource;
import ipb.pt.timetableapi.model.ClassroomType;
import ipb.pt.timetableapi.model.Timeslot;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClassroomConverter {
    public ClassroomDto toDto(Classroom classroom) {
        ClassroomDto classroomDto = new ClassroomDto();
        BeanUtils.copyProperties(classroom, classroomDto);

        classroomDto.setUnavailabilityIds(classroom.getUnavailability().stream().map(Timeslot::getId).toList());
        classroomDto.setClassroomResourceIds(classroom.getClassroomResources().stream().map(ClassroomResource::getId).toList());
        classroomDto.setClassroomTypeId(classroom.getClassroomType().getId());

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

        classroom.setUnavailability(
                classroomDto.getUnavailabilityIds().stream()
                        .map(unavailability -> {
                            Timeslot timeslot = new Timeslot();
                            timeslot.setId(unavailability);
                            return timeslot;
                        })
                        .toList()
        );

        classroom.setClassroomResources(
                classroomDto.getClassroomResourceIds().stream()
                        .map(classroomResourceId -> {
                            ClassroomResource classroomResource = new ClassroomResource();
                            classroomResource.setId(classroomResourceId);
                            return classroomResource;
                        })
                        .toList()
        );

        ClassroomType classroomType = new ClassroomType();
        classroomType.setId(classroomDto.getClassroomTypeId());
        classroom.setClassroomType(classroomType);

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
