package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.SubjectCourseDto;
import ipb.pt.timetableapi.service.SubjectCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/subject-courses")
public class SubjectCourseController {
    @Autowired
    private SubjectCourseService subjectCourseService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

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

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        subjectCourseService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<SubjectCourseDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/subject-courses", SubjectCourseDto[].class);

        SubjectCourseDto[] subjectCourseDtos = responseEntity.getBody();
        if (subjectCourseDtos == null) {
            return ResponseEntity.notFound().build();
        }

        subjectCourseService.createMany(Arrays.asList(subjectCourseDtos));
        return ResponseEntity.ok().build();
    }
}
