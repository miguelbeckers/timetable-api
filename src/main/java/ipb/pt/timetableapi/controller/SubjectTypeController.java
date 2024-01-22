package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.SubjectTypeDto;
import ipb.pt.timetableapi.service.SubjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@CrossOrigin
@RequestMapping("/subject-types")
public class SubjectTypeController {
    @Autowired
    private SubjectTypeService subjectTypeService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(subjectTypeService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(subjectTypeService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody SubjectTypeDto subjectTypeDto) {
        return ResponseEntity.ok().body(subjectTypeService.create(subjectTypeDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody SubjectTypeDto subjectTypeDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(subjectTypeService.update(subjectTypeDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        subjectTypeService.findById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        subjectTypeService.deleteAll();
        return ResponseEntity.ok().build();
    }
}
