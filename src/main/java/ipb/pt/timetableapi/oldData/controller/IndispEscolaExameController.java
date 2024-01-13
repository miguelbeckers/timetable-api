package ipb.pt.timetableapi.oldData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ipb.pt.timetableapi.oldData.service.IndispEscolaExameService;

@Controller
@RequestMapping("/indisp-escola-exame")
public class IndispEscolaExameController {
    @Autowired
    private IndispEscolaExameService indispEscolaExameService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello indisp-escola-exame");
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(indispEscolaExameService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(indispEscolaExameService.findById(id));
    }
}
