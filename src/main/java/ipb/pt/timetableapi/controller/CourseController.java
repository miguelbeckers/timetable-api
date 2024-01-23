package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.CourseDto;
import ipb.pt.timetableapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

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

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        courseService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<CourseDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/courses", CourseDto[].class);

        CourseDto[] courseDtos = responseEntity.getBody();
        if (courseDtos == null) {
            return ResponseEntity.notFound().build();
        }

        courseService.saveAll(Arrays.asList(courseDtos));
        return ResponseEntity.ok().build();
    }
}
