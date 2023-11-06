package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.Professor;
import ipb.pt.timetableapi.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/professors")
public class ProfessorController {
    @Autowired
    private ProfessorService professorService;

    //findAll
    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(professorService.findAll());
    }

    //findById
    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Professor> optional = Optional.ofNullable(professorService.findById(id));
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //create

    //update
    //delete

}
