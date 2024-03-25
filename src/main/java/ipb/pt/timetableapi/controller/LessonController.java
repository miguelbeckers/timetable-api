package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.LessonDto;
import ipb.pt.timetableapi.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lessonService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok().body(lessonService.create(lessonDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody LessonDto lessonDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(lessonService.update(lessonDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        lessonService.findById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("enable-with-timeslot-and-classroom")
    public ResponseEntity<Object> enableWithTimeslotAndClassroom() {
        return ResponseEntity.ok().body(lessonService.enableLessonsWithTimeslotAndClassroom());
    }

    @PutMapping("enable-all")
    public ResponseEntity<Object> enableAll() {
        return ResponseEntity.ok().body(lessonService.enableAllLessons());
    }

    @PutMapping("enable/{id}")
    public ResponseEntity<Object> enable(@PathVariable Long id) {
        return ResponseEntity.ok().body(lessonService.enableLesson(id));
    }

    @PutMapping("disable-all")
    public ResponseEntity<Object> disableAll() {
        return ResponseEntity.ok().body(lessonService.disableAllLessons());
    }

    @PutMapping("disable/{id}")
    public ResponseEntity<Object> disable(@PathVariable Long id) {
        return ResponseEntity.ok().body(lessonService.disableLesson(id));
    }
}