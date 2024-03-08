package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.LessonResourceDto;
import ipb.pt.timetableapi.service.LessonResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/lesson-resources")
public class LessonResourceController {
    private final LessonResourceService lessonResourceService;

    @Autowired
    public LessonResourceController(LessonResourceService lessonResourceService) {
        this.lessonResourceService = lessonResourceService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonResourceService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lessonResourceService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody LessonResourceDto lessonResourceDto) {
        return ResponseEntity.ok().body(lessonResourceService.create(lessonResourceDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody LessonResourceDto lessonResourceDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(lessonResourceService.update(lessonResourceDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        lessonResourceService.findById(id);
        return ResponseEntity.ok().build();
    }
}
