package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.LessonDto;
import ipb.pt.timetableapi.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/lessons")
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(lessonService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok().body(lessonService.create(lessonDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody LessonDto lessonDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(lessonService.update(lessonDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        lessonService.findById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        lessonService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<LessonDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/lessons", LessonDto[].class);

        LessonDto[] lessonDtos = responseEntity.getBody();
        if (lessonDtos == null) {
            return ResponseEntity.notFound().build();
        }

        lessonService.saveAll(Arrays.asList(lessonDtos));
        return ResponseEntity.ok().build();
    }

//    @PutMapping("/reset")
//    public ResponseEntity<List<Lesson>> reset() {
//        List<Lesson> lessons = lessonService.findAll();
//        List<Lesson> updated = new ArrayList<>();
//        for(Lesson lesson: lessons) {
//            lesson.setTimeslot(null);
//            lesson.setLesson(null);
//            updated.add(lessonService.update(lesson));
//        }
//        return ResponseEntity.ok().body(updated);
//    }
}