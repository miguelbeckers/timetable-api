package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.Lesson;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;

public class TimeTableConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                roomConflict(constraintFactory),
                teacherConflict(constraintFactory),
                studentGroupConflict(constraintFactory),
                groupSizeAndCapacityConflict(constraintFactory),
                // Soft constraints
                teacherTimeEfficiency(constraintFactory)
        };
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

    private Constraint teacherConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getTeacher),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
    }

    private Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getStudentGroup),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student group conflict");
    }

    private Constraint teacherTimeEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTeacher))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher time efficiency");
    }

    private Constraint groupSizeAndCapacityConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .filter(lesson -> lesson.getClassroom() != null && lesson.getGroupSize() > lesson.getClassroom().getCapacity())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Group size and capacity conflict");
    }
}
