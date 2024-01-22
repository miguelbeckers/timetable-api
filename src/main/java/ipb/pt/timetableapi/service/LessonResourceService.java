package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.LessonResourceDto;
import ipb.pt.timetableapi.model.LessonResource;
import ipb.pt.timetableapi.repository.LessonResourceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonResourceService {
    @Autowired
    private LessonResourceRepository lessonResourceRepository;

    public List<LessonResource> findAll() {
        return lessonResourceRepository.findAll();
    }

    public LessonResource findById(Long id) {
        return lessonResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonResource not found"));
    }

    public LessonResource create(LessonResourceDto lessonResourceDto) {
        LessonResource lessonResource = new LessonResource();
        BeanUtils.copyProperties(lessonResourceDto, lessonResource);
        return lessonResourceRepository.save(lessonResource);
    }

    public LessonResource update(LessonResourceDto lessonResourceDto, Long id) {
        LessonResource lessonResource = lessonResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonResource not found"));

        BeanUtils.copyProperties(lessonResourceDto, lessonResource);
        return lessonResourceRepository.save(lessonResource);
    }

    public void delete(Long id) {
        LessonResource lessonResource = lessonResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonResource not found"));

        lessonResourceRepository.delete(lessonResource);
    }

    public void deleteAll() {
        lessonResourceRepository.deleteAll();
    }

    public void createMany(List<LessonResourceDto> lessonResourceDtos) {
        List<LessonResource> lessonResources = new ArrayList<>();

        for (LessonResourceDto lessonResourceDto : lessonResourceDtos) {
            LessonResource lessonResource = new LessonResource();
            BeanUtils.copyProperties(lessonResourceDto, lessonResource);
            lessonResources.add(lessonResource);
        }

        lessonResourceRepository.saveAll(lessonResources);
    }
}
