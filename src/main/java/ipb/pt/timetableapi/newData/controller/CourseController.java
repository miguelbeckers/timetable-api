package ipb.pt.timetableapi.newData.controller;

import ipb.pt.timetableapi.newData.dto.CourseDto;
import ipb.pt.timetableapi.newData.model.Course;
import ipb.pt.timetableapi.newData.service.CourseService;
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
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello courses!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Course> optional = courseService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody CourseDto courseDto) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        return ResponseEntity.ok().body(courseService.create(course));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<CourseDto> courseDtoList) {

        List<Object> created = new ArrayList<>();
        for (CourseDto courseDto : courseDtoList) {
            Course course = new Course();
            BeanUtils.copyProperties(courseDto, course);
            created.add(courseService.create(course));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody CourseDto courseDto, @PathVariable Long id) {
        Optional<Course> optional = courseService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = optional.get();
        BeanUtils.copyProperties(courseDto, course);
        return ResponseEntity.ok().body(courseService.update(course));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Course> areaOptional = courseService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        courseService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
