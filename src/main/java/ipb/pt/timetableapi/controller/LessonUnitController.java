package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.LessonUnitDto;
import ipb.pt.timetableapi.service.LessonUnitService;
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
@RequestMapping("/lesson-units")
public class LessonUnitController {
    @Autowired
    private LessonUnitService lessonUnitService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello lessonUnits!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(lessonUnitService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<LessonUnit> optional = lessonUnitService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody LessonUnitDto lessonUnitDto) {
        LessonUnit lessonUnit = new LessonUnit();
        BeanUtils.copyProperties(lessonUnitDto, lessonUnit);
        return ResponseEntity.ok().body(lessonUnitService.create(lessonUnit));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<LessonUnitDto> lessonUnitDtoList) {

        List<Object> created = new ArrayList<>();
        for (LessonUnitDto lessonUnitDto : lessonUnitDtoList) {
            LessonUnit lessonUnit = new LessonUnit();
            BeanUtils.copyProperties(lessonUnitDto, lessonUnit);
            created.add(lessonUnitService.create(lessonUnit));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody LessonUnitDto lessonUnitDto, @PathVariable Long id) {
        Optional<LessonUnit> optional = lessonUnitService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LessonUnit lessonUnit = optional.get();
        BeanUtils.copyProperties(lessonUnitDto, lessonUnit);
        return ResponseEntity.ok().body(lessonUnitService.update(lessonUnit));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<LessonUnit> areaOptional = lessonUnitService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        lessonUnitService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
