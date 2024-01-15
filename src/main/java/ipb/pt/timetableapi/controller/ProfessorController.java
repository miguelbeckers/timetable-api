package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.Professor;
import ipb.pt.timetableapi.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/professors")
public class ProfessorController {
    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(professorService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Professor> optional = Optional.ofNullable(professorService.findById(id));
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Professor professor) {
        return ResponseEntity.ok(professorService.create(professor));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Professor professor) {
        return ResponseEntity.ok(professorService.update(professor));
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Professor professor) {
        professorService.delete(professor);
        return ResponseEntity.ok().build();
    }
}
