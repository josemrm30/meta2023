
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


/**
 * @author pedro
 */
public class Solution {

    private int size;
    private int cost;
    private int[] solutionList;


    public Solution(int size) {
        this.size = size;
        solutionList = new int[size];
    }

    public void setCost(int cost){
        this.cost = cost;
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

    public void setSolutionList(int[] solution){
        solutionList = solution;
    }

    @Override
    public String toString() {
        return "Solution = {" +
                "cost = " + cost +
                ", solutionList = " + Arrays.toString(solutionList) +
                " }";
    }
}
