package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.LessonResourceDto;
import ipb.pt.timetableapi.model.LessonResource;
import ipb.pt.timetableapi.service.LessonResourceService;
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
@RequestMapping("/lesson-resources")
public class LessonResourceController {
    @Autowired
    private LessonResourceService lessonResourceService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello lessonResources!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonResourceService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<LessonResource> optional = lessonResourceService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody LessonResourceDto lessonResourceDto) {
        LessonResource lessonResource = new LessonResource();
        BeanUtils.copyProperties(lessonResourceDto, lessonResource);
        return ResponseEntity.ok().body(lessonResourceService.create(lessonResource));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<LessonResourceDto> lessonResourceDtoList) {

        List<Object> created = new ArrayList<>();
        for (LessonResourceDto lessonResourceDto : lessonResourceDtoList) {
            LessonResource lessonResource = new LessonResource();
            BeanUtils.copyProperties(lessonResourceDto, lessonResource);
            created.add(lessonResourceService.create(lessonResource));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody LessonResourceDto lessonResourceDto, @PathVariable Long id) {
        Optional<LessonResource> optional = lessonResourceService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LessonResource lessonResource = optional.get();
        BeanUtils.copyProperties(lessonResourceDto, lessonResource);
        return ResponseEntity.ok().body(lessonResourceService.update(lessonResource));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<LessonResource> areaOptional = lessonResourceService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lessonResourceService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
