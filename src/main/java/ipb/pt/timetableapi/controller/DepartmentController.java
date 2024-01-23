package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.DepartmentDto;
import ipb.pt.timetableapi.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(departmentService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok().body(departmentService.create(departmentDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody DepartmentDto departmentDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(departmentService.update(departmentDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        departmentService.findById(id);
        return ResponseEntity.ok().build();
    }
}
