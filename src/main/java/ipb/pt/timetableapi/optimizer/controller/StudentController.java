package ipb.pt.timetableapi.optimizer.controller;

import ipb.pt.timetableapi.optimizer.dto.StudentDto;
import ipb.pt.timetableapi.optimizer.model.Student;
import ipb.pt.timetableapi.optimizer.service.StudentService;
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
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello students!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Student> optional = studentService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody StudentDto studentDto) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);
        return ResponseEntity.ok().body(studentService.create(student));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<StudentDto> studentDtoList) {

        List<Object> created = new ArrayList<>();
        for (StudentDto studentDto : studentDtoList) {
            Student student = new Student();
            BeanUtils.copyProperties(studentDto, student);
            created.add(studentService.create(student));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody StudentDto studentDto, @PathVariable Long id) {
        Optional<Student> optional = studentService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student student = optional.get();
        BeanUtils.copyProperties(studentDto, student);
        return ResponseEntity.ok().body(studentService.update(student));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Student> areaOptional = studentService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        studentService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
