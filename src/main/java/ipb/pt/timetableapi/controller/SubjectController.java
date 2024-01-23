package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.SubjectDto;
import ipb.pt.timetableapi.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(subjectService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(subjectService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody SubjectDto subjectDto) {
        return ResponseEntity.ok().body(subjectService.create(subjectDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody SubjectDto subjectDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(subjectService.update(subjectDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        subjectService.findById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        subjectService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<SubjectDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/subjects", SubjectDto[].class);

        SubjectDto[] subjectDtos = responseEntity.getBody();
        if (subjectDtos == null) {
            return ResponseEntity.notFound().build();
        }

        subjectService.saveAll(Arrays.asList(subjectDtos));
        return ResponseEntity.ok().build();
    }
}
