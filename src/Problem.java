import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


// TODO: add javadoc
public class Problem {
    private final String name;
    private final int[][] flowMatrix;
    private final int[][] distMatrix;
    private final int matrixSize;


    public Problem(String filePath) throws IOException {
        String line;
        name = filePath.split("\\.")[0];
        FileReader f;
        f = new FileReader(filePath);
        BufferedReader b = new BufferedReader(f);
        matrixSize = Integer.parseInt(b.readLine());
        flowMatrix = new int[matrixSize][matrixSize];
        distMatrix = new int[matrixSize][matrixSize];
        line = b.readLine();
        for (int i = 0; i < matrixSize; i++) {
            line = b.readLine();
            String[] splited = line.split(" ");
            int errors = 0;
            for (int j = 0; j < splited.length; j++) {
                try {
                    flowMatrix[i][j - errors] = Integer.parseInt(splited[j]);
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
                    distMatrix[i][j - errors] = Integer.parseInt(splited[j]);
                } catch (NumberFormatException ex) {
                    errors++;
                }
            }
        }
    }

    public int getMatrixSize() {
        return matrixSize;
    }

    public String getName() {
        return name;
    }

    public int[][] getFlowMatrix() {
           return flowMatrix;
    }
    public int[][] getDistMatrix() {
        return distMatrix;
    }
  
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Data filename: " + name + "\n" + "FlowMatrix:" + "\n");
        for (int[] ints : flowMatrix) {
            str.append(Arrays.toString(ints)).append("\n");
        }
        str.append("DistMatrix:").append("\n");
        for (int[] ints : distMatrix) {
            str.append(Arrays.toString(ints)).append("\n");
        }
        return str.toString();
    }

}
