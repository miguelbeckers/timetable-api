package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.ClassroomDto;
import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.ClassroomResource;
import ipb.pt.timetableapi.model.ClassroomType;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.repository.ClassroomResourceRepository;
import ipb.pt.timetableapi.repository.ClassroomTypeRepository;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class ClassroomConverter {
    private final HashMap<Long, Timeslot> unavailabilityMap = new HashMap<>();
    private final HashMap<Long, ClassroomResource> classroomResourceMap = new HashMap<>();
    private final HashMap<Long, ClassroomType> classroomTypeMap = new HashMap<>();

    @Autowired
    public ClassroomConverter(
            TimeslotRepository timeslotRepository,
            ClassroomResourceRepository classroomResourceRepository,
            ClassroomTypeRepository classroomTypeRepository) {

        for (Timeslot timeslot : timeslotRepository.findAll()) {
            unavailabilityMap.put(timeslot.getId(), timeslot);
        }

        for (ClassroomResource classroomResource : classroomResourceRepository.findAll()) {
            classroomResourceMap.put(classroomResource.getId(), classroomResource);
        }

        for (ClassroomType classroomType : classroomTypeRepository.findAll()) {
            classroomTypeMap.put(classroomType.getId(), classroomType);
        }
    }

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
                        .map(unavailabilityMap::get)
                        .map(Optional::ofNullable)
                        .map(optionalTimeslot -> optionalTimeslot.orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found")))
                        .toList()
        );

        classroom.setClassroomResources(
                classroomDto.getClassroomResourceIds().stream()
                        .map(classroomResourceMap::get)
                        .map(Optional::ofNullable)
                        .map(optionalResource -> optionalResource.orElseThrow(
                                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomResource not found")))
                        .toList()
        );

        classroom.setClassroomType(Optional.ofNullable(classroomTypeMap.get(classroomDto.getClassroomTypeId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomType not found"))
        );

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
