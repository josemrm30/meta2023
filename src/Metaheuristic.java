import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Metaheuristic implements Runnable {
    private Random rand;
    private MatrixLoader file;
    private StringBuilder log;
    private CountDownLatch cdl;

    public Metaheuristic(MatrixLoader file, CountDownLatch cdl, Long seed) {
        this.file = file;
        this.cdl = cdl;
        rand = new Random(seed);
        log = new StringBuilder();
    }

    @Override
    public void run() {
        log.append("Initial solution cost is X");
        long initTime = System.currentTimeMillis();
        //code to run
        long endTime = System.currentTimeMillis();

        log.append("Final solution cost is X." + "\n" + "Time spent: " + (endTime - initTime) / 1000 + "seconds.");
        cdl.countDown();
    }

    public String getLog() {
        return log.toString();
    }
}
