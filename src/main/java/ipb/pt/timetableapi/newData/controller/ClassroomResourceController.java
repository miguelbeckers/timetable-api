package ipb.pt.timetableapi.newData.controller;

import ipb.pt.timetableapi.newData.dto.ClassroomResourceDto;
import ipb.pt.timetableapi.newData.model.ClassroomResource;
import ipb.pt.timetableapi.newData.service.ClassroomResourceService;
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
@RequestMapping("/resources")
public class ClassroomResourceController {
    @Autowired
    private ClassroomResourceService classroomResourceService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello resources!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(classroomResourceService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<ClassroomResource> optional = classroomResourceService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody ClassroomResourceDto classroomResourceDto) {
        ClassroomResource classroomResource = new ClassroomResource();
        BeanUtils.copyProperties(classroomResourceDto, classroomResource);
        return ResponseEntity.ok().body(classroomResourceService.create(classroomResource));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<ClassroomResourceDto> classroomResourceDtoList) {

        List<Object> created = new ArrayList<>();
        for (ClassroomResourceDto classroomResourceDto : classroomResourceDtoList) {
            ClassroomResource classroomResource = new ClassroomResource();
            BeanUtils.copyProperties(classroomResourceDto, classroomResource);
            created.add(classroomResourceService.create(classroomResource));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody ClassroomResourceDto classroomResourceDto, @PathVariable Long id) {
        Optional<ClassroomResource> optional = classroomResourceService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ClassroomResource classroomResource = optional.get();
        BeanUtils.copyProperties(classroomResourceDto, classroomResource);
        return ResponseEntity.ok().body(classroomResourceService.update(classroomResource));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<ClassroomResource> areaOptional = classroomResourceService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        classroomResourceService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
