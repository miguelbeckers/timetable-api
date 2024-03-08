package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.LessonConverter;
import ipb.pt.timetableapi.dto.LessonDto;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonConverter lessonConverter;

    @Autowired
    public LessonService(LessonRepository lessonRepository, LessonConverter lessonConverter) {
        this.lessonRepository = lessonRepository;
        this.lessonConverter = lessonConverter;
    }

    public List<LessonDto> findAll() {
        return lessonConverter.toDto(lessonRepository.findAll());
    }

    public LessonDto findById(Long id) {
        return lessonConverter.toDto(lessonRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found")));
    }

    public LessonDto create(LessonDto lessonDto) {
        Lesson lesson = lessonConverter.toModel(lessonDto);
        return lessonConverter.toDto(lessonRepository.save(lesson));
    }

    public LessonDto update(LessonDto lessonDto, Long id) {
        lessonRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        Lesson lesson = lessonConverter.toModel(lessonDto);
        return lessonConverter.toDto(lessonRepository.save(lesson));
    }

    public void delete(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        lessonRepository.delete(lesson);
    }

    public void deleteAll() {
        lessonRepository.deleteAll();
    }

    public List<LessonDto> saveAll(List<LessonDto> lessonDtos) {
        List<Lesson> lessons = lessonConverter.toModel(lessonDtos);
        return lessonConverter.toDto(lessonRepository.saveAll(lessons));
    }
}
