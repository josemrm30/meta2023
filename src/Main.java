import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.*;


public class Main {
    static ArrayList<Problem> problems = new ArrayList<>();
    static Configurator config;
    static ExecutorService executor = Executors.newCachedThreadPool();

    public static void loadFiles(String[] args) {
        config = new Configurator(args[0]);

        for (int i = 0; i < config.getFiles().size(); i++) {
            Problem problem = new Problem(config.getFiles().get(i));
            problems.add(problem);
        }
    }
    public static void runAlgorithms() throws IOException, InterruptedException {
        int num = config.getAlgorithms().size() * config.getFiles().size() * config.getSeeds().size();
        CountDownLatch cdl = new CountDownLatch(num);
        for (int i = 0; i < config.getAlgorithms().size(); i++) {
            for (Problem problem : problems) {
                for (int k = 0; k < config.getSeeds().size(); k++) {
                    String logFile = "log/" + config.getAlgorithms().get(i) + "_" + problem.getName() + "_" + config.getSeeds().get(k) + ".txt";
                    Metaheuristic meta = new Metaheuristic(problem, cdl, config.getSeeds().get(k), logFile, config.consoleLog,
                            config.getIterations(),config.getTabuprob(),config.getTenenciaTabu(),config.getEstancamientos(),
                            config.getAlgorithms().get(i));
                    //executor.execute(meta);
                    meta.run();
                }
            }
        }
        cdl.await();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        loadFiles(args);


        runAlgorithms();
        executor.shutdown();
    }
}