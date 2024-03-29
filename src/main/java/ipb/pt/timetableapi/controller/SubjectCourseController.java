package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.SubjectCourseDto;
import ipb.pt.timetableapi.service.SubjectCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/subject-courses")
public class SubjectCourseController {
    private final SubjectCourseService subjectCourseService;

    @Autowired
    public SubjectCourseController(SubjectCourseService subjectCourseService) {
        this.subjectCourseService = subjectCourseService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(subjectCourseService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(subjectCourseService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody SubjectCourseDto subjectCourseDto) {
        return ResponseEntity.ok().body(subjectCourseService.create(subjectCourseDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody SubjectCourseDto subjectCourseDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(subjectCourseService.update(subjectCourseDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        subjectCourseService.findById(id);
        return ResponseEntity.ok().build();
    }
}
