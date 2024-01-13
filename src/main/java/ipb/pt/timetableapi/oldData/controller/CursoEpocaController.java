package ipb.pt.timetableapi.oldData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ipb.pt.timetableapi.oldData.service.CursoEpocaService;

@Controller
@RequestMapping("/curso-epoca")
public class CursoEpocaController {
    @Autowired
    private CursoEpocaService cursoEpocaService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello curso-epoca");
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(cursoEpocaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(cursoEpocaService.findById(id));
    }
}
