package ipb.pt.timetableapi.solver;

import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonStudent;
import ipb.pt.timetableapi.model.Student;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class TimeTableConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                // Hard constraints
                roomConflict(constraintFactory),
                studentConflict(constraintFactory),
                professorConflict(constraintFactory),
                // Soft constraints
                professorTimeEfficiency(constraintFactory)
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

    private Constraint studentConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.lessThan(Lesson::getId))
                .filter(this::haveStudentConflict)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student conflict");
    }

    private boolean haveStudentConflict(Lesson lesson1, Lesson lesson2) {
        List<Student> students1 = getStudentsInLesson(lesson1);
        List<Student> students2 = getStudentsInLesson(lesson2);

        for (Student student1 : students1) {
            if (students2.contains(student1)) {
                return true; // Student is in two places at the same time
            }
        }

        return false;
    }

    private List<Student> getStudentsInLesson(Lesson lesson) {
        return lesson.getLessonStudents().stream()
                .map(LessonStudent::getStudent)
                .collect(Collectors.toList());
    }

    private Constraint professorConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getProfessor),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
    }

    private Constraint professorTimeEfficiency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getProfessor))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher time efficiency");
    }
}
