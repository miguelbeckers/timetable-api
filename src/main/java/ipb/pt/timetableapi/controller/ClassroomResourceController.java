package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ClassroomResourceDto;
import ipb.pt.timetableapi.service.ClassroomResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@CrossOrigin
@RequestMapping("/classroom-resources")
public class ClassroomResourceController {
    @Autowired
    private ClassroomResourceService classroomResourceService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(classroomResourceService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(classroomResourceService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody ClassroomResourceDto classroomResourceDto) {
        return ResponseEntity.ok().body(classroomResourceService.create(classroomResourceDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody ClassroomResourceDto classroomResourceDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(classroomResourceService.update(classroomResourceDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        classroomResourceService.findById(id);
        return ResponseEntity.ok().build();
    }
}
