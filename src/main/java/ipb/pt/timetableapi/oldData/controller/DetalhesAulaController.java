package ipb.pt.timetableapi.oldData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ipb.pt.timetableapi.oldData.service.DetalhesAulaService;

@Controller
@RequestMapping("/detalhes-aula")
public class DetalhesAulaController {
    @Autowired
    private DetalhesAulaService detalhesAulaService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello detalhes-aula");
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(detalhesAulaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(detalhesAulaService.findById(id));
    }
}
