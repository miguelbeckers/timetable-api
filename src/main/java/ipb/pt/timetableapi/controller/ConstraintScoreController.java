package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ConstraintScoreDto;
import ipb.pt.timetableapi.service.ConstraintScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/constraint-score")
public class ConstraintScoreController {
    private final ConstraintScoreService constraintScoreService;

    @Autowired
    public ConstraintScoreController(ConstraintScoreService constraintScoreService) {
        this.constraintScoreService = constraintScoreService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(constraintScoreService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(constraintScoreService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody ConstraintScoreDto constraintScoreDto) {
        return ResponseEntity.ok().body(constraintScoreService.create(constraintScoreDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody ConstraintScoreDto constraintScoreDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(constraintScoreService.update(constraintScoreDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        constraintScoreService.findById(id);
        return ResponseEntity.ok().build();
    }
}
