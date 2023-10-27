import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;


public class ILS {
    private final int[] distances;
    private final int[] flows;
    private int size;
    private double percentIls;
    private int iterationsIls;
    private TabuSearch tabu;
    private Random rand;
    private Logger log;
    private Problem problem;


    public ILS(int size, Problem problem, int iterations, long seed, int Tabuprob, Logger log, int tabuTenure, double percent, double percentIls, int iterationsIls) {
        this.size = size;
        distances = new int[size];
        flows = new int[size];
        rand = new Random(seed);
        this.percentIls = percentIls;
        this.iterationsIls = iterationsIls;
        System.out.println("percentIls: " + percentIls);
        System.out.println("percent: for tabu " + percent);
        tabu = new TabuSearch(problem, iterations, seed, Tabuprob, log, tabuTenure,percent);
    }

    public int[] swapSolution(int[] actualSolution, int i, int j) {
        int[] newSol = actualSolution.clone();
        int temp = newSol[i];
        newSol[i] = newSol[j];
        newSol[j] = temp;
        return newSol;
    }

    public Solution swapSolution(Solution actualSolution, int i, int j) {
        int[] Sol = actualSolution.getSolutionList();
        int temp = Sol[i];
        Sol[i] = Sol[j];
        Sol[j] = temp;
        Solution newSol = new Solution(problem.getMatrixSize());
        newSol.setSolutionList(Sol);
        return newSol;
    }


    void Ils(int[][] flow, int[][] loc, int size, int evaluations, int tabuTenure, int blockage, Solution bestSolution) {

        int bestCost = Integer.MAX_VALUE;

        Solution actualSolution;
        actualSolution =  new Solution(bestSolution);

        for (int i = 0; i < iterationsIls; i++) {

            System.out.println("BEFORE: ils " + Arrays.toString(actualSolution.getSolutionList()));
            tabu.TabuSearch(flow, loc, size, evaluations, tabuTenure, blockage, actualSolution);

            System.out.println("AFTER: ils " + Arrays.toString(actualSolution.getSolutionList()));
            int cost = actualSolution.getCost();
            if (cost < bestCost) {
                bestSolution.setSolutionList(actualSolution.getSolutionList()) ;
                bestCost = cost;
            }
            //swap list
            int p1 = rand.nextInt(0, size - 1);
            int p2 = rand.nextInt(0, size - 1);
            //para que la diferencia no sea negativa
            if(p1 > p2){
                int temp = p1;
                p1 = p2;
                p2 = temp;
            }
            //System.out.println("percentIls2: " + percentIls);
            if ((p1 - p2) > size * percentIls) {
                swapSolution(actualSolution, p1, p2);
                for (int k = p1, j = p2; k < j; k++, j--) {
                    swapSolution(actualSolution, k, j);
                }
            }
        }
    }
}
