import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;

public class Metaheuristic implements Runnable {
    private Problem problem;
    private Logger log;
    private CountDownLatch cdl;
    private int iterations;
    private Long seed;
    private String alg;

    public Metaheuristic(Problem problem, CountDownLatch cdl, Long seed, String logFile, boolean consoleLog, int iterations, String alg) throws IOException {
        this.problem = problem;
        this.cdl = cdl;
        this.iterations = iterations;
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
        switch (this.alg){
            case "PMDLBit":
                LocalSearch localSearch = new LocalSearch(problem, iterations, seed, log);
                localSearch.searchLocalSolution();
                break;
        }
        long endTime = System.currentTimeMillis();
        log.log(Level.INFO, "Run time = " + (endTime-initTime) + "milliseconds");

        cdl.countDown();
    }
}
