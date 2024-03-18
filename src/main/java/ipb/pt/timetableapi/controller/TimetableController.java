package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutionException;

@Controller
@CrossOrigin
@RequestMapping("/timetables")
public class TimetableController {
    private final TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @PostMapping("/solve")
    public ResponseEntity<Object> solve() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok().body(timetableService.solve());
    }

    @PostMapping("solve-blocks")
    public ResponseEntity<Object> solveAsBlocks() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok().body(timetableService.solveAsBlocks());
    }

    @PostMapping("/stop")
    public ResponseEntity<Object> stop() {
        return ResponseEntity.ok().body(timetableService.stop());
    }
}
