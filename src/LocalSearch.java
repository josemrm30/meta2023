
import java.util.Random;
import java.util.random.*;


public class LocalSearch {

    void SolSLocal(int flow[][], int loc[][], int size, int eval, int solActual[], int random) {
        int tipo;
        //Calculamos el coste de la Solucion inicial
        int actualCost=Cost (solActual,flow,loc,size);
        //inicializamos a 0 la dlb
        int[] dlb = new int [size];
        for (int i=0; i<size; i++){
            dlb[i]=0;
        }
        int iter=0;
        boolean mejora=true;
        int pos=random;      //la primera posición es aleatoria
        while (mejora && iter<eval) {
            mejora=false;

                tipo=pos;     //SI NO HAY CARGA ALEATORIA ESTA OPCION DA EL MISMO RESULTADO AUN CAMBIANDO SEMILLA

                //tipo=Randint(0,size-1);   //PRIMERA UNIDAD DE INTERCAMBIO ALEATORIA

            //comenzar por el principio y llegar hasta el punto de partida
            for (int i=tipo, cont=0; cont<size && !mejora; i++, cont++){
                if (i==size) i=0;  //para que vuelva a comparar con los que ha ya comparado y están a 0
                if (dlb[i]==0) {
                    boolean improve_flag = false;

                    for (int j=i+1, cont1=0; cont1<size && !mejora; j++, cont1++){

                        if (j==size) j=0;  //para que vuelva a comparar con los que ha ya comparado y están a 0
                        int C = FactCost2Opt (solActual, flow,loc, size, actualCost, i,j);
                        if (C<actualCost){
                            iter++;
                            actualCost=C;
                            swap(solActual,i,j);
                            dlb[i] = dlb[j] = 0;
                            pos=j;    //ULTIMA UNIDAD DE INTERCAMBIO
                            improve_flag = true;
                            mejora=true;
                        }

                    }
                    if (improve_flag == false) {
                        dlb[i] = 1;
                    }
                }
            }

            //  if (iter % kPaso == 0) {

            System.out.println( "Paso = " + iter);
            System.out.println("Coste BL: " + actualCost);
            System.out.println("Asignacion de Unidades a Localiz.:");
            for (int i = 0; i <size; i++) {
                System.out.print(solActual[i] + " ");
            }
            System.out.println();
            //  }
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

    public int Cost(int[] s, int[][] flow, int[][] loc, int size) {
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
