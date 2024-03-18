package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.*;
import ipb.pt.timetableapi.repository.ClassroomRepository;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import ipb.pt.timetableapi.solver.TimetableConstraintConfiguration;
import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class TimetableService {
    private static final UUID problemId = UUID.randomUUID();
    private final SolverManager<Timetable, UUID> solverManager;
    private final ClassroomRepository classroomRepository;
    private final LessonUnitRepository lessonUnitRepository;
    private final TimeslotRepository timeslotRepository;
    private final LessonUnitService lessonUnitService;
    private final TimeslotService timeslotService;

    @Autowired
    public TimetableService(
            SolverManager<Timetable, UUID> solverManager,
            ClassroomRepository classroomRepository,
            LessonUnitRepository lessonUnitRepository,
            TimeslotRepository timeslotRepository,
            TimeslotService timeslotService,
            LessonUnitService lessonUnitService
    ) {
        this.solverManager = solverManager;
        this.classroomRepository = classroomRepository;
        this.lessonUnitRepository = lessonUnitRepository;
        this.timeslotRepository = timeslotRepository;
        this.lessonUnitService = lessonUnitService;
        this.timeslotService = timeslotService;
    }

    public String solve() throws ExecutionException, InterruptedException {
        List<Classroom> classrooms = classroomRepository.findAll();
        List<Timeslot> timeslots = timeslotRepository.findAll();
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();

        lessonUnits.forEach(lessonUnit -> lessonUnit.setIsPinned(false));
        SolverJob<Timetable, UUID> solverJob = solve(lessonUnits, timeslots, classrooms);
        Timetable solution = solverJob.getFinalBestSolution();

        lessonUnitRepository.saveAll(solution.getLessonUnits());
        return "Completed solving with score: " + solution.getScore();
    }

    private SolverJob<Timetable, UUID> solve(
            List<LessonUnit> lessonUnits,
            List<Timeslot> timeslots,
            List<Classroom> classrooms
    ) {
        TimetableConstraintConfiguration timetableConfiguration = new TimetableConstraintConfiguration();

        Timetable problem = new Timetable();
        problem.setClassrooms(classrooms);
        problem.setTimeslots(timeslots);
        problem.setLessonUnits(lessonUnits);
        problem.setTimetableConfiguration(timetableConfiguration);

        return solverManager.solve(problemId, problem);
    }

    public String stop() {
        solverManager.terminateEarly(problemId);
        return "Stopped solving";
    }

    public String solveAsBlocks() throws ExecutionException, InterruptedException {
        List<Double> sizes = List.of(
                TimeslotConstant.SIZE_5,
                TimeslotConstant.SIZE_2_5,
                TimeslotConstant.SIZE_1,
                TimeslotConstant.SIZE_0_5);

        List<String> solutionScores = new ArrayList<>();
        List<Classroom> classrooms = classroomRepository.findAll();

        for (Double size : sizes) {
            Double nextSize = sizes.indexOf(size) != sizes.size() - 1 ? sizes.get(sizes.indexOf(size) + 1) : null;
            double firstSize = sizes.get(0);

            List<Timeslot> timeslots = timeslotService.getTimeslotsBySize(size);
            List<LessonUnit> lessonBlocks = lessonUnitService.getLessonBlocksBySize(size, nextSize, firstSize);

            SolverJob<Timetable, UUID> solverJob = solve(lessonBlocks, timeslots, classrooms);
            Timetable solution = solverJob.getFinalBestSolution();

            List<LessonUnit> lessonUnits = lessonUnitService.divideLessonBlocksIntoUnits(solution.getLessonUnits());
            lessonUnits.forEach(lessonUnit -> lessonUnit.setIsPinned(true));

            lessonUnitRepository.saveAll(lessonUnits);
            solutionScores.add("Completed solving with size " + size + " and score: " + solution.getScore());
        }

        return solutionScores.toString();
    }
}
