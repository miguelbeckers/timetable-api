package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.CourseDto;
import ipb.pt.timetableapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(courseService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody CourseDto courseDto) {
        return ResponseEntity.ok().body(courseService.create(courseDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody CourseDto courseDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(courseService.update(courseDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        courseService.findById(id);
        return ResponseEntity.ok().build();
    }
}
