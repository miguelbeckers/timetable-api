package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ResourceDto;
import ipb.pt.timetableapi.model.Resource;
import ipb.pt.timetableapi.service.ResourceService;
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
@RequestMapping("/resource-types")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello resourceTypes!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(resourceService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Resource> optional = resourceService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody ResourceDto resourceDto) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceDto, resource);
        return ResponseEntity.ok().body(resourceService.create(resource));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<ResourceDto> resourceDtoList) {

        List<Object> created = new ArrayList<>();
        for (ResourceDto resourceDto : resourceDtoList) {
            Resource resource = new Resource();
            BeanUtils.copyProperties(resourceDto, resource);
            created.add(resourceService.create(resource));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody ResourceDto resourceDto, @PathVariable Long id) {
        Optional<Resource> optional = resourceService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = optional.get();
        BeanUtils.copyProperties(resourceDto, resource);
        return ResponseEntity.ok().body(resourceService.update(resource));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Resource> areaOptional = resourceService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        resourceService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
