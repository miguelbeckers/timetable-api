package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.PeriodDto;
import ipb.pt.timetableapi.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/periods")
public class PeriodController {
    @Autowired
    private PeriodService periodService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(periodService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(periodService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody PeriodDto periodDto) {
        return ResponseEntity.ok().body(periodService.create(periodDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody PeriodDto periodDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(periodService.update(periodDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        periodService.findById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        periodService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<PeriodDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/periods", PeriodDto[].class);

        PeriodDto[] periodDtos = responseEntity.getBody();
        if (periodDtos == null) {
            return ResponseEntity.notFound().build();
        }

        periodService.saveAll(Arrays.asList(periodDtos));
        return ResponseEntity.ok().build();
    }
}
