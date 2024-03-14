package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.constant.TimeslotConstant;
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

import java.util.ArrayList;
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
        List<Classroom> classrooms = classroomRepository.findAll();
        List<Timeslot> timeslots = timeslotRepository.findAll();
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();

        lessonUnits.forEach(lessonUnit -> lessonUnit.setIsPinned(false));
        SolverJob<Timetable, UUID> solverJob = solve(lessonUnits, timeslots, classrooms);
        Timetable solution = solverJob.getFinalBestSolution();

        lessonUnitRepository.saveAll(solution.getLessonUnits());
        return ResponseEntity.ok().body("Solving completed with score: " + solution.getScore());
    }

    @PostMapping("solve-blocks")
    public ResponseEntity<Object> solveAsBlocks() throws ExecutionException, InterruptedException {
        List<Double> timeslotSizes = List.of(
                TimeslotConstant.SIZE_5,
                TimeslotConstant.SIZE_2_5,
                TimeslotConstant.SIZE_1,
                TimeslotConstant.SIZE_0_5);

        List<String> solutionScores = new ArrayList<>();
        List<Classroom> classrooms = classroomRepository.findAll();

        for (Double timeslotSize : timeslotSizes) {
            List<Timeslot> timeslots = timeslotService.getTimeslots(timeslotSize);
            List<LessonUnit> lessonBlocks = lessonUnitService.getLessonBlocks(timeslotSize);

            SolverJob<Timetable, UUID> solverJob = solve(lessonBlocks, timeslots, classrooms);
            Timetable solution = solverJob.getFinalBestSolution();

            List<LessonUnit> lessonUnits = lessonUnitService.divideLessonBlocks(solution.getLessonUnits());
            lessonUnits.forEach(lessonUnit -> lessonUnit.setIsPinned(true));
            lessonUnitRepository.saveAll(lessonUnits);

            solutionScores.add("Solving with size " + timeslotSize +" completed with score: " + solution.getScore());
        }

        return ResponseEntity.ok().body(solutionScores);
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
