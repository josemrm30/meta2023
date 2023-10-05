import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static ArrayList<MatrixLoader> matrixs;
    static Configurator config;
    static ArrayList<Solution> solutions;
    static ExecutorService executor;

    public static void loadFiles(String[] args) {
        config = new Configurator(args[0]);
        matrixs = new ArrayList<>();
        solutions = new ArrayList<>();

        for (int i = 0; i < config.getFiles().size(); i++) {
            MatrixLoader loader = new MatrixLoader(config.getFiles().get(i));
            matrixs.add(loader);
        }
        for (int i = 0; i < config.getSolutions().size(); i++) {
            Solution solutionLoader = new Solution(config.getSolutions().get(i));
            solutions.add(solutionLoader);
        }
    }

    public static void printFiles() {
        if (config.logs) {
            System.out.println("TODO");
        } else {
            for (int i = 0; i < config.getFiles().size(); i++) {
                System.out.println(matrixs.get(i).toString());
            }
        }
    }

    public static void saveLog(String path, String text) throws IOException {
        PrintWriter pw;
        try (FileWriter file = new FileWriter(path)) {
            pw = new PrintWriter(file);
            pw.print(text);
        }
    }

    public static void runAlgorithms() throws IOException{
        executor = Executors.newCachedThreadPool();

        for (int i = 0; i < config.getAlgorithms().size(); i++) {
            for (MatrixLoader matrix : matrixs) {
                try {
                    CountDownLatch cdl = new CountDownLatch(config.getSeeds().size());
                    switch (config.getAlgorithms().get(i)) {
                        case "BestFirst":
                            ArrayList<Metaheuristic> algRun = new ArrayList<>();
                            for (int k = 0; k < config.getSeeds().size(); k++) {
                                Metaheuristic meta = new Metaheuristic(matrix, cdl, config.getSeeds().get(k));
                                algRun.add(meta);
                                executor.execute(meta);
                            }
                            cdl.await();
                            for (int k = 0; k < algRun.size(); k++) {
                                saveLog(("log/" + config.getAlgorithms().get(i) + "_" + matrix.getName()) + "_" + config.getSeeds().get(k) + ".txt", algRun.get(k).getLog());
                            }
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
        printFiles();

        //Solution sol = new Solution(config.getSolutions().get(0));
        Greedy greedy = new Greedy();
        LocalSearch localS = new LocalSearch();


        for (int i = 0; i < solutions.size(); i++) {
            greedy.SoluGreedy(matrixs.get(i).getMatrix1(), matrixs.get(i).getMatrix2(), matrixs.get(i).getMatrixSize(), solutions.get(i).getSolutionList());
        }
        for(int i=0; i < solutions.size();i++) {
            //localS.SolSLocal(matrixs.get(i).getMatrix1(), matrixs.get(i).getMatrix2(), matrixs.get(i).getMatrixSize(),config.getIterations(),solutions.get(i).getSolutionList(),0);
        }


        //localsearch.greedy();
        //printSolution();
        runAlgorithms();

    }
}