package ipb.pt.timetableapi.solver;

import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.optaplanner.core.api.solver.change.ProblemChange;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class SolverManagerImpl implements SolverManager {

    @Override
    public SolverJob solve(Object o, Function problemFinder, Consumer finalBestSolutionConsumer, BiConsumer exceptionHandler) {
        return null;
    }

    @Override
    public SolverJob solveAndListen(Object o, Function problemFinder, Consumer bestSolutionConsumer, Consumer finalBestSolutionConsumer, BiConsumer exceptionHandler) {
        return null;
    }

    @Override
    public SolverStatus getSolverStatus(Object o) {
        return null;
    }

    @Override
    public CompletableFuture<Void> addProblemChange(Object o, ProblemChange problemChange) {
        return null;
    }

    @Override
    public void terminateEarly(Object o) {

    }

    @Override
    public void close() {

    }
}
