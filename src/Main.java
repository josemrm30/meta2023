import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public static void runAlgorithms() throws IOException {
        for (int i = 0; i < config.getAlgorithms().size(); i++) {
            for (Problem problem : problems) {
                try {
                    CountDownLatch cdl = new CountDownLatch(config.getSeeds().size());
                        for (int k = 0; k < config.getSeeds().size(); k++) {
                            String logFile = "log/" + config.getAlgorithms().get(i) + "_" + problem.getName() + "_" + config.getSeeds().get(k) + ".txt";
                            Metaheuristic meta = new Metaheuristic(problem, cdl, config.getSeeds().get(k), logFile, config.consoleLog, config.getIterations(), config.getAlgorithms().get(i));
                            executor.execute(meta);
                        }
                        cdl.await();
                    } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        loadFiles(args);


/*
        LocalSearch Lsearch = new LocalSearch();
        int size = config.getFiles().size();
        int[][] flow = new int[size][size]; // Matriz de costos de flujo
        int[][] loc = new int[size][size];  // Matriz de costos de asignación
// Generar una solución inicial aleatoria
        System.out.println("Local Search solution");
        for (int i = 0; i < problems.size(); i++) {
            int[] solActual = Lsearch.getInitialSolution(config.getFiles().size(), problems.get(i).getMatrix1(), problems.get(i).getMatrix2()).getSolutionList();
            Lsearch.LocalSolution(problems.get(i).getMatrix1(), problems.get(i).getMatrix2(), problems.get(i).getMatrixSize(), config.getIterations());
        }
*/

        runAlgorithms();

    }
}