package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.repository.ClassroomRepository;
import ipb.pt.timetableapi.repository.LessonRepository;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

public class TimeTableConstraintProvider implements ConstraintProvider {
    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                roomConflict(constraintFactory),
//                professorConflict(constraintFactory),
//                studentGroupConflict(constraintFactory),
//                groupSizeAndCapacityConflict(constraintFactory),
//                // Soft constraints
//                professorTimeEfficiency(constraintFactory)
        };
    }

    public void penalizeLesson(Lesson lesson) {
        lesson.setColor("black");
    }

    private Constraint roomConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getClassroom),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Room conflict");
    }

//    private Constraint professorConflict(ConstraintFactory constraintFactory) {
//        return constraintFactory.forEach(Lesson.class)
//                .join(Lesson.class,
//                        Joiners.equal(Lesson::getTimeslot),
//                        Joiners.equal(Lesson::getProfessor),
//                        Joiners.lessThan(Lesson::getId))
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Teacher conflict");
//    }
//
//    private Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
//        return constraintFactory.forEach(Lesson.class)
//                .join(Lesson.class,
//                        Joiners.equal(Lesson::getTimeslot),
//                        Joiners.equal(Lesson::getStudentGroup),
//                        Joiners.lessThan(Lesson::getId))
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Student group conflict");
//    }
//
//    private Constraint professorTimeEfficiency(ConstraintFactory constraintFactory) {
//        return constraintFactory
//                .forEach(Lesson.class)
//                .join(Lesson.class,
//                        Joiners.equal(Lesson::getProfessor))
//                .filter((lesson1, lesson2) -> {
//                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
//                            lesson2.getTimeslot().getStartTime());
//                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
//                })
//                .reward(HardSoftScore.ONE_SOFT)
//                .asConstraint("Teacher time efficiency");
//    }
//
//    private Constraint groupSizeAndCapacityConflict(ConstraintFactory constraintFactory) {
//        return constraintFactory.forEach(Lesson.class)
//                .filter(lesson -> lesson.getGroupSize() > lesson.getClassroom().getCapacity())
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Group size and capacity conflict");
//    }
}
