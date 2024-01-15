package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.LessonDto;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.service.LessonService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello lessons!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Lesson> optional = lessonService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody LessonDto lessonDto) {
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(lessonDto, lesson);
        return ResponseEntity.ok().body(lessonService.create(lesson));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<LessonDto> lessonDtoList) {

        List<Object> created = new ArrayList<>();
        for (LessonDto lessonDto : lessonDtoList) {
            Lesson lesson = new Lesson();
            BeanUtils.copyProperties(lessonDto, lesson);
            created.add(lessonService.create(lesson));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody LessonDto lessonDto, @PathVariable Long id) {
        Optional<Lesson> optional = lessonService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Lesson lesson = optional.get();
        BeanUtils.copyProperties(lessonDto, lesson);
        return ResponseEntity.ok().body(lessonService.update(lesson));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Lesson> areaOptional = lessonService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lessonService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reset")
    public ResponseEntity<List<Lesson>> reset() {
        List<Lesson> lessons = lessonService.findAll();
        List<Lesson> updated = new ArrayList<>();
        for(Lesson lesson: lessons) {
            lesson.setTimeslot(null);
            lesson.setClassroom(null);
            updated.add(lessonService.update(lesson));
        }
        return ResponseEntity.ok().body(updated);
    }
}