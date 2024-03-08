package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.model.Timetable;
import ipb.pt.timetableapi.repository.ClassroomRepository;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import ipb.pt.timetableapi.solver.TimetableConstraintConfiguration;
import lombok.Data;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.optaplanner.core.api.score.ScoreManager;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Controller
@CrossOrigin
@RequestMapping("/timetables")
public class TimetableController {
    private static final UUID problemId = UUID.randomUUID();
    private LocalTime startTime = null;
    private LocalTime lastStartTime = null;
    private Duration lastDuration = null;
    private SolverJob<Timetable, UUID> solverJob = null;

//    private Duration spendTimeExpected = Duration.ofHours(10);

    private final SolverManager<Timetable, UUID> solverManager;

    private final ClassroomRepository classroomRepository;
    private final LessonUnitRepository lessonUnitRepository;
    private final TimeslotRepository timeslotRepository;

    @Autowired
    public TimetableController(
            SolverManager<Timetable, UUID> solverManager,
            ClassroomRepository classroomRepository,
            LessonUnitRepository lessonUnitRepository,
            TimeslotRepository timeslotRepository
    ) {
        this.solverManager = solverManager;
        this.classroomRepository = classroomRepository;
        this.lessonUnitRepository = lessonUnitRepository;
        this.timeslotRepository = timeslotRepository;
    }

    @PostMapping("/solve")
    public ResponseEntity<Object> solve() {
        if (startTime != null) {
            return ResponseEntity.status(409).body("Solver is already running.");
        }

        Timetable problem = createProblem();
        solverJob = solverManager.solve(problemId, problem);
        startTime = LocalTime.now();

        return runAndAnswer(true);
    }

    @GetMapping("/status")
    public ResponseEntity<Object> status() {
        HardSoftScore initialScore = computeScore(createProblem());
        ProcessStatus status = new ProcessStatus();
        status.setInitialScore(initialScore.toString());

        if (startTime == null) {
            if (lastStartTime != null && lastDuration != null) {
                status.setStartTime(lastStartTime.toString());
                status.setSeconds(lastDuration.toSeconds());
                status.setStatus("Completed");
            } else {
                status.setStatus("Not started");
            }
        } else {
            status.setStartTime(startTime.toString());
            status.setSeconds(Duration.between(startTime, LocalTime.now()).toSeconds());
            status.setStatus("Running");
        }

        return ResponseEntity.ok().body(status);
    }

    @PostMapping("/stop")
    public ResponseEntity<Object> stop() {
        if (startTime == null) {
            return ResponseEntity.status(404).body("Solver is not running.");
        }

        solverManager.terminateEarly(problemId);

        return runAndAnswer(false);
    }

    private ResponseEntity<Object> runAndAnswer(boolean save) {
        try {
            Timetable solution = solverJob.getFinalBestSolution();
            if (save) lessonUnitRepository.saveAll(solution.getLessonUnits());
            return ResponseEntity.ok().body("Solving completed with score: " + solution.getScore());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Solving failed.", e);
        } finally {
            if (startTime != null) {
                lastStartTime = startTime;
                lastDuration = Duration.between(startTime, LocalTime.now());
                startTime = null;
            }
        }
    }

    private Timetable createProblem() {
        Timetable problem = new Timetable();
        problem.setClassrooms(classroomRepository.findAll());
        problem.setTimeslots(timeslotRepository.findAll());
        problem.setLessonUnits(lessonUnitRepository.findAll());
        TimetableConstraintConfiguration timetableConfiguration = new TimetableConstraintConfiguration();
        timetableConfiguration.setRoomConflict(HardSoftScore.ofHard(10));
        problem.setTimetableConfiguration(timetableConfiguration);
        return problem;
    }

    private HardSoftScore computeScore(Timetable currentProblem) {
        ScoreManager<Timetable, HardSoftScore> scoreManager = ScoreManager.create(solverManager);
        scoreManager.updateScore(currentProblem);
        return currentProblem.getScore();
    }
}

@Data
class ProcessStatus {
    private String status;
    private String initialScore;
    private String startTime;
    private long seconds;
}
