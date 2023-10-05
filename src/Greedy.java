import java.util.*;

public class Greedy {
    int[] mark;

    public int[] minorDist(ArrayList<Integer> vec, int []mark, int size) {
        int pminor = 0;
        int[] prov = new int[size];

        for (int i = 0; i < size; i++) {
            if (vec.get(i) < vec.get(pminor) && mark[i] == 0) {
                pminor = i;
                prov[i] = 1;
            }
        }
        return prov;
    }

    public void majorFlow(ArrayList<Integer> vec, int[] mark, int[] minordist, int size) {
        int pmajor = 0;
        for (int i = 0; i < size; i++) {
            if (vec.get(i) > vec.get(pmajor) && mark[i] == 0) {
                pmajor = i;

            }
        }
        mark[pmajor] = 1;
    }

    public void CreatePotentials(ArrayList<Integer> flowPotential, ArrayList<Integer> distPotential, int size, int flow[][], int loc[][]) {

        for (int i = 0; i < size; i++) {
            flowPotential.add(0);
            distPotential.add(0);
            for (int j = 0; j < size; j++) {
                flowPotential.set(i, flowPotential.get(i) + flow[i][j]);
                distPotential.set(i, distPotential.get(i) + loc[i][j]);
            }
            System.out.println("flow " + flowPotential.get(i) + " -- " + "dist " + distPotential.get(i));
        }
    }

    public void SoluGreedy(int[][] flow, int[][] loc, int size, int[] s) {
        int[] minorDist;

        ArrayList<Integer> distPotential = new ArrayList<>();
        ArrayList<Integer> flowPotential = new ArrayList<>();
        mark = new int[size];

        CreatePotentials(flowPotential, distPotential, size, flow, loc);

        for (int i = 0; i < size; i++) {
            minorDist = minorDist(distPotential, mark, size);
            majorFlow(flowPotential, mark, minorDist, size);
        }
        int cost = this.Cost(s, flow, loc, size);

        System.out.println("flow");
        System.out.println(flowPotential.toString());
        System.out.println("distance");
        System.out.println(distPotential.toString());
        System.out.println("solution");
        System.out.println(Arrays.toString(s));
        System.out.println("Cost: " + cost);


    }

    public int Cost(int[] s, int[][] flow, int[][] loc, int size) {
        int cost = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j)
                    cost += flow[i][j] * loc[i][j];
            }
        }
        return cost;
    }


}