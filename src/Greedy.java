public class Greedy {
    private final int[] distances;
    private final int[] flows;
    private final int size;

    public Greedy(int size) {
        this.size = size;
        distances = new int[size];
        flows = new int[size];

    }

    public int minorDist(int[] dist, int[] mark) {
        int minDist = Integer.MAX_VALUE;
        int department = 0;
        for (int i = 0; i < size; i++) {
            if (dist[i] <= minDist && mark[i] == 0) {
                minDist = dist[i];
                department = i;
            }
        }
        mark[department] = 1;
        return department;
    }

    public int majorFlow(int[] flow, int[] mark) {
        int maxFlow = Integer.MIN_VALUE;
        int pmajor = 0;
        for (int i = 0; i < size; i++) {
            if (flow[i] >= maxFlow && mark[i] == 0) {
                pmajor = i;
                maxFlow = flow[i];
            }
        }
        mark[pmajor] = 1;
        return pmajor;
    }

    public void CreatePotentials(int[] flowPotential, int[] distPotential, int[][] flow, int[][] dist) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                flowPotential[i] += flow[i][j];
                distPotential[i] += dist[i][j];
            }
        }
    }

    public Solution SoluGreedy(int[][] flow, int[][] distance) {
        Solution sol = new Solution(size);
        int location;
        int department;

        int[] mark1 = new int[size];
        int[] mark2 = new int[size];

        CreatePotentials(flows, distances, flow, distance);

        for (int i = 0; i < size; i++) {
            location = minorDist(distances, mark1);
            department = majorFlow(flows, mark2);
            sol.getSolutionList()[department] = location + 1;
        }
        sol.setCost(this.Cost(flow, distance, sol.getSolutionList()));

        return sol;
    }

    public int Cost(int[][] flow, int[][] loc, int[] sol) {
        int cost = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    cost += flow[i][j] * loc[sol[i]-1][sol[j]-1];
                }
            }
        }
        return cost;
    }
}