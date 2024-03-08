package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.LessonResourceDto;
import ipb.pt.timetableapi.model.LessonResource;
import ipb.pt.timetableapi.model.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LessonResourceConverter {
    public LessonResourceDto toDto(LessonResource lessonResource) {
        LessonResourceDto lessonResourceDto = new LessonResourceDto();
        BeanUtils.copyProperties(lessonResource, lessonResourceDto);
        return lessonResourceDto;
    }

    public List<LessonResourceDto> toDto(List<LessonResource> lessonResources) {
        List<LessonResourceDto> lessonResourceDtos = new ArrayList<>();
        for (LessonResource lessonResource : lessonResources) {
            lessonResourceDtos.add(toDto(lessonResource));
        }

        return lessonResourceDtos;
    }

    public LessonResource toModel(LessonResourceDto lessonResourceDto) {
        LessonResource lessonResource = new LessonResource();
        BeanUtils.copyProperties(lessonResourceDto, lessonResource);

        Resource resource = new Resource();
        resource.setId(lessonResourceDto.getResourceId());
        lessonResource.setResource(resource);

        return lessonResource;
    }

    public List<LessonResource> toModel(List<LessonResourceDto> lessonResourceDtos) {
        List<LessonResource> lessonResources = new ArrayList<>();
        for (LessonResourceDto lessonResourceDto : lessonResourceDtos) {
            lessonResources.add(toModel(lessonResourceDto));
        }

        return lessonResources;
    }
}
