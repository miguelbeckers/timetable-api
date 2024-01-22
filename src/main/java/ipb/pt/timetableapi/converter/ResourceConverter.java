package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.ResourceDto;
import ipb.pt.timetableapi.model.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceConverter {
    public ResourceDto toDto(Resource resource) {
        ResourceDto resourceDto = new ResourceDto();
        BeanUtils.copyProperties(resource, resourceDto);
        return resourceDto;
    }

    public List<ResourceDto> toDto(List<Resource> resources) {
        List<ResourceDto> resourceDtos = new ArrayList<>();
        for (Resource resource : resources) {
            resourceDtos.add(toDto(resource));
        }

        return resourceDtos;
    }

    public Resource toModel(ResourceDto resourceDto) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceDto, resource);
        return resource;
    }

    public List<Resource> toModel(List<ResourceDto> resourceDtos) {
        List<Resource> resources = new ArrayList<>();
        for (ResourceDto resourceDto : resourceDtos) {
            resources.add(toModel(resourceDto));
        }

        return resources;
    }
}
