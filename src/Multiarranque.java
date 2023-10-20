import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;


public class Multiarranque {
    private final int[] distances;
    private final int[] flows;
    private int size;

    private TabuSearch tabu;
    private Solution actualSolution;
    private Random rand;
    private Logger log;
    private Problem problem;


    public Multiarranque(int size, Problem problem, int iterations, int seed, int Tabuprob, Logger log, int tenenciaTabu) {
        this.size = size;
        distances = new int[size];
        flows = new int[size];
        tabu = new TabuSearch(problem, iterations, seed, Tabuprob, log, tenenciaTabu);

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


    void Ils(int[][] flu, int[][] loc, int tam, int evaluaciones, int tenenciaTabu, int estancamientos, Solution mejorSol) {

        int mejorCoste = Integer.MAX_VALUE;

        Solution SolActual;
        SolActual = mejorSol;

        for (int i = 0; i < tam; i++) {

            System.out.println("ANTES:" + Arrays.toString(SolActual.getSolutionList()));
            tabu.TabuSearch(flu, loc, tam, evaluaciones, tenenciaTabu, estancamientos, SolActual);

            System.out.println("DESPUES:" + Arrays.toString(SolActual.getSolutionList()));
            int coste = SolActual.getCost();
            if (coste < mejorCoste) {
                mejorSol = SolActual;
                mejorCoste = coste;
            }
            //swap list
            int p1 = rand.nextInt(0, tam - 1);
            int p2 = rand.nextInt(0, tam - 1);
            if ((p1 - p2) > tam/4) {
                swapSolution(SolActual, p1, p2);
                for (int k = p1, j = p2; k < j; k++, j--) {
                    swapSolution(SolActual, k, j);
                }
            }
        }
    }

}
