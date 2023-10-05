import java.util.*;

public class Greedy {
    int[] mark1, mark2;

    public int minorDist(int[] dist, int[] mark, int size) {
        int pminor = 0;
        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            if (dist[i] < minDist && mark[i] == 0) {
                pminor = i;
                minDist = dist[i];
            }
        }
        mark[pminor] = 1;
        return pminor;
    }
/*
    public int minorDist(int[] dist, int[] mark, int size) {
        int pminor = 0;
        int[] prov = new int[size];

        for (int i = 0; i < size; i++) {
            if (dist[i] < dist[pminor] && mark[i] == 0) {
                pminor = i;
            }
        }
        for (int i = 0; i < size; i++) {
            if (dist[i] == dist[pminor] && mark[i] == 0) {
                prov[i] = 1;
            }
        }
        return pminor;
    }
*/

    public int majorFlow(int[] flow, int[] mark, int size) {
        int maxFlow = Integer.MIN_VALUE;
        int pmajor = 0;
        for (int i = 0; i < size; i++) {
            if (flow[i] > maxFlow && mark[i] == 0) {
                pmajor = i;
                maxFlow = flow[i];
            }
        }
        mark[pmajor] = 1;
        return pmajor;
    }

    public void CreatePotentials(int[] flowPotential, int[] distPotential, int size, int flow[][], int loc[][]) {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                flowPotential[i] = (flowPotential[i] + flow[i][j]);
                distPotential[i] = (distPotential[i] + loc[i][j]);
            }
        }
    }

    public void SoluGreedy(int[][] flow, int[][] distance, int size, int[] s) {
        int[] sol = new int[size];
        int location;
        int department;

        int[] distPotential = new int[size];
        int[] flowPotential = new int[size];
        mark1 = new int[size];
        mark2 = new int[size];

        CreatePotentials(flowPotential, distPotential, size, flow, distance);
        for (int i = 0; i < size; i++) {
            location = minorDist(distPotential, mark1, size);
            department = majorFlow(flowPotential, mark2, size);
            sol[department] = location;
        }
        int cost = this.Cost(s, flow, distance, size);

        System.out.println(Arrays.toString(sol));
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