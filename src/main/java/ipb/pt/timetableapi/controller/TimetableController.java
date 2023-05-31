package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.Timetable;
import ipb.pt.timetableapi.service.ClassroomService;
import ipb.pt.timetableapi.service.LessonService;
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

    @Autowired
    private SolverManager<Timetable, UUID> solverManager;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    LessonService lessonService;

    @Autowired
    TimeslotService timeslotService;



    @PostMapping("/solve")
    public ResponseEntity<Object> solve() {
        Timetable problem = new Timetable(timeslotService.findAll(), classroomService.findAll(), lessonService.findAll());

        UUID problemId = UUID.randomUUID();
        SolverJob<Timetable, UUID> solverJob = solverManager.solve(problemId, problem);
        Timetable solution;
        try {
            solution = solverJob.getFinalBestSolution();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }

        for(Lesson lesson: solution.getLessons()) {
            lessonService.update(lesson);
        }

        return ResponseEntity.ok().body(solution);
    }
}