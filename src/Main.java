import java.util.ArrayList;
import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static ArrayList<MatrixLoader> matrixs;
    static Configurator config;
    static ArrayList<Integer> solution;
    public static void loadFiles(String[] args) {
        matrixs = new ArrayList<>();
        config = new Configurator(args[0]);

        for (int i = 0; i < config.getFiles().size(); i++) {
            MatrixLoader loader = new MatrixLoader(config.getFiles().get(i));
            matrixs.add(loader);
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
        
        Solution sol = new Solution(args[args.length - 1]);
        Greedy greedy = new Greedy();
        
        greedy.SoluGreedy(matrixs.get(0).getMatrix1(), matrixs.get(0).getMatrix2(),matrixs.get(0).getMatrixSize() , sol.getSolution());
        
        
        //localsearch.greedy();
        //printSolution();
    }
}