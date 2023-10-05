
import java.util.Random;
import java.util.random.*;


public class LocalSearch {

    void SolSLocal(long flow[][], long loc[][], int size, int eval, int solActual[]) {


    }


    void initialcharge(int[] s, int tam, long seed) {

        int randomNumberInRange;
        //Set_random(seed);

        //genero vector con todos los elementos
        for (int i = 0; i < tam; i++) {
            s[i] = i;
        }
        // y lo reorganizamos al azar

        for (int i = 0; i < tam; i++) {
            randomNumberInRange = (int) Math.random() * (tam - 0) + 0;
            //elements swapping
            swap(s, i, randomNumberInRange);
        }
    }

    void swap(int[] SolActual, int r, int s) {
        int aux;
        aux = SolActual[r];
        SolActual[r] = SolActual[s];
        SolActual[s] = aux;
    }

    //factorization function  with 2 elements
    int FactCost2Opt(int[] ActualSol, int[][] flow, int[][] loc,
                      int tam, int ActualCost, int r, int s) {

        ActualCost += flow[s][s] * (loc[ActualSol[r]][ActualSol[r]] - loc[ActualSol[s]][ActualSol[s]]) +
                flow[r][r] * (loc[ActualSol[s]][ActualSol[s]] - loc[ActualSol[r]][ActualSol[r]]) +
                flow[s][r] * (loc[ActualSol[r]][ActualSol[s]] - loc[ActualSol[s]][ActualSol[r]]) +
                flow[r][s] * (loc[ActualSol[s]][ActualSol[r]] - loc[ActualSol[r]][ActualSol[s]]);
        for (int k = 0; k < tam; k++) {
            if (k != r && k != s)
                ActualCost += flow[r][k] * (loc[ActualSol[s]][ActualSol[k]] - loc[ActualSol[r]][ActualSol[k]]) +
                        flow[s][k] * (loc[ActualSol[r]][ActualSol[k]] - loc[ActualSol[s]][ActualSol[k]]) +
                        flow[k][r] * (loc[ActualSol[k]][ActualSol[s]] - loc[ActualSol[k]][ActualSol[r]]) +
                        flow[k][s] * (loc[ActualSol[k]][ActualSol[r]] - loc[ActualSol[k]][ActualSol[s]]);
        }
        return ActualCost;
    }
}
