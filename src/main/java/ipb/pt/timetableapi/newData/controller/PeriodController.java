package ipb.pt.timetableapi.newData.controller;

import ipb.pt.timetableapi.newData.dto.PeriodDto;
import ipb.pt.timetableapi.newData.model.Period;
import ipb.pt.timetableapi.newData.service.PeriodService;
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
@RequestMapping("/periods")
public class PeriodController {
    @Autowired
    private PeriodService periodService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello periods!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(periodService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Period> optional = periodService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody PeriodDto periodDto) {
        Period period = new Period();
        BeanUtils.copyProperties(periodDto, period);
        return ResponseEntity.ok().body(periodService.create(period));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<PeriodDto> periodDtoList) {

        List<Object> created = new ArrayList<>();
        for (PeriodDto periodDto : periodDtoList) {
            Period period = new Period();
            BeanUtils.copyProperties(periodDto, period);
            created.add(periodService.create(period));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody PeriodDto periodDto, @PathVariable Long id) {
        Optional<Period> optional = periodService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Period period = optional.get();
        BeanUtils.copyProperties(periodDto, period);
        return ResponseEntity.ok().body(periodService.update(period));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Period> areaOptional = periodService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        periodService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/convert")
    public ResponseEntity<Object> convert() {
        periodService.convert();
        return ResponseEntity.ok().build();
    }
}
