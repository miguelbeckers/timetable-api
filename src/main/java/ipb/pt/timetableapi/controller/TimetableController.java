package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.model.Timetable;
import ipb.pt.timetableapi.repository.ClassroomRepository;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import ipb.pt.timetableapi.service.LessonUnitService;
import ipb.pt.timetableapi.service.TimeslotService;
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
    private final TimeslotService timeslotService;

    @Autowired
    public TimetableController(
            SolverManager<Timetable, UUID> solverManager,
            ClassroomRepository classroomRepository,
            LessonUnitRepository lessonUnitRepository,
            TimeslotRepository timeslotRepository,
            LessonUnitService lessonUnitService,
            TimeslotService timeslotService
    ) {
        this.solverManager = solverManager;
        this.classroomRepository = classroomRepository;
        this.lessonUnitRepository = lessonUnitRepository;
        this.timeslotRepository = timeslotRepository;
        this.lessonUnitService = lessonUnitService;
        this.timeslotService = timeslotService;
    }

    @PostMapping("/solve")
    public ResponseEntity<Object> solve() throws ExecutionException, InterruptedException {
        SolverJob<Timetable, UUID> solverJob = solve(
                lessonUnitRepository.findAll(),
                timeslotRepository.findAll(),
                classroomRepository.findAll());

        Timetable solution = solverJob.getFinalBestSolution();
        lessonUnitRepository.saveAll(solution.getLessonUnits());
        return ResponseEntity.ok().body("Solving completed with score: " + solution.getScore());
    }

    @PostMapping("solve-blocks")
    public ResponseEntity<Object> solveAsBlocks() throws ExecutionException, InterruptedException {
        List<LessonUnit> original = lessonUnitRepository.findAll();

        List<Classroom> classrooms = classroomRepository.findAll();
        List<LessonUnit> blocks = lessonUnitService.getLessonUnitsAsBlocks();

        List<Timeslot> timeslotsOf5 = timeslotService.combineTimeslotsIntoBlocks(5);
        List<LessonUnit> blocksOf5 = blocks.stream().filter(b -> b.getBlockSize() > 2.5 && b.getBlockSize() <= 5).toList();

        SolverJob<Timetable, UUID> solverJobOf5 = solve(blocksOf5, timeslotsOf5, classrooms);
        List<LessonUnit> solvedBlocksOf5 = solverJobOf5.getFinalBestSolution().getLessonUnits();

        List<LessonUnit> splitedBlocks = lessonUnitService.splitInTwoBlocks(solvedBlocksOf5, 0.5);

        return ResponseEntity.ok().body(original.size());
    }

    private SolverJob<Timetable, UUID> solve(List<LessonUnit> lessonUnits, List<Timeslot> timeslots, List<Classroom> classrooms) {
        TimetableConstraintConfiguration timetableConfiguration = new TimetableConstraintConfiguration();

        Timetable problem = new Timetable();
        problem.setClassrooms(classrooms);
        problem.setTimeslots(timeslots);
        problem.setLessonUnits(lessonUnits);
        problem.setTimetableConfiguration(timetableConfiguration);

        return solverManager.solve(problemId, problem);
    }

    @PostMapping("/stop")
    public ResponseEntity<Object> stop() {
        solverManager.terminateEarly(problemId);
        return ResponseEntity.ok().build();
    }
}
