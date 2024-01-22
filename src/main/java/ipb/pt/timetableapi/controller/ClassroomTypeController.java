package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ClassroomTypeDto;
import ipb.pt.timetableapi.service.ClassroomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@CrossOrigin
@RequestMapping("/classroomType-types")
public class ClassroomTypeController {
    @Autowired
    private ClassroomTypeService classroomTypeService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(classroomTypeService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(classroomTypeService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody ClassroomTypeDto classroomTypeDto) {
        return ResponseEntity.ok().body(classroomTypeService.create(classroomTypeDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody ClassroomTypeDto classroomTypeDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(classroomTypeService.update(classroomTypeDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        classroomTypeService.findById(id);
        return ResponseEntity.ok().build();
    }
}
