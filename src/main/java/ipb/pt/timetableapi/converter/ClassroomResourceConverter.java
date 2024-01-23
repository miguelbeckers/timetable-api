package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.ClassroomResourceDto;
import ipb.pt.timetableapi.model.ClassroomResource;
import ipb.pt.timetableapi.model.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClassroomResourceConverter {
    public ClassroomResourceDto toDto(ClassroomResource classroomResource) {
        ClassroomResourceDto classroomResourceDto = new ClassroomResourceDto();
        BeanUtils.copyProperties(classroomResource, classroomResourceDto);
        return classroomResourceDto;
    }

    public List<ClassroomResourceDto> toDto(List<ClassroomResource> classroomResources) {
        List<ClassroomResourceDto> classroomResourceDtos = new ArrayList<>();
        for (ClassroomResource classroomResource : classroomResources) {
            classroomResourceDtos.add(toDto(classroomResource));
        }

        return classroomResourceDtos;
    }

    public ClassroomResource toModel(ClassroomResourceDto classroomResourceDto) {
        ClassroomResource classroomResource = new ClassroomResource();
        BeanUtils.copyProperties(classroomResourceDto, classroomResource);

        Resource resource = new Resource();
        resource.setId(classroomResourceDto.getResourceId());
        classroomResource.setResource(resource);

        return classroomResource;
    }

    public List<ClassroomResource> toModel(List<ClassroomResourceDto> classroomResourceDtos) {
        List<ClassroomResource> classroomResources = new ArrayList<>();
        for (ClassroomResourceDto classroomResourceDto : classroomResourceDtos) {
            classroomResources.add(toModel(classroomResourceDto));
        }

        return classroomResources;
    }
}
