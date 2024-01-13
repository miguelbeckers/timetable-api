package ipb.pt.timetableapi.newData.controller;

import ipb.pt.timetableapi.newData.dto.LessonStudentDto;
import ipb.pt.timetableapi.newData.model.LessonStudent;
import ipb.pt.timetableapi.newData.service.LessonStudentService;
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
@RequestMapping("/lesson-students")
public class LessonStudentController {
    @Autowired
    private LessonStudentService lessonStudentService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello lessonStudents!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonStudentService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<LessonStudent> optional = lessonStudentService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody LessonStudentDto lessonStudentDto) {
        LessonStudent lessonStudent = new LessonStudent();
        BeanUtils.copyProperties(lessonStudentDto, lessonStudent);
        return ResponseEntity.ok().body(lessonStudentService.create(lessonStudent));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<LessonStudentDto> lessonStudentDtoList) {

        List<Object> created = new ArrayList<>();
        for (LessonStudentDto lessonStudentDto : lessonStudentDtoList) {
            LessonStudent lessonStudent = new LessonStudent();
            BeanUtils.copyProperties(lessonStudentDto, lessonStudent);
            created.add(lessonStudentService.create(lessonStudent));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody LessonStudentDto lessonStudentDto, @PathVariable Long id) {
        Optional<LessonStudent> optional = lessonStudentService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LessonStudent lessonStudent = optional.get();
        BeanUtils.copyProperties(lessonStudentDto, lessonStudent);
        return ResponseEntity.ok().body(lessonStudentService.update(lessonStudent));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<LessonStudent> areaOptional = lessonStudentService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lessonStudentService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
