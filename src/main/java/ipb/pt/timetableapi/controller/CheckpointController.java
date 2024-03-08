package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.CheckpointDto;
import ipb.pt.timetableapi.service.CheckpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/checkpoint")
public class CheckpointController {
    private final CheckpointService checkpointService;

    @Autowired
    public CheckpointController(CheckpointService checkpointService) {
        this.checkpointService = checkpointService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(checkpointService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(checkpointService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody CheckpointDto checkpointDto) {
        return ResponseEntity.ok().body(checkpointService.create(checkpointDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody CheckpointDto checkpointDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(checkpointService.update(checkpointDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        checkpointService.findById(id);
        return ResponseEntity.ok().build();
    }
}
