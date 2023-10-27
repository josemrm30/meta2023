import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;

public class Metaheuristic implements Runnable {
    private final Problem problem;
    private final Logger log;
    private final CountDownLatch cdl;
    private final int iterations;
    private final int TabuProb;
    private final int tabuTenure;
    private final int blockage;
    private double percent;
    private double percentIls;
    private int iterationsIls;
    private double probabilitySet;
    private final Long seed;
    private final String alg;

    public Metaheuristic(Problem problem, CountDownLatch cdl, Long seed, String logFile, boolean consoleLog, int iterations, int tabuProb, int tabuTenure, int blockage, double percent, double percentIls, int iterationsIls,double probabilitySet, String alg) throws IOException {
        this.problem = problem;
        this.cdl = cdl;
        this.iterations = iterations;
        this.TabuProb = tabuProb;
        this.tabuTenure = tabuTenure;
        this.blockage = blockage;
        this.percent = percent;
        this.percentIls = percentIls;
        this.probabilitySet = probabilitySet;
        this.iterationsIls = iterationsIls;
        this.seed = seed;
        this.alg = alg;
        log = Logger.getLogger(Metaheuristic.class.getName() + " " + logFile);
        if (consoleLog){
            ConsoleHandler consoleHand = new ConsoleHandler();
            log.addHandler(consoleHand);
        }
        else{
            FileHandler fileHand = new FileHandler(logFile);
            log.setUseParentHandlers(false);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHand.setFormatter(formatter);
            log.addHandler(fileHand);
        }
    }

    @Override
    public void run() {
        long initTime = System.currentTimeMillis();
        int cost = 0;
        switch (this.alg){
            case "PMDLBit":
                LocalSearch localSearch = new LocalSearch(problem, iterations, seed, log);
                cost = localSearch.searchLocalSolution();
                break;
            case "TabuSearch":
                TabuSearch tabuSearch = new TabuSearch(problem, iterations, seed,TabuProb ,log, tabuTenure, percent,probabilitySet);
                cost = tabuSearch.TabuSearch(problem.getFlowMatrix(),problem.getDistMatrix(),problem.getMatrixSize(),
                                    iterations, tabuTenure, blockage,tabuSearch.getInitialSolution(problem));
                break;
            case "ILS":
                TabuSearch tabuSearch2 = new TabuSearch(problem, iterations, seed,TabuProb ,log, tabuTenure, percent,probabilitySet);
                cost = tabuSearch2.TabuSearch(problem.getFlowMatrix(),problem.getDistMatrix(),problem.getMatrixSize(),
                        iterations, tabuTenure, blockage,tabuSearch2.getInitialSolution(problem));
                ILS ils = new ILS(problem.getMatrixSize(), problem, iterations,seed,TabuProb, log, tabuTenure, percent, percentIls, iterationsIls,probabilitySet);
                ils.Ils(problem.getFlowMatrix(),problem.getDistMatrix(),problem.getMatrixSize(),
                        iterations, tabuTenure, blockage,tabuSearch2.getInitialSolution(problem));
                break;

        }
        long endTime = System.currentTimeMillis();
        log.log(Level.INFO, "Run time = " + (endTime-initTime) + " milliseconds. " + "Final cost = " + cost);

        cdl.countDown();
    }
}
