import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
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
            for (int i=0; i<size; i++){
                dlb[i]=0;
            }

            improvement = false;
            int contadorI = 0;
            for (int i = pos; i <= size && contadorI < size; i++) {
                if (i == size) {
                    i = 0;
                }
                contadorI++;
                // TODO: preguntar a cristobal sobre como reinicializar desde el principio y sobre el coste extra de los logs
                if (dlb[i] == 0) {
                    int contadorJ = 0;
                    for (int j = i + 1; j <= size && contadorJ < size; j++) {
                        if (j == size) {
                            j = 0;
                        }
                        contadorJ++;
                        System.out.println("i:"+ i + " j:" + j+ " iter:" + iter + " dlb" + Arrays.toString(dlb));
                        if (i != j){
                            log.log(Level.INFO, "Actual solution list = " + Arrays.toString(solutionList));
                            int[] newSolution = swapSolution(solutionList, i, j);
                            log.log(Level.INFO, "Swapped solution list in positions i = " + i + " j = " + j + " " + Arrays.toString(newSolution));
                            int newCost = Factorization2Opt(flow, dist, size, newSolution, actualCost, i, j);
                            log.log(Level.INFO, "Swapped solution cost = " + newCost);

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
