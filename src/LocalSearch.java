
import java.util.Random;
import java.util.random.*;


public class LocalSearch {

    int[] generarSolucionInicial(int size) {
        int[] solucion = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            solucion[i] = rand.nextInt(size); // Asignar unidades aleatoriamente a localizaciones
        }
        return solucion;
    }

    public int[] swap(int[] SolActual, int i, int j) {
        int[] nuevaSolucion = SolActual.clone();
        int temp = nuevaSolucion[i];
        nuevaSolucion[i] = nuevaSolucion[j];
        nuevaSolucion[j] = temp;
        return nuevaSolucion;
    }

    void SolucionLocal(int flow[][], int loc[][], int size, int eval, int solActual[]) {
        int actualCost = Cost( flow, loc,size,solActual);
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
                            int nuevoCosto = FactCost2Opt( flow, loc,size,nuevaSolucion,actualCost,i,j);

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
    int FactCost2Opt( int[][] flow, int[][] loc,
                      int tam, int[] ActualSol,int ActualCost, int r, int s) {
        for (int k=0; k<tam; k++){
            if (k!=r && k!=s)
                ActualCost+= flow[r][k]*(loc[ActualSol[s]-1][ActualSol[k]-1]-loc[ActualSol[r]-1][ActualSol[k]-1])+
                        flow[s][k]*(loc[ActualSol[r]-1][ActualSol[k]-1]-loc[ActualSol[s]-1][ActualSol[k]-1])+
                        flow[k][r]*(loc[ActualSol[k]-1][ActualSol[s]-1]-loc[ActualSol[k]-1][ActualSol[r]-1])+
                        flow[k][s]*(loc[ActualSol[k]-1][ActualSol[r]-1]-loc[ActualSol[k]-1][ActualSol[s]-1]);
        }
        return ActualCost;
    }

    public int Cost(int[][] flow, int[][] loc, int size, int[] sol) {
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
