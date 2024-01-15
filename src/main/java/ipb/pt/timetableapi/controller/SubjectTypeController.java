package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.SubjectTypeDto;
import ipb.pt.timetableapi.model.SubjectType;
import ipb.pt.timetableapi.service.SubjectTypeService;
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
@RequestMapping("/subject-types")
public class SubjectTypeController {
    @Autowired
    private SubjectTypeService subjectTypeService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello subjectTypes!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(subjectTypeService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<SubjectType> optional = subjectTypeService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody SubjectTypeDto subjectTypeDto) {
        SubjectType subjectType = new SubjectType();
        BeanUtils.copyProperties(subjectTypeDto, subjectType);
        return ResponseEntity.ok().body(subjectTypeService.create(subjectType));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<SubjectTypeDto> subjectTypeDtoList) {

        List<Object> created = new ArrayList<>();
        for (SubjectTypeDto subjectTypeDto : subjectTypeDtoList) {
            SubjectType subjectType = new SubjectType();
            BeanUtils.copyProperties(subjectTypeDto, subjectType);
            created.add(subjectTypeService.create(subjectType));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody SubjectTypeDto subjectTypeDto, @PathVariable Long id) {
        Optional<SubjectType> optional = subjectTypeService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SubjectType subjectType = optional.get();
        BeanUtils.copyProperties(subjectTypeDto, subjectType);
        return ResponseEntity.ok().body(subjectTypeService.update(subjectType));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<SubjectType> areaOptional = subjectTypeService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        subjectTypeService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
