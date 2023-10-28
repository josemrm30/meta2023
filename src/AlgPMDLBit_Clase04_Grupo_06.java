import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlgPMDLBit_Clase04_Grupo_06 {

    private final Solution actualSolution;
    private final Random rand;
    private final Logger log;
    private final Problem problem;
    private final int iterations;

    public AlgPMDLBit_Clase04_Grupo_06(Problem problem, int iterations, Long seed, Logger log) {
        this.problem = problem;
        this.iterations = iterations;
        this.log = log;
        rand = new Random(seed);
        actualSolution = getInitialSolution(problem);
    }

    public Solution getInitialSolution(Problem problem) {
        AlgGreedy_Clase04_Grupo_06 greedy = new AlgGreedy_Clase04_Grupo_06(problem.getMatrixSize());
        return greedy.SoluGreedy(problem.getFlowMatrix(), problem.getDistMatrix());
    }

    public int[] swapSolution(int[] actualSolution, int i, int j) {
        int[] newSol = actualSolution.clone();
        int temp = newSol[i];
        newSol[i] = newSol[j];
        newSol[j] = temp;
        return newSol;
    }

    public int searchLocalSolution() {
        int size = problem.getMatrixSize();
        int[][] flow = problem.getFlowMatrix();
        int[][] dist = problem.getDistMatrix();
        int[] dlb = new int[size];
        int iter = 0;
        boolean improvement = true;
        int pos = rand.nextInt(0, size);
        int[] solutionList = actualSolution.getSolutionList();
        int actualCost = actualSolution.getCost();
        log.log(Level.INFO, "Random start position = " + pos);
        while (improvement && iter < iterations) {
            Arrays.fill(dlb, 0);

            improvement = false;
            int countI = 0;
            for (int i = pos; i <= size && countI < size; i++) {
                if (i == size) {
                    i = 0;
                }
                countI++;
                if (dlb[i] == 0) {
                    int countJ = 0;
                    for (int j = i + 1; j <= size && countJ < size; j++) {
                        if (j == size) {
                            j = 0;
                        }
                        countJ++;
                        if (i != j) {
                            log.log(Level.INFO, "Actual solution list = " + Arrays.toString(solutionList));
                            int[] newSolution = swapSolution(solutionList, i, j);
                            log.log(Level.INFO, "Swapped solution list in positions i = " + i + " j = " + j + " " + Arrays.toString(newSolution));
                            int newCost = Factorization2Opt(flow, dist, size, newSolution, actualCost, i, j);
                            log.log(Level.INFO, "Swapped solution cost = " + newCost);
                            log.log(Level.INFO, "i: " + i + " j: " + j + " countI: " + countI + " countJ: " + countJ + " iteration: " + iter + " dlb" + Arrays.toString(dlb) + " Cost: " + actualCost + " New cost: " + newCost);

                            if (newCost < actualCost) {
                                log.log(Level.INFO, "Iteration = " + iter);
                                log.log(Level.INFO, "Accepted swapped solution");
                                solutionList = newSolution;
                                actualCost = newCost;
                                dlb[i] = 0;
                                dlb[j] = 0;
                                pos = j;
                                improvement = true;
                                iter++;
                            } else {
                                log.log(Level.INFO, "Rejected swapped solution");
                                dlb[i] = 1;
                            }
                        }
                    }
                }
            }
        }
        return actualCost;
    }

    //factorization function  with 2 elements
    private int Factorization2Opt(int[][] flow, int[][] loc, int size, int[] actualSolution, int ActualCost, int r, int s) {
        for (int k = 0; k < size; k++) {
            if (k != r && k != s) {
                ActualCost += flow[r][k] * (loc[actualSolution[s]][actualSolution[k]] - loc[actualSolution[r]][actualSolution[k]]) +
                        (flow[s][k] * (loc[actualSolution[r]][actualSolution[k]] - loc[actualSolution[s]][actualSolution[k]])) +
                        (flow[k][r] * (loc[actualSolution[k]][actualSolution[s]] - loc[actualSolution[k]][actualSolution[r]])) +
                        (flow[k][s] * (loc[actualSolution[k]][actualSolution[r]] - loc[actualSolution[k]][actualSolution[s]]));
            }
        }
        return ActualCost;
    }
}
