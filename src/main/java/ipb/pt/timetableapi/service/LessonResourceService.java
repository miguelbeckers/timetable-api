package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.LessonResourceConverter;
import ipb.pt.timetableapi.dto.LessonResourceDto;
import ipb.pt.timetableapi.model.LessonResource;
import ipb.pt.timetableapi.repository.LessonResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LessonResourceService {
    private final LessonResourceRepository lessonResourceRepository;
    private final LessonResourceConverter lessonResourceConverter;

    @Autowired
    public LessonResourceService(LessonResourceRepository lessonResourceRepository, LessonResourceConverter lessonResourceConverter) {
        this.lessonResourceRepository = lessonResourceRepository;
        this.lessonResourceConverter = lessonResourceConverter;
    }

    public List<LessonResourceDto> findAll() {
        return lessonResourceConverter.toDto(lessonResourceRepository.findAll());
    }

    public LessonResourceDto findById(Long id) {
        return lessonResourceConverter.toDto(lessonResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonResource not found")));
    }

    public LessonResourceDto create(LessonResourceDto lessonResourceDto) {
        LessonResource lessonResource = lessonResourceConverter.toModel(lessonResourceDto);
        return lessonResourceConverter.toDto(lessonResourceRepository.save(lessonResource));
    }

    public LessonResourceDto update(LessonResourceDto lessonResourceDto, Long id) {
        lessonResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonResource not found"));

        LessonResource lessonResource = lessonResourceConverter.toModel(lessonResourceDto);
        return lessonResourceConverter.toDto(lessonResourceRepository.save(lessonResource));
    }

    public void delete(Long id) {
        LessonResource lessonResource = lessonResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonResource not found"));

        lessonResourceRepository.delete(lessonResource);
    }

    public void deleteAll() {
        lessonResourceRepository.deleteAll();
    }

    public List<LessonResourceDto> saveAll(List<LessonResourceDto> lessonResourceDtos) {
        List<LessonResource> lessonResources = lessonResourceConverter.toModel(lessonResourceDtos);
        return lessonResourceConverter.toDto(lessonResourceRepository.saveAll(lessonResources));
    }
}
