package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.ClassroomTypeDto;
import ipb.pt.timetableapi.service.ClassroomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@CrossOrigin
@RequestMapping("/classroomType-types")
public class ClassroomTypeController {
    @Autowired
    private ClassroomTypeService classroomTypeService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String baseUrl;

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

    @DeleteMapping()
    public ResponseEntity<Object> deleteAll() {
        classroomTypeService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load")
    public ResponseEntity<Object> load() {
        ResponseEntity<ClassroomTypeDto[]> responseEntity = restTemplate.getForEntity(
                baseUrl + "/classroom-types", ClassroomTypeDto[].class);

        ClassroomTypeDto[] classroomTypeDtos = responseEntity.getBody();
        if (classroomTypeDtos == null) {
            return ResponseEntity.notFound().build();
        }

        classroomTypeService.createMany(Arrays.asList(classroomTypeDtos));
        return ResponseEntity.ok().build();
    }
}
