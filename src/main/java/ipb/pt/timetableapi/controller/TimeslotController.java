package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.TimeslotDto;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.service.TimeslotService;
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
@RequestMapping("/timeslots")
public class TimeslotController {

    @Autowired
    public TimeslotService timeslotService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello timeslots!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(timeslotService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Timeslot> optional = timeslotService.findById(id);
        return optional.isPresent() ? ResponseEntity.ok(optional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody TimeslotDto timeslotDto) {
        Timeslot timeSlot = new Timeslot();
        BeanUtils.copyProperties(timeslotDto, timeSlot);
        return ResponseEntity.ok().body(timeslotService.create(timeSlot));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<TimeslotDto> timeslotDtoList) {

        List<Object> created = new ArrayList<>();
        for (TimeslotDto timeslotDto : timeslotDtoList) {
            Timeslot timeslot = new Timeslot();
            BeanUtils.copyProperties(timeslotDto, timeslot);
            created.add(timeslotService.create(timeslot));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody TimeslotDto timeslotDto, @PathVariable Long id) {
        Optional<Timeslot> optional = timeslotService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Timeslot timeSlot = optional.get();
        BeanUtils.copyProperties(timeslotDto, timeSlot);
        return ResponseEntity.ok().body(timeslotService.update(timeSlot));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Timeslot> areaOptional = timeslotService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        timeslotService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}