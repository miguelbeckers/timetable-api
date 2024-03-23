package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.solver.SizeConstant;
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

    /** SOLVE CONCEPT

     The timetable has two dimensions: the timeslots and the classrooms.
     Note that each timeslot have the size of 0.5 hour, and goes from 08:00 to 23:00, from Monday to Friday.

     timeslots            c1    c2    c3    c4    c5    c6    c7    c8    c9    c10   [...]
     MON - 08:00 -> 08:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 08:30 -> 09:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 09:00 -> 09:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 09:30 -> 10:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 10:00 -> 10:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 10:30 -> 11:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 11:00 -> 11:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 11:30 -> 12:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 12:00 -> 12:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 12:30 -> 13:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     [...]

     We cannot place any lesson in the timetable because the lessons have different number of hours per week.
     So, the solution is to divide the lessons into units of 0.5 hours, and place them.

     Also, the lessons can be divided in a different number of blocks.
     This means that the lesson units can be distributed in sequence forming blocks.

     For example, a lesson with 5 hours per week will have 10 units of 0.5 hours each.
     Assuming that this lesson has 2 blocks, those units will form two lesson blocks of 2.5 hour each.

     So the regular solve method gets all the lesson units from the database, and place them in the timetable.
     This placement is done by using the constraints to ensure that the rules will be satisfied.

     timeslots            c1    c2    c3    c4    c5    c6    c7    c8    c9    c10   [...]
     MON - 08:00 -> 08:30 █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 08:30 -> 09:00 █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 09:00 -> 09:30 █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 09:30 -> 10:00 █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 10:00 -> 10:30 █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 10:30 -> 11:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 11:00 -> 11:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 11:30 -> 12:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 12:00 -> 12:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 12:30 -> 13:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     [...]

     TUE - 08:00 -> 08:30 ┌──── █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 08:30 -> 09:00 ┌──── █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 09:00 -> 09:30 ┌──── █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 09:30 -> 10:00 ┌──── █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 10:00 -> 10:30 ┌──── █████ ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 10:30 -> 11:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 11:00 -> 11:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 11:30 -> 12:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 12:00 -> 12:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     TUE - 12:30 -> 13:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     [...]
     */
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

    /*** SOLVE BY BLOCKS CONCEPT

     The problem of the regular solve method is that the process to create blocks is complex and time consuming.
     So, we could have a way to optimize the blocks instead of optimize separated units.

     One interesting functionality that has drawn attention is the possibility to pin the lesson units.
     When a lesson unit is pinned, it does't move, and the others will be arranged around it.

     That bring the idea of use different timeslot sizes to solve the problem.
     In this approach we could place the big blocks first, then we could divide and pin them to place the smaller ones.

     So then, we could have 4 different timeslot sizes: 5, 2.5 and 0.5 hours.
     In each dimension we could place all the blocks that are in between them.

     In the first dimension, the timeslot has the size of the entire day, and in the last one, it has the unit size.
     Not all the sizes do exist, but the process is able to handle all cases.

     timeslots            c1    c2    c3    c4    c5    c6    c7    c8    c9    c10  [...]
     MON - 08:00 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 13:00 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     MON - 13:00 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 18:00 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     MON - 18:00 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 23:00 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     [...]

     timeslots            c1    c2    c3    c4    c5    c6    c7    c8    c9    c10  [...]
     MON - 08:00 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 10:30 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     MON - 10:30 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 13:00 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     MON - 13:00 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 15:30 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     MON - 15:30 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 18:00 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     MON - 18:00 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 20:30 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     MON - 20:30 ┐        ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           │        │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │ │   │
     .           └> 23:00 └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘ └───┘
     [...]

     timeslots            c1    c2    c3    c4    c5    c6    c7    c8    c9    c10   [...]
     MON - 08:00 -> 08:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 08:30 -> 09:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 09:00 -> 09:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 09:30 -> 10:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 10:00 -> 10:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 10:30 -> 11:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 11:00 -> 11:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 11:30 -> 12:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 12:00 -> 12:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 12:30 -> 13:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 13:00 -> 13:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 13:30 -> 14:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 14:00 -> 14:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 14:30 -> 15:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 15:00 -> 15:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 15:30 -> 16:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 16:00 -> 16:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 16:30 -> 17:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 17:00 -> 17:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 17:30 -> 18:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 18:00 -> 18:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 18:30 -> 19:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 19:00 -> 19:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 19:30 -> 20:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 20:00 -> 20:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 20:30 -> 21:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 21:00 -> 21:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 21:30 -> 22:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 22:00 -> 22:30 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     MON - 22:30 -> 23:00 ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌──── ┌────
     [...]
     */
    public List<String> solveAsBlocks() throws ExecutionException, InterruptedException {
        List<Double> sizes = List.of(
                SizeConstant.SIZE_5,
                SizeConstant.SIZE_2_5,
                SizeConstant.SIZE_0_5);

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

        return solutionScores;
    }
}
