package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.model.Timetable;
import jakarta.transaction.Transactional;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@CrossOrigin
@RequestMapping("/timetables")
public class TimetableController {

    private static final Long ID = 1L;

    @Autowired
    private SolverManager<Timetable, Long> solverManager;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("hello timetables!");
    }

    @GetMapping
    public ResponseEntity<Object> getTimetable() {
        return ResponseEntity.ok(findById(ID));
    }

    @PostMapping("/solve")
    public void solve() {
        solverManager.solveAndListen(1L,
                this::findById,
                this::save);
    }

    @Transactional
    protected Timetable findById(Long id) {
        return new Timetable(
                Timeslot.listAll(),
                Classroom.listAll(),
                Lesson.listAll());
    }

    protected void save(Timetable timetable) {

    }
}