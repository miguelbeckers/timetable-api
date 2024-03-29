package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.StudentDto;
import ipb.pt.timetableapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

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
}
