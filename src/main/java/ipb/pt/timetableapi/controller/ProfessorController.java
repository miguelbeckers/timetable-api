package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ProfessorDto;
import ipb.pt.timetableapi.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/professors")
public class ProfessorController {
    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(professorService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(professorService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody ProfessorDto professorDto) {
        return ResponseEntity.ok().body(professorService.create(professorDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody ProfessorDto professorDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(professorService.update(professorDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        professorService.findById(id);
        return ResponseEntity.ok().build();
    }
}
