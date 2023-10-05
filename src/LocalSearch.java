
import java.util.Random;
import java.util.random.*;


public class LocalSearch {

    static int[] generarSolucionInicial(int size) {
        int[] solucion = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            solucion[i] = rand.nextInt(size); // Asignar unidades aleatoriamente a localizaciones
        }
        return solucion;
    }

    public static int[] swap(int[] SolActual, int i, int j) {
        int[] nuevaSolucion = SolActual.clone();
        int temp = nuevaSolucion[i];
        nuevaSolucion[i] = nuevaSolucion[j];
        nuevaSolucion[j] = temp;
        return nuevaSolucion;
    }

    static void SolucionLocal(int flow[][], int loc[][], int size, int eval, int solActual[]) {
        int actualCost = Cost(solActual, flow, loc,size);
        int[] dlb = new int[size];

        for (int i = 0; i < size; i++) {
            dlb[i] = 0; // Inicializar la DLB con 0 (ninguna unidad bloqueada)
        }

        int iter = 0;
        boolean mejora = true;
        int pos = 0;
        while (mejora && iter < eval) {
            mejora = false;

            for (int i = pos; i < size; i++) {
                if (dlb[i] == 0) {
                    for (int j = i + 1; j < size; j++) {
                        if (i != j) {
                            // Generar una nueva solución intercambiando unidades i y j

                            int[] nuevaSolucion = swap(solActual,i,j);
                            // Calcular el costo de la nueva solución
                            int nuevoCosto = FactCost2Opt(nuevaSolucion, flow, loc,size,actualCost,i,j);

                            // Si la nueva solución es mejor, actualizar solActual y resetear la DLB
                            if (nuevoCosto < actualCost) {
                                solActual = nuevaSolucion;
                                actualCost = nuevoCosto;
                                dlb[i] = 0;
                                dlb[j] = 0;
                                pos=j;
                                mejora = true;
                                iter++;
                            } else {
                                dlb[i] = 1; // Marcar i como bloqueada en la DLB
                            }
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
            System.out.print(solActual[i] + " ");
        }
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

    //factorization function  with 2 elements
    static int FactCost2Opt(int[] ActualSol, int[][] flow, int[][] loc,
                      int tam, int ActualCost, int r, int s) {
        for (int k=0; k<tam; k++){
            if (k!=r && k!=s)
                ActualCost+= flow[r][k]*(loc[ActualSol[s]][ActualSol[k]]-loc[ActualSol[r]][ActualSol[k]])+
                        flow[s][k]*(loc[ActualSol[r]][ActualSol[k]]-loc[ActualSol[s]][ActualSol[k]])+
                        flow[k][r]*(loc[ActualSol[k]][ActualSol[s]]-loc[ActualSol[k]][ActualSol[r]])+
                        flow[k][s]*(loc[ActualSol[k]][ActualSol[r]]-loc[ActualSol[k]][ActualSol[s]]);
        }
        return ActualCost;
    }

    public static int Cost(int[] s, int[][] flow, int[][] loc, int size) {
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
