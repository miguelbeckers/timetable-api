package ipb.pt.timetableapi.solver;


import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TimeTableConstraintProviderTest {

    @Test
    public void testSolve() {
        // Arrange
        // Act
        // Assert

        Lesson lesson1 = new Lesson();
        lesson1.setGroupCount(2);
        lesson1.setGroupNumber(1);

        Lesson lesson2 = new Lesson();
        lesson2.setGroupCount(4);
        lesson2.setGroupNumber(3);

        boolean result = checkStudentGroupConflict(lesson1, lesson2);
        System.out.println(result);
    }

    private boolean checkStudentGroupConflict(Lesson lesson1, Lesson lesson2) {
        Lesson primeiro = lesson1.getGroupCount() < lesson2.getGroupCount() ? lesson1 : lesson2;
        Lesson segundo = lesson1.getGroupCount() > lesson2.getGroupCount() ? lesson1 : lesson2;

        double ratio = (double) segundo.getGroupCount() / primeiro.getGroupCount();
        boolean isMultiple = ratio % 1 == 0;

        int n1 = primeiro.getGroupNumber();
        int n2 = segundo.getGroupNumber();

        if (isMultiple) {
            for (int i = 1; i < primeiro.getGroupCount(); i++) {
                int n3 = (int) ratio * i;
                int n4 = (int) (ratio * (i + 1));

                if (i == 1 && n1 <= i && n2 <= n3) return true;
                if (n1 > i && n2 > n3 && n1 <= (i + 1) && n2 <= n4) return true;
            }
        } else {
            for (int i = 1; i < primeiro.getGroupCount(); i++) {
                int n3 = (int) Math.ceil(ratio * i);
                int n4 = (int) Math.ceil(ratio * (i + 1));

                if (i == 1 && n1 <= i && n2 <= n3) return true;
                if (n1 > i && n2 >= n3 && n1 <= (i + 1) && n2 <= n4) return true;
            }
        }

        return false;
    }

    //    SolverFactory<Timetable> solverFactory = SolverFactory.create(new SolverConfig()
    //            .withSolutionClass(Timetable.class)
    //            .withEntityClasses(Lesson.class)
    //            .withTerminationSpentLimit(Duration.ofSeconds(1)));
    //    Solver<Timetable> solver = solverFactory.buildSolver();
}
