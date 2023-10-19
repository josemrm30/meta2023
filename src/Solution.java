import java.util.Arrays;

/**
 * @author pedro
 */
public class Solution {

    private int cost;
    private final int[] solutionList;


    public Solution(int size) {
        solutionList = new int[size];
    }

    public void setCost(int cost){
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public int[] getSolutionList() {
        return solutionList;
    }

    @Override
    public String toString() {
        return "Solution = {" +
                "cost = " + cost +
                ", solutionList = " + Arrays.toString(solutionList) +
                " }";
    }
}
