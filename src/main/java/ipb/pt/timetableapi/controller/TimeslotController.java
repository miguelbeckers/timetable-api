package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.TimeslotDto;
import ipb.pt.timetableapi.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/timeslots")
public class TimeslotController {
    private final TimeslotService timeslotService;

    @Autowired
    public TimeslotController(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(timeslotService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(timeslotService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody TimeslotDto timeslotDto) {
        return ResponseEntity.ok().body(timeslotService.create(timeslotDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody TimeslotDto timeslotDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(timeslotService.update(timeslotDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        timeslotService.findById(id);
        return ResponseEntity.ok().build();
    }
}