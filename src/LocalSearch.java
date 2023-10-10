import java.util.Random;
import java.util.logging.Logger;

public class LocalSearch {

    private final Solution actualSolution;
    private Random rand;
    private Logger log;
    private final Problem problem;
    private final int iterations;

    public LocalSearch(Problem problem, int iterations, Long seed, Logger log) {
        this.problem = problem;
        this.iterations = iterations;
        this.log = log;
        rand = new Random(seed);
        actualSolution = getInitialSolution(problem);
    }

    public Solution getInitialSolution(Problem problem) {
        Greedy greedy = new Greedy(problem.getMatrixSize());
        return greedy.SoluGreedy(problem.getFlowMatrix(), problem.getDistMatrix());
    }

    public int[] swapSolution(int[] actualSolution, int i, int j) {
        int[] newSol = actualSolution.clone();
        int temp = newSol[i];
        newSol[i] = newSol[j];
        newSol[j] = temp;
        return newSol;
    }

    public void searchLocalSolution() {
        int size = problem.getMatrixSize();
        int[][] flow = problem.getFlowMatrix();
        int[][] dist = problem.getDistMatrix();
        int[] dlb = new int[size];
        int iter = 0;
        boolean improvement = true;
        int pos = rand.nextInt(0, size);
        int[] solutionList = actualSolution.getSolutionList();
        int actualCost = actualSolution.getCost();


        while (improvement && iter < iterations) {
            improvement = false;

            for (int i = pos; i < size - 1; i++) {
                // TODO: preguntar a cristobal sobre como reinicializar desde el principio
                if (dlb[i] == 0) {
                    for (int j = i + 1; j < size; j++) {

                        int[] newSolution = swapSolution(solutionList, i, j);

                        int newCost = Factorization2Opt(flow, dist, size, newSolution, actualCost, i, j);

                        if (newCost < actualCost) {
                            solutionList = newSolution;
                            actualCost = newCost;
                            dlb[i] = 0;
                            dlb[j] = 0;
                            pos = j;
                            improvement = true;
                            iter++;
                        } else {
                            dlb[i] = 1;
                        }
                    }
                }
            }
            // Imprimir información de cada iteración si lo deseas
            System.out.println("Iteration " + iter + ", Cost: " + actualCost);
        }

        // Imprimir la solución final
        System.out.println("Final Solution: ");
        for (int i = 0; i < size; i++) {
            System.out.print(solutionList[i] + " ");
        }
    }


    //factorization function  with 2 elements
    private int Factorization2Opt(int[][] flow, int[][] loc, int tam, int[] actualSolution, int ActualCost, int r, int s) {
        for (int k = 0; k < tam; k++) {
            if (k != r && k != s) {
                ActualCost += flow[r][k] * (loc[actualSolution[s] - 1][actualSolution[k] - 1] - loc[actualSolution[r] - 1][actualSolution[k] - 1]) +
                        flow[s][k] * (loc[actualSolution[r] - 1][actualSolution[k] - 1] - loc[actualSolution[s] - 1][actualSolution[k] - 1]) +
                        flow[k][r] * (loc[actualSolution[k] - 1][actualSolution[s] - 1] - loc[actualSolution[k] - 1][actualSolution[r] - 1]) +
                        flow[k][s] * (loc[actualSolution[k] - 1][actualSolution[r] - 1] - loc[actualSolution[k] - 1][actualSolution[s] - 1]);
            }
        }
        return ActualCost;
    }
}
