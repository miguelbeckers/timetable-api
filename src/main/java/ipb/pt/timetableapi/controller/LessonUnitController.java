package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.service.LessonUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/lesson-units")
public class LessonUnitController {
    @Autowired
    private LessonUnitService lessonUnitService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonUnitService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lessonUnitService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody LessonUnitDto lessonUnitDto) {
        return ResponseEntity.ok().body(lessonUnitService.create(lessonUnitDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody LessonUnitDto lessonUnitDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(lessonUnitService.update(lessonUnitDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        lessonUnitService.findById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        lessonUnitService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<LessonUnitDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/lesson-units", LessonUnitDto[].class);

        LessonUnitDto[] lessonUnitDtos = responseEntity.getBody();
        if (lessonUnitDtos == null) {
            return ResponseEntity.notFound().build();
        }

        lessonUnitService.createMany(Arrays.asList(lessonUnitDtos));
        return ResponseEntity.ok().build();
    }
}
