package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.converter.LessonUnitConverter;
import ipb.pt.timetableapi.dto.LessonUnitDto;
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

import java.util.List;
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
    private final LessonUnitConverter lessonUnitConverter;

    @Autowired
    public TimetableController(
            SolverManager<Timetable, UUID> solverManager,
            ClassroomService classroomService,
            LessonUnitService lessonUnitService,
            TimeslotService timeslotService,
            LessonUnitConverter lessonUnitConverter
    ) {
        this.solverManager = solverManager;
        this.classroomService = classroomService;
        this.lessonUnitService = lessonUnitService;
        this.timeslotService = timeslotService;
        this.lessonUnitConverter = lessonUnitConverter;
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

        List<LessonUnitDto> lessonUnitDtos = lessonUnitConverter.toDto(solution.getLessonUnits());

        for (LessonUnitDto lessonUnitDto : lessonUnitDtos) {
            lessonUnitService.update(lessonUnitDto, lessonUnitDto.getId());
        }

        return ResponseEntity.ok().body(solution);
    }

    @PostMapping("/stop")
    public ResponseEntity<Object> stop() {
        solverManager.terminateEarly(problemId);
        return ResponseEntity.ok().build();
    }
}
