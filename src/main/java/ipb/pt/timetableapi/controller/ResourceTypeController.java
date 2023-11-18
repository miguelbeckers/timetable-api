package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ResourceTypeDto;
import ipb.pt.timetableapi.model.ResourceType;
import ipb.pt.timetableapi.service.ResourceTypeService;
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
public class ResourceTypeController {
    @Autowired
    private ResourceTypeService resourceTypeService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello resourceTypes!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(resourceTypeService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<ResourceType> optional = resourceTypeService.findById(id);
        return optional.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody ResourceTypeDto resourceTypeDto) {
        ResourceType resourceType = new ResourceType();
        BeanUtils.copyProperties(resourceTypeDto, resourceType);
        return ResponseEntity.ok().body(resourceTypeService.create(resourceType));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<ResourceTypeDto> resourceTypeDtoList) {

        List<Object> created = new ArrayList<>();
        for (ResourceTypeDto resourceTypeDto : resourceTypeDtoList) {
            ResourceType resourceType = new ResourceType();
            BeanUtils.copyProperties(resourceTypeDto, resourceType);
            created.add(resourceTypeService.create(resourceType));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody ResourceTypeDto resourceTypeDto, @PathVariable Long id) {
        Optional<ResourceType> optional = resourceTypeService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ResourceType resourceType = optional.get();
        BeanUtils.copyProperties(resourceTypeDto, resourceType);
        return ResponseEntity.ok().body(resourceTypeService.update(resourceType));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<ResourceType> areaOptional = resourceTypeService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        resourceTypeService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
