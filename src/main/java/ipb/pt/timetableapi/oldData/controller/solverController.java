package ipb.pt.timetableapi.oldData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ipb.pt.timetableapi.oldData.repository.HorarioRepository;
import ipb.pt.timetableapi.oldData.repository.TempoRepository;
import ipb.pt.timetableapi.oldData.model.Horario;
import ipb.pt.timetableapi.oldData.model.Tempo;

import java.util.List;

@Controller
@RequestMapping("/solver")
public class solverController {
    @Autowired
    private HorarioRepository horarioRepository;
    @Autowired
    private TempoRepository tempoRepository;

    @GetMapping("/horario")
    public ResponseEntity<Object> horario() {
        List<Horario> horarios = horarioRepository.findAll();
        return ResponseEntity.ok().body(horarios);
    }

    @GetMapping("/tempo")
    public ResponseEntity<Object> tempo() {
        List<Tempo> tempos = tempoRepository.findAll();
        return ResponseEntity.ok().body(tempos);
    }
}
