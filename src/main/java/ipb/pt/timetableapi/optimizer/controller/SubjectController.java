package ipb.pt.timetableapi.optimizer.controller;

import ipb.pt.timetableapi.optimizer.dto.SubjectDto;
import ipb.pt.timetableapi.optimizer.model.Subject;
import ipb.pt.timetableapi.optimizer.service.SubjectService;
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
@RequestMapping("/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello subjects!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(subjectService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Subject> optional = subjectService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody SubjectDto subjectDto) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectDto, subject);
        return ResponseEntity.ok().body(subjectService.create(subject));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<SubjectDto> subjectDtoList) {

        List<Object> created = new ArrayList<>();
        for (SubjectDto subjectDto : subjectDtoList) {
            Subject subject = new Subject();
            BeanUtils.copyProperties(subjectDto, subject);
            created.add(subjectService.create(subject));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody SubjectDto subjectDto, @PathVariable Long id) {
        Optional<Subject> optional = subjectService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Subject subject = optional.get();
        BeanUtils.copyProperties(subjectDto, subject);
        return ResponseEntity.ok().body(subjectService.update(subject));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Subject> areaOptional = subjectService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        subjectService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
