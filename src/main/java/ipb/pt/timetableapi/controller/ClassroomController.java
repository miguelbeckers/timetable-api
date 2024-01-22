package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ClassroomDto;
import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.service.ClassroomService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin
@RequestMapping("/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(classroomService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(classroomService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody ClassroomDto classroomDto) {
        return ResponseEntity.ok().body(classroomService.create(classroomDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody ClassroomDto classroomDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(classroomService.update(classroomDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        classroomService.findById(id);
        return ResponseEntity.ok().build();
    }
}
