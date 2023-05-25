package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.RoomDto;
import ipb.pt.timetableapi.model.Room;
import ipb.pt.timetableapi.service.RoomService;
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
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello rooms!");
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        Optional<Room> optional = roomService.findById(id);
        return optional.isPresent() ? ResponseEntity.ok(optional.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody RoomDto roomDto) {
        Room room = new Room();
        BeanUtils.copyProperties(roomDto, room);
        return ResponseEntity.ok().body(roomService.create(room));
    }

    @Transactional
    @PostMapping("/many")
    public ResponseEntity<List<Object>> createMany(@Valid @RequestBody List<RoomDto> roomDtoList) {

        List<Object> created = new ArrayList<>();
        for (RoomDto roomDto : roomDtoList) {
            Room room = new Room();
            BeanUtils.copyProperties(roomDto, room);
            created.add(roomService.create(room));
        }

        return ResponseEntity.ok().body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@Valid @RequestBody RoomDto roomDto, @PathVariable Long id) {
        Optional<Room> optional = roomService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Room room = optional.get();
        BeanUtils.copyProperties(roomDto, room);
        return ResponseEntity.ok().body(roomService.update(room));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Room> areaOptional = roomService.findById(id);
        if (areaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        roomService.delete(areaOptional.get());
        return ResponseEntity.ok().build();
    }
}
