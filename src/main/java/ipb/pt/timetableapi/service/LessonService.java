package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.LessonConverter;
import ipb.pt.timetableapi.dto.LessonDto;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.repository.LessonRepository;
import org.springframework.beans.BeanUtils;
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

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Lesson findById(Long id) {
        return lessonRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));
    }

    public Lesson create(LessonDto lessonDto) {
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(lessonDto, lesson);
        return lessonRepository.save(lesson);
    }

    public Lesson update(LessonDto lessonDto, Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        BeanUtils.copyProperties(lessonDto, lesson);
        return lessonRepository.save(lesson);
    }

    public void delete(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        lessonRepository.delete(lesson);
    }

    public void deleteAll() {
        lessonRepository.deleteAll();
    }

    public void saveAll(List<LessonDto> lessonDtos) {
        List<Lesson> lessons = lessonConverter.toModel(lessonDtos);
        lessonRepository.saveAll(lessons);
    }
}
