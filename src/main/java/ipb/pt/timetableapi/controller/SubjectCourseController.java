package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.SubjectCourseDto;
import ipb.pt.timetableapi.model.SubjectCourse;
import ipb.pt.timetableapi.service.SubjectCourseService;
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
@RequestMapping("/subject-courses")
public class SubjectCourseController {
    @Autowired
    private SubjectCourseService subjectCourseService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello subjectCourses!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(subjectCourseService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<SubjectCourse> optional = subjectCourseService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody SubjectCourseDto subjectCourseDto) {
        SubjectCourse subjectCourse = new SubjectCourse();
        BeanUtils.copyProperties(subjectCourseDto, subjectCourse);
        return ResponseEntity.ok().body(subjectCourseService.create(subjectCourse));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<SubjectCourseDto> subjectCourseDtoList) {

        List<Object> created = new ArrayList<>();
        for (SubjectCourseDto subjectCourseDto : subjectCourseDtoList) {
            SubjectCourse subjectCourse = new SubjectCourse();
            BeanUtils.copyProperties(subjectCourseDto, subjectCourse);
            created.add(subjectCourseService.create(subjectCourse));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody SubjectCourseDto subjectCourseDto, @PathVariable Long id) {
        Optional<SubjectCourse> optional = subjectCourseService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SubjectCourse subjectCourse = optional.get();
        BeanUtils.copyProperties(subjectCourseDto, subjectCourse);
        return ResponseEntity.ok().body(subjectCourseService.update(subjectCourse));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<SubjectCourse> areaOptional = subjectCourseService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        subjectCourseService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
