
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author pedro
 */
public class Solution {
    private String name;
    private int size;
    private int cost;
    private int[] solutionList;

    public void loadSolution(String filePath){
        String line;
        FileReader f = null;
        name = filePath.split("\\.")[0];
        try {

            f = new FileReader(filePath);
            BufferedReader b = new BufferedReader(f);


            try {
                line = b.readLine();
                String[] splited = line.split(" ");
                size = Integer.parseInt(splited[0]);
                solutionList = new int[size];
                System.out.println("Size: " + getSize());
                String[] splitedCost = line.split(" ");
                cost = (Integer.parseInt(splitedCost[1]));
                line = b.readLine();
                String[] splitedSol = line.split(" ");
                for (int i = 0; i < size; i++) {
                    solutionList[i] = Integer.parseInt(splitedSol[i]);
                }

            } catch (NumberFormatException ex) {
                System.err.println(ex);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


    public Solution(String filePath) {
        loadSolution(filePath);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getCost() {
        return cost;
    }

    public int[] getSolutionList() {
        return solutionList;
    }
}
