package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ResourceDto;
import ipb.pt.timetableapi.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@CrossOrigin
@RequestMapping("/resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(resourceService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(resourceService.findById(id));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody ResourceDto resourceDto) {
        return ResponseEntity.ok().body(resourceService.create(resourceDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@RequestBody ResourceDto resourceDto, @PathVariable Long id) {
        return ResponseEntity.ok().body(resourceService.update(resourceDto, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        resourceService.findById(id);
        return ResponseEntity.ok().build();
    }
}
