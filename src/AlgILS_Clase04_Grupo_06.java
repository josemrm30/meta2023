import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AlgILS_Clase04_Grupo_06 {
    private final double percentIls;
    private final int iterationsIls;
    private final AlgTabuVie_Clase04_Grupo_06 tabu;
    private final Random rand;
    private final Logger log;


    public AlgILS_Clase04_Grupo_06(long seed, int Tabuprob, Logger log, double percent, double percentIls, int iterationsIls, double probabilitySet) {
        rand = new Random(seed);
        this.percentIls = percentIls;
        this.iterationsIls = iterationsIls;
        this.log = log;
        tabu = new AlgTabuVie_Clase04_Grupo_06(seed, Tabuprob, log, percent, probabilitySet);
    }

    public void swapSolution(Solution actualSolution, int i, int j) {
        int[] Sol = actualSolution.getSolutionList();
        int temp = Sol[i];
        Sol[i] = Sol[j];
        Sol[j] = temp;
        Solution newSol = new Solution(Sol.length);
        newSol.setSolutionList(Sol);
    }


    void Ils(int[][] flow, int[][] loc, int size, int evaluations, int tabuTenure, int blockage, Solution bestSolution) {
        int bestCost = Integer.MAX_VALUE;
        Solution actualSolution;
        actualSolution = new Solution(bestSolution);

        log.log(Level.INFO, "started Solution " + actualSolution);

        for (int i = 0; i < iterationsIls; i++) {
            log.log(Level.INFO, "Iteration " + iterationsIls);
            log.log(Level.INFO, "BEFORE tabu: " + Arrays.toString(actualSolution.getSolutionList()));
            tabu.search(flow, loc, size, evaluations, tabuTenure, blockage, actualSolution);

            log.log(Level.INFO, "AFTER tabu: " + Arrays.toString(actualSolution.getSolutionList()));
            int cost = actualSolution.getCost();
            if (cost < bestCost) {
                bestSolution.setSolutionList(actualSolution.getSolutionList());
                bestCost = cost;
            }

            int p1 = rand.nextInt(0, size - 1);
            int p2 = rand.nextInt(0, size - 1);

            if (p1 > p2) {
                int temp = p1;
                p1 = p2;
                p2 = temp;
            }
            log.log(Level.INFO, "Swapped list point p1= " + p1 + "p2= " + p2);

            if ((p1 - p2) > size * percentIls) {
                swapSolution(actualSolution, p1, p2);
                for (int k = p1, j = p2; k < j; k++, j--) {
                    swapSolution(actualSolution, k, j);
                }
            }
            log.log(Level.INFO, "Swapped solution p1 = " + actualSolution);

        }
        log.log(Level.INFO, "Final Solution= " + actualSolution);

    }
}
