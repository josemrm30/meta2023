import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


// TODO: add javadoc
public class Problem {
    private String name;
    private int matrix1[][];
    private int matrix2[][];
    private int matrixSize;


    public Problem(String filePath) {
        String line;
        name = filePath.split("\\.")[0];
        FileReader f = null;
        try {
            f = new FileReader(filePath);
            BufferedReader b = new BufferedReader(f);
            matrixSize = Integer.parseInt(b.readLine());
            matrix1 = new int[matrixSize][matrixSize];
            matrix2 = new int[matrixSize][matrixSize];
            line = b.readLine();
            for (int i = 0; i < matrixSize; i++) {
                line = b.readLine();
                String[] splited = line.split(" ");
                int errors = 0;
                for (int j = 0; j < splited.length; j++) {
                    try {
                        matrix1[i][j - errors] = Integer.parseInt(splited[j]);
                    } catch (NumberFormatException ex) {
                        errors++;
                    }
                }
            }
            line = b.readLine();
            for (int i = 0; i < matrixSize; i++) {
                line = b.readLine();
                String[] splited = line.split(" ");
                int errors = 0;
                for (int j = 0; j < splited.length; j++) {
                    try {
                        matrix2[i][j - errors] = Integer.parseInt(splited[j]);
                    } catch (NumberFormatException ex) {
                        errors++;
                    }
                }
            }


        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public int getMatrixSize() {
        return matrixSize;
    }

    public String getName() {
        return name;
    }

    public int[][] getMatrix1() {
           return matrix1;
    }
    public int[][] getMatrix2() {
        return matrix2;
    }
  
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Data filename: " + name + "\n" + "Matrix 1:" + "\n");
        for (int[] ints : matrix1) {
            str.append(Arrays.toString(ints)).append("\n");
        }
        str.append("Matrix 2:").append("\n");
        for (int[] ints : matrix2) {
            str.append(Arrays.toString(ints)).append("\n");
        }
        return str.toString();
    }

}
