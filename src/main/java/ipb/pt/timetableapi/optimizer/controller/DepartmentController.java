package ipb.pt.timetableapi.optimizer.controller;

import ipb.pt.timetableapi.optimizer.dto.DepartmentDto;
import ipb.pt.timetableapi.optimizer.model.Department;
import ipb.pt.timetableapi.optimizer.service.DepartmentService;
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
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello departments!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Department> optional = departmentService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody DepartmentDto departmentDto) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDto, department);
        return ResponseEntity.ok().body(departmentService.create(department));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<DepartmentDto> departmentDtoList) {

        List<Object> created = new ArrayList<>();
        for (DepartmentDto departmentDto : departmentDtoList) {
            Department department = new Department();
            BeanUtils.copyProperties(departmentDto, department);
            created.add(departmentService.create(department));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody DepartmentDto departmentDto, @PathVariable Long id) {
        Optional<Department> optional = departmentService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Department department = optional.get();
        BeanUtils.copyProperties(departmentDto, department);
        return ResponseEntity.ok().body(departmentService.update(department));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Department> areaOptional = departmentService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        departmentService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
