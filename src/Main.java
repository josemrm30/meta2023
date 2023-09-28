import java.util.ArrayList;
import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static ArrayList<MatrixLoader> matrixs;
    static Configurator config;

    public static void loadFiles(String[] args) {
        matrixs = new ArrayList<>();
        config = new Configurator(args[0]);

        for (int i = 0; i < config.getFiles().size(); i++) {
            MatrixLoader loader = new MatrixLoader(config.getFiles().get(i));
            matrixs.add(loader);
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


    public static void main(String[] args) {
        loadFiles(args);
        printFiles();
        //localsearch.greedy();
        //printSolution();
    }
}