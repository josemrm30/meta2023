import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static ArrayList<Problem> problems = new ArrayList<>();
    static Configurator config;
    static ExecutorService executor = Executors.newCachedThreadPool();
    static ArrayList<Solution> greedySolutions = new ArrayList<>();
    static Logger log;

    public static void loadFiles(String[] args) {
        config = new Configurator(args[0]);

        for (int i = 0; i < config.getFiles().size(); i++) {
            Problem problem = new Problem(config.getFiles().get(i));
            problems.add(problem);
        }
    }

    public static void getInitialSolutions() throws IOException {
        for (int i = 0; i < problems.size(); i++) {
            String logFile = "log/" + "greedy" + "_" + problems.get(i).getName() + ".txt";
            log = Logger.getLogger(Main.class.getName() + " " + logFile);
            if (config.consoleLog){
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
            Greedy greedy = new Greedy(problems.get(i).getMatrixSize());
            Solution greedySol = greedy.SoluGreedy(problems.get(i).getMatrix1(), problems.get(i).getMatrix2(), problems.get(i).getMatrixSize());
            greedySolutions.add(greedySol);
        }
    }

    public static void runAlgorithms() throws IOException {
        for (int i = 0; i < config.getAlgorithms().size(); i++) {
            for (Problem problem : problems) {
                try {
                    CountDownLatch cdl = new CountDownLatch(config.getSeeds().size());
                    switch (config.getAlgorithms().get(i)) {
                        case "BestFirst":
                            for (int k = 0; k < config.getSeeds().size(); k++) {
                                String logFile = "log/" + config.getAlgorithms().get(i) + "_" + problem.getName() + "_" + config.getSeeds().get(k) + ".txt";
                                Metaheuristic meta = new Metaheuristic(problem, greedySolutions, cdl, config.getSeeds().get(k), logFile, config.consoleLog);
                                executor.execute(meta);
                            }
                            cdl.await();
                            break;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        loadFiles(args);
        getInitialSolutions();


        LocalSearch Lsearch = new LocalSearch();
        int size = config.getFiles().size();
        int[][] flow = new int[size][size]; // Matriz de costos de flujo
        int[][] loc = new int[size][size];  // Matriz de costos de asignación
// Generar una solución inicial aleatoria
        System.out.println("Local Search solution");
        for (int i = 0; i < greedySolutions.size(); i++) {
            int[] solActual = Lsearch.getInitialSolution(config.getFiles().size(), problems.get(i).getMatrix1(), problems.get(i).getMatrix2()).getSolutionList();
            Lsearch.LocalSolution(problems.get(i).getMatrix1(), problems.get(i).getMatrix2(), problems.get(i).getMatrixSize(), config.getIterations(), greedySolutions.get(i));
        }

        runAlgorithms();

    }
}