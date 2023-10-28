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
    private final double percent;
    private final double percentIls;
    private final int iterationsIls;
    private final double probabilitySet;
    private final Long seed;
    private final String alg;

    public Metaheuristic(Problem problem, CountDownLatch cdl, Long seed, String logFile, boolean consoleLog, int iterations, int tabuProb, int tabuTenure, int blockage, double percent, double percentIls, int iterationsIls, double probabilitySet, String alg) throws IOException {
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
        if (consoleLog) {
            ConsoleHandler consoleHand = new ConsoleHandler();
            log.addHandler(consoleHand);
        } else {
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
        switch (this.alg) {
            case "PMDLBit":
                AlgPMDLBit_Clase04_Grupo_06 localSearch = new AlgPMDLBit_Clase04_Grupo_06(problem, iterations, seed, log);
                cost = localSearch.searchLocalSolution();
                break;
            case "TabuVie":
                AlgTabuVie_Clase04_Grupo_06 tabuSearch = new AlgTabuVie_Clase04_Grupo_06(seed, TabuProb, log, percent, probabilitySet);
                cost = tabuSearch.search(problem.getFlowMatrix(), problem.getDistMatrix(), problem.getMatrixSize(),
                        iterations, tabuTenure, blockage, tabuSearch.getInitialSolution(problem));
                break;
            case "ILS":
                AlgTabuVie_Clase04_Grupo_06 tabuSearch2 = new AlgTabuVie_Clase04_Grupo_06(seed, TabuProb, log, percent, probabilitySet);
                cost = tabuSearch2.search(problem.getFlowMatrix(), problem.getDistMatrix(), problem.getMatrixSize(),
                        iterations, tabuTenure, blockage, tabuSearch2.getInitialSolution(problem));
                AlgILS_Clase04_Grupo_06 ils = new AlgILS_Clase04_Grupo_06(seed, TabuProb, log, percent, percentIls, iterationsIls, probabilitySet);
                ils.Ils(problem.getFlowMatrix(), problem.getDistMatrix(), problem.getMatrixSize(),
                        iterations, tabuTenure, blockage, tabuSearch2.getInitialSolution(problem));
                break;

        }
        long endTime = System.currentTimeMillis();
        log.log(Level.INFO, "Run time = " + (endTime - initTime) + " milliseconds. " + "Final cost = " + cost);

        cdl.countDown();
        for (Handler handler : log.getHandlers()) {
            handler.close();
        }
    }
}
