import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;

public class Metaheuristic implements Runnable {
    private Random rand;
    private Problem file;
    private Logger log;
    private CountDownLatch cdl;

    public Metaheuristic(Problem file, ArrayList<Solution> solutions, CountDownLatch cdl, Long seed, String logFile, boolean consoleLog) throws IOException {
        this.file = file;
        this.cdl = cdl;
        rand = new Random(seed);
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
        //code to run
        log.log(Level.INFO, "test");
        long endTime = System.currentTimeMillis();

        cdl.countDown();
    }
}
