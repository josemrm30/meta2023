import java.util.ArrayList;
import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static ArrayList<MatrixLoader> matrixs;
    static Configurator config;
    static ArrayList<Solution> solutions;

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
        for (int i = 0; i < config.getFiles().size(); i++) {
            System.out.println(matrixs.get(i).getName());
            for (int j = 0; j < matrixs.get(i).getMatrix1().length; j++) {
                System.out.println(Arrays.toString(matrixs.get(i).getMatrix1()[j]));
            }
        }
    }


    public static void main(String[] args) {
        loadFiles(args);
        printFiles();

        Solution sol = new Solution(config.getSolutions().get(0));
        Greedy greedy = new Greedy();

        greedy.SoluGreedy(matrixs.get(0).getMatrix1(), matrixs.get(0).getMatrix2(), matrixs.get(0).getMatrixSize(), sol.getSolutionList());


        //localsearch.greedy();
        //printSolution();
    }
}