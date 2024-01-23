package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ProfessorDto;
import ipb.pt.timetableapi.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/professors")
public class ProfessorController {
    @Autowired
    private ProfessorService professorService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

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

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        professorService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<ProfessorDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/professors", ProfessorDto[].class);

        ProfessorDto[] professorDtos = responseEntity.getBody();
        if (professorDtos == null) {
            return ResponseEntity.notFound().build();
        }

        professorService.saveAll(Arrays.asList(professorDtos));
        return ResponseEntity.ok().build();
    }
}
