package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.ClassroomType;
import ipb.pt.timetableapi.model.ClassroomTypeDto;
import ipb.pt.timetableapi.service.ClassroomTypeService;
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
@RequestMapping("/classroom-types")
public class ClassroomTypeController {
    @Autowired
    private ClassroomTypeService classroomTypeService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello classroomTypes!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(classroomTypeService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<ClassroomType> optional = classroomTypeService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody ClassroomTypeDto classroomTypeDto) {
        ClassroomType classroomType = new ClassroomType();
        BeanUtils.copyProperties(classroomTypeDto, classroomType);
        return ResponseEntity.ok().body(classroomTypeService.create(classroomType));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<ClassroomTypeDto> classroomTypeDtoList) {

        List<Object> created = new ArrayList<>();
        for (ClassroomTypeDto classroomTypeDto : classroomTypeDtoList) {
            ClassroomType classroomType = new ClassroomType();
            BeanUtils.copyProperties(classroomTypeDto, classroomType);
            created.add(classroomTypeService.create(classroomType));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody ClassroomTypeDto classroomTypeDto, @PathVariable Long id) {
        Optional<ClassroomType> optional = classroomTypeService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ClassroomType classroomType = optional.get();
        BeanUtils.copyProperties(classroomTypeDto, classroomType);
        return ResponseEntity.ok().body(classroomTypeService.update(classroomType));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<ClassroomType> areaOptional = classroomTypeService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        classroomTypeService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
