package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timetable;
import ipb.pt.timetableapi.repository.ClassroomRepository;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import ipb.pt.timetableapi.service.LessonUnitService;
import ipb.pt.timetableapi.solver.TimetableConstraintConfiguration;
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
    private final ClassroomRepository classroomRepository;
    private final LessonUnitRepository lessonUnitRepository;
    private final TimeslotRepository timeslotRepository;
    private final LessonUnitService lessonUnitService;

    @Autowired
    public TimetableController(
            SolverManager<Timetable, UUID> solverManager,
            ClassroomRepository classroomRepository,
            LessonUnitRepository lessonUnitRepository,
            TimeslotRepository timeslotRepository,
            LessonUnitService lessonUnitService
    ) {
        this.solverManager = solverManager;
        this.classroomRepository = classroomRepository;
        this.lessonUnitRepository = lessonUnitRepository;
        this.timeslotRepository = timeslotRepository;
        this.lessonUnitService = lessonUnitService;
    }

    @PostMapping("/solve")
    public ResponseEntity<Object> solve() {
        TimetableConstraintConfiguration timetableConfiguration = new TimetableConstraintConfiguration();

        Timetable problem = new Timetable();
        problem.setClassrooms(classroomRepository.findAll());
        problem.setTimeslots(timeslotRepository.findAll());
        problem.setLessonUnits(lessonUnitRepository.findAll());
        problem.setTimetableConfiguration(timetableConfiguration);

        SolverJob<Timetable, UUID> solverJob = solverManager.solve(problemId, problem);
        Timetable solution;

        try {
            solution = solverJob.getFinalBestSolution();
            lessonUnitRepository.saveAll(solution.getLessonUnits());
            return ResponseEntity.ok().body("Solving completed with score: " + solution.getScore());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        }
    }

    @PostMapping("solve-new")
    public ResponseEntity<Object> solveNew() {
        List<LessonUnit> lessonUnitsAsBlocks = lessonUnitService.getLessonUnitsAsBlocks();

        List<LessonUnit> blocksGreaterThen5 = lessonUnitsAsBlocks.stream()
                .filter(lessonUnit -> lessonUnit.getBlockSize() > 5).toList();

        // TODO: convert the timeslot
        List<LessonUnit> blocksOf5 = lessonUnitService.splitBlocks(blocksGreaterThen5, 5);

        // solve with blocks in size of 5
        // solve with blocks in size of 2.5
        // solve with blocks in size of 1
        // solve with blocks in size of 0.5

        return ResponseEntity.ok().body(lessonUnitsAsBlocks.size());
    }

    private void solveWithBlocksOfSize(List<LessonUnit> lessonUnits, double blockSize) {

        // filter the lessonUnits by the block size
    }

    @PostMapping("/stop")
    public ResponseEntity<Object> stop() {
        solverManager.terminateEarly(problemId);
        return ResponseEntity.ok().build();
    }
}
