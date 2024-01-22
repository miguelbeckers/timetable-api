package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.StudentDto;
import ipb.pt.timetableapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody StudentDto studentDto) {
        return ResponseEntity.ok().body(studentService.create(studentDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody StudentDto studentDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(studentService.update(studentDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        studentService.findById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        studentService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<StudentDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/students", StudentDto[].class);

        StudentDto[] studentDtos = responseEntity.getBody();
        if (studentDtos == null) {
            return ResponseEntity.notFound().build();
        }

        studentService.createMany(Arrays.asList(studentDtos));
        return ResponseEntity.ok().build();
    }
}
