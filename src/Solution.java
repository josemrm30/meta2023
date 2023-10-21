import java.util.Arrays;

/**
 * @author pedro
 */
public class Solution {

    private int cost;
    private int[] solutionList;

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

    public void setSolutionList(int[] solution){
        solutionList = solution;
    }

    public Solution(Solution other) {
        this.cost = other.cost;
        this.solutionList = Arrays.copyOf(other.solutionList, other.solutionList.length);
    }

    @Override
    public String toString() {
        return "Solution = {" +
                "cost = " + cost +
                ", solutionList = " + Arrays.toString(solutionList) +
                " }";
    }
}
