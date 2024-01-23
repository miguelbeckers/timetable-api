package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timetable;
import ipb.pt.timetableapi.service.ClassroomService;
import ipb.pt.timetableapi.service.LessonUnitService;
import ipb.pt.timetableapi.service.TimeslotService;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Controller
@CrossOrigin
@RequestMapping("/timetables")
public class TimetableController {
    private static final UUID problemId = UUID.randomUUID();
    private final SolverManager<Timetable, UUID> solverManager;
    private final ClassroomService classroomService;
    private final LessonUnitService lessonUnitService;
    private final TimeslotService timeslotService;

    @Autowired
    public TimetableController(
            SolverManager<Timetable, UUID> solverManager,
            ClassroomService classroomService,
            LessonUnitService lessonUnitService,
            TimeslotService timeslotService
    ) {
        this.solverManager = solverManager;
        this.classroomService = classroomService;
        this.lessonUnitService = lessonUnitService;
        this.timeslotService = timeslotService;
    }

    @PostMapping("/solve")
    public ResponseEntity<Object> solve() {
        Timetable problem = new Timetable(timeslotService.findAll(), classroomService.findAll(), lessonUnitService.findAll());

        SolverJob<Timetable, UUID> solverJob = solverManager.solve(problemId, problem);
        Timetable solution;

        try {
            solution = solverJob.getFinalBestSolution();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }

        for (LessonUnit lessonUnit : solution.getLessonUnits()) {
            lessonUnitService.update(lessonUnit);
        }

        return ResponseEntity.ok().body(solution);
    }

    @PostMapping("/stop")
    public ResponseEntity<Object> stop() {
        solverManager.terminateEarly(problemId);
        return ResponseEntity.ok().build();
    }
}
