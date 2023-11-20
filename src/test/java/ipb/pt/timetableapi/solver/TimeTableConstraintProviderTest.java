package ipb.pt.timetableapi.solver;


import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.Timetable;
import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;

import java.time.Duration;

public class TimeTableConstraintProviderTest {
    SolverFactory<Timetable> solverFactory = SolverFactory.create(new SolverConfig()
            .withSolutionClass(Timetable.class)
            .withEntityClasses(Lesson.class)
            .withTerminationSpentLimit(Duration.ofSeconds(1)));
    Solver<Timetable> solver = solverFactory.buildSolver();

//    @Test
//    public void testTimetableConstraints() {
//        // Passo 1: Crie instâncias do seu problema (Timetable)
//        Timetable timetable = new Timetable();
//
//        // Passo 2: Configure o estado inicial desejado
//
//        // Passo 3: Chame o solver para encontrar uma solução
//        Timetable solvedTimetable = solver.solve(timetable);
//
//        // Passo 4: Verifique a solução
//        // Exemplo: Verificar se a pontuação é a esperada
//        assertNotNull(solvedTimetable.getScore());
//        assertEquals(expectedHardScore, solvedTimetable.getScore().getHardScore());
//
//        // Exemplo: Verificar se as restrições específicas foram atendidas
//        assertTrue(checkSpecificConstraint(solvedTimetable));
//    }

    // Métodos auxiliares para criar instâncias do Timetable e verificar restrições
    // ...

    // Defina os métodos auxiliares conforme necessário
}
