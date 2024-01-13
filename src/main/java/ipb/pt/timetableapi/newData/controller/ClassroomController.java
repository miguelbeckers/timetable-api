package ipb.pt.timetableapi.newData.controller;

import ipb.pt.timetableapi.newData.dto.ClassroomDto;
import ipb.pt.timetableapi.newData.model.Classroom;
import ipb.pt.timetableapi.newData.service.ClassroomService;
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
@RequestMapping("/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello classrooms!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(classroomService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Classroom> optional = classroomService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody ClassroomDto classroomDto) {
        Classroom classroom = new Classroom();
        BeanUtils.copyProperties(classroomDto, classroom);
        return ResponseEntity.ok().body(classroomService.create(classroom));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<ClassroomDto> classroomDtoList) {

        List<Object> created = new ArrayList<>();
        for (ClassroomDto classroomDto : classroomDtoList) {
            Classroom classroom = new Classroom();
            BeanUtils.copyProperties(classroomDto, classroom);
            created.add(classroomService.create(classroom));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody ClassroomDto classroomDto, @PathVariable Long id) {
        Optional<Classroom> optional = classroomService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Classroom classroom = optional.get();
        BeanUtils.copyProperties(classroomDto, classroom);
        return ResponseEntity.ok().body(classroomService.update(classroom));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Classroom> areaOptional = classroomService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        classroomService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
