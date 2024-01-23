package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.service.LessonUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/lesson-units")
public class LessonUnitController {
    private final LessonUnitService lessonUnitService;

    @Autowired
    public LessonUnitController(LessonUnitService lessonUnitService) {
        this.lessonUnitService = lessonUnitService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonUnitService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lessonUnitService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody LessonUnitDto lessonUnitDto) {
        return ResponseEntity.ok().body(lessonUnitService.create(lessonUnitDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody LessonUnitDto lessonUnitDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(lessonUnitService.update(lessonUnitDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        lessonUnitService.findById(id);
        return ResponseEntity.ok().build();
    }
}
