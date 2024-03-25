package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.LessonConverter;
import ipb.pt.timetableapi.dto.LessonDto;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.repository.LessonRepository;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonConverter lessonConverter;
    private final LessonUnitRepository lessonUnitRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository,
                         LessonConverter lessonConverter,
                         LessonUnitRepository lessonUnitRepository
    ) {
        this.lessonRepository = lessonRepository;
        this.lessonConverter = lessonConverter;
        this.lessonUnitRepository = lessonUnitRepository;
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

    public List<LessonDto> enableLessonsWithTimeslotAndClassroom() {
        List<Lesson> enabledLessons = new ArrayList<>();
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();
        HashMap<Lesson, List<LessonUnit>> lessonUnitsMap = new HashMap<>();

        for (LessonUnit lessonUnit : lessonUnits) {
            Lesson lesson = lessonUnit.getLesson();
            lessonUnitsMap.computeIfAbsent(lesson, k -> new ArrayList<>()).add(lessonUnit);
        }

        for (Map.Entry<Lesson, List<LessonUnit>> entry : lessonUnitsMap.entrySet()) {
            List<LessonUnit> lessonUnitsWithSameLesson = entry.getValue();
            Lesson lesson = entry.getKey();

            boolean hasTimeslotAndClassroom = lessonUnitsWithSameLesson.stream()
                    .allMatch(lessonUnit -> lessonUnit.getTimeslot() != null && lessonUnit.getClassroom() != null);

            if (hasTimeslotAndClassroom) {
                lesson.setIsEnabled(true);
                enabledLessons.add(lesson);
            }
        }

        return lessonConverter.toDto(lessonRepository.saveAll(enabledLessons));
    }

    public List<LessonDto> enableAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        lessons.forEach(lesson -> lesson.setIsEnabled(true));
        return lessonConverter.toDto(lessonRepository.saveAll(lessons));
    }

    public List<LessonDto> disableAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        lessons.forEach(lesson -> lesson.setIsEnabled(false));
        return lessonConverter.toDto(lessonRepository.saveAll(lessons));
    }

    public LessonDto enableLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        lesson.setIsEnabled(true);
        return lessonConverter.toDto(lessonRepository.save(lesson));
    }

    public LessonDto disableLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        lesson.setIsEnabled(false);
        return lessonConverter.toDto(lessonRepository.save(lesson));
    }
}
