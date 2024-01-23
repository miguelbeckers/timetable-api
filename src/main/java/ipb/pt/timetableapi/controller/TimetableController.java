package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.Timetable;
import ipb.pt.timetableapi.service.ClassroomService;
import ipb.pt.timetableapi.service.LessonService;
import ipb.pt.timetableapi.service.TimeslotService;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;


@Controller
@CrossOrigin
@RequestMapping("/timetables")
public class TimetableController {
    private static final UUID problemId = UUID.randomUUID();
    private final SolverManager<Timetable, UUID> solverManager;
    private final ClassroomService classroomService;
    private final LessonService lessonService;
    private final TimeslotService timeslotService;

    @Autowired
    public TimetableController(
            SolverManager<Timetable, UUID> solverManager,
            ClassroomService classroomService,
            LessonService lessonService,
            TimeslotService timeslotService
    ) {
        this.solverManager = solverManager;
        this.classroomService = classroomService;
        this.lessonService = lessonService;
        this.timeslotService = timeslotService;
    }

    @PostMapping("/solve")
    public ResponseEntity<Object> solve() {
//        Timetable problem = new Timetable(timeslotService.findAll(), classroomService.findAll(), lessonService.findAll());
//
//        SolverJob<Timetable, UUID> solverJob = solverManager.solve(problemId, problem);
//        Timetable solution;
//
//        try {
//            solution = solverJob.getFinalBestSolution();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new IllegalStateException("Solving failed.", e);
//        }
//
//        for (Lesson lesson : solution.getLessons()) {
//            lessonService.update(lesson);
//        }
//
//        return ResponseEntity.ok().body(solution);
        return null;
    }

    @PostMapping("/stop")
    public ResponseEntity<Object> stop() {
        solverManager.terminateEarly(problemId);
        return ResponseEntity.ok().build();
    }
}
