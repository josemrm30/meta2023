import java.sql.Array;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

public class TabuSearch {
    private Solution actualSolution;
    private Random rand;
    private Logger log;
    private final Problem problem;
    private final int iterations;
    private int Tabuprob;
    private int Tenenciatabu;

    public TabuSearch(Problem problem, int iterations, long seed, int Tabuprob, Logger log, int tenenciaTabu) {
        this.problem = problem;
        this.iterations = iterations;
        this.Tabuprob = Tabuprob;
        this.Tenenciatabu = tenenciaTabu;
        this.log = log;
        rand = new Random(seed);
        actualSolution = getInitialSolution(problem);
    }

    public Solution getInitialSolution(Problem problem) {
        Greedy greedy = new Greedy(problem.getMatrixSize());
        return greedy.SoluGreedy(problem.getFlowMatrix(), problem.getDistMatrix());
    }

    void masVisitados(int[][] mat, Solution provnuevaSol) {
        int[] nuevaSol = provnuevaSol.getSolutionList();
        int tam = nuevaSol.length;
        int mayor = Integer.MIN_VALUE;
        int pfuni = -1;
        int pcloc = -1;
        boolean[] marcafuni = new boolean[tam];
        boolean[] marcacloc = new boolean[tam];

        for (int k = 0; k < tam; k++) {
            for (int i = 0; i < tam; i++) {
                if (!marcafuni[i]) {
                    for (int j = 0; j < tam; j++) {
                        if (!marcacloc[j] && mat[i][j] >= mayor) {
                            mayor = mat[i][j];
                            pfuni = i;
                            pcloc = j;
                        }
                    }
                }
            }
            nuevaSol[pfuni] = pcloc;
            provnuevaSol.setSolutionList(nuevaSol);
            marcafuni[pfuni] = true;
            marcacloc[pcloc] = true;
            mayor = Integer.MIN_VALUE;
        }
    }

    void menosVisitados(int[][] memfrec, Solution provnuevaSol) {
        int tam = provnuevaSol.getSolutionList().length;
        int[] nuevaSol = provnuevaSol.getSolutionList();
        int menor = Integer.MAX_VALUE;
        int pfuni = -1;
        int pcloc = -1;
        boolean[] marcafuni = new boolean[tam];
        boolean[] marcacloc = new boolean[tam];

        for (int k = 0; k < tam; k++) {
            for (int i = 0; i < tam; i++) {
                if (!marcafuni[i]) {
                    for (int j = 0; j < tam; j++) {
                        if (!marcacloc[j] && memfrec[i][j] <= menor) {
                            menor = memfrec[i][j];
                            pfuni = i;
                            pcloc = j;
                        }
                    }
                }
            }
            nuevaSol[pfuni] = pcloc;
            provnuevaSol.setSolutionList(nuevaSol);
            marcafuni[pfuni] = true;
            marcacloc[pcloc] = true;
            menor = Integer.MAX_VALUE;
        }
    }

    private int Factorization2Opt(int[][] flow, int[][] loc, int tam, Solution actualSolution, int ActualCost, int r, int s) {
        for (int k = 0; k < tam; k++) {
            if (k != r && k != s) {
                ActualCost += flow[r][k] * (loc[actualSolution.getSolutionList()[s] - 1][actualSolution.getSolutionList()[k] - 1] - loc[actualSolution.getSolutionList()[r] - 1][actualSolution.getSolutionList()[k] - 1]) +
                        flow[s][k] * (loc[actualSolution.getSolutionList()[r] - 1][actualSolution.getSolutionList()[k] - 1] - loc[actualSolution.getSolutionList()[s] - 1][actualSolution.getSolutionList()[k] - 1]) +
                        flow[k][r] * (loc[actualSolution.getSolutionList()[k] - 1][actualSolution.getSolutionList()[s] - 1] - loc[actualSolution.getSolutionList()[k] - 1][actualSolution.getSolutionList()[r] - 1]) +
                        flow[k][s] * (loc[actualSolution.getSolutionList()[k] - 1][actualSolution.getSolutionList()[r] - 1] - loc[actualSolution.getSolutionList()[k] - 1][actualSolution.getSolutionList()[s] - 1]);
            }
        }
        return ActualCost;
    }

    public int[] swapSolution(int[] actualSolution, int i, int j) {
        int[] newSol = actualSolution.clone();
        int temp = newSol[i];
        newSol[i] = newSol[j];
        newSol[j] = temp;
        return newSol;
    }

    public Solution swapSolution(Solution actualSolution, int i, int j) {
        //System.out.println("dato i: " + actualSolution.getSolutionList()[i] + " dato j: " + actualSolution.getSolutionList()[j]);
        int temp = actualSolution.getSolutionList()[i];
        actualSolution.getSolutionList()[i] = actualSolution.getSolutionList()[j];
        actualSolution.getSolutionList()[j] = temp;
        //System.out.println("dato i: " + actualSolution.getSolutionList()[i] + " dato j: " + actualSolution.getSolutionList()[j]);
        return actualSolution;
    }

    public void swap(ArrayList<Solution> lTabu, int fil, int col) {
        int temp = lTabu.get(fil).getSolutionList()[col];
        lTabu.get(fil).getSolutionList()[col] = lTabu.get(col).getSolutionList()[fil];
        lTabu.get(col).getSolutionList()[fil] = temp;
    }

    public void swap(Solution[] lTabu, int fil, int col) {
        int temp = lTabu[fil].getSolutionList()[col];
        lTabu[fil].getSolutionList()[col] = lTabu[col].getSolutionList()[fil];
        lTabu[col].getSolutionList()[fil] = temp;
    }

    public int[][] swap(int[][] lTabu, int fil, int col) {
        int temp = lTabu[fil][col];
        lTabu[fil][col] = lTabu[col][fil];
        lTabu[col][fil] = temp;
        return lTabu;
    }

    public void dlb25(int[] dlb, int tam) {
        double random;
        int contador = 0;
        int cantidad = tam / 4;
        for (int i = 0; i < tam; i++) {
            if (contador <= cantidad) {
                contador++;
                random = rand.nextDouble();
                if (random < 0.5) {
                    dlb[i] = 0;
                } else {
                    dlb[i] = 1;
                }
            } else {
                dlb[i] = 1;
            }
        }
        // y lo reorganizamos al azar
        for (int i = tam - 1; i >= 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = dlb[i];
            dlb[i] = dlb[j];
            dlb[j] = temp;
        }
    }

    int TabuSearch(int[][] flu, int[][] loc,
                   int tam, int evaluaciones, int tenenciaTabu, int estancamientosMax,
                   Solution SolActual) {

        int tipo, estancaCont;
        //Calculamos el coste de la Solucion inicial
        int CosteActual = actualSolution.getCost();
        //costes de soluciones de apoyo
        int CosteMejorPeor = Integer.MAX_VALUE, CGlobal = Integer.MAX_VALUE, CosteMejorMomento = Integer.MAX_VALUE;
        int CosteMejorMomentoAnt = 0;

        //calculo de veces q mejora o no la reinicializacion
        int OEMejoraInte = 0, OEnoMejoraInte = 0, OEMejoraDive = 0, OEnoMejoraDive = 0, osc = 0;
        Solution posmejorPeores = new Solution(tam);
        Solution aux2 = new Solution(tam);
        //memorias a corto y largo plazo

        //memoria de frecuencias
        int[][] memFrec = new int[tam + 1][tam + 1];

        for (int i = 0; i < tam; i++)
            for (int j = 0; j < tam; j++)
                memFrec[i][j] = 0;

        //lista tabu explicita (solución entera)
        ArrayList<Solution> lTabu = new ArrayList<>();


        //metemos la solucion inicial en tabu
        lTabu.add(actualSolution);
        //lista tabu implicita (movimientos)
        int[][] lTabu2 = new int[tam][tam];

        //dlb y vectores de apoyo
        int[] dlb = new int[tam];

        Solution mejorPeores = new Solution(tam);
        Solution SolGlobal = new Solution(tam);
        Solution nuevaSol = new Solution(tam);


        //inicializo la dlb
        dlb25(dlb,tam);

        int iter = 0;

        estancaCont = 0;
        boolean mejora;
        int pos = rand.nextInt(0, tam - 1);//random para darle dinamismo inicialmente //PARA ANOTAR LA ULTIMA POSICIÓN DE INTERCAMBIO ANTERIOR


        //CosteMejorPeor = Integer.MAX_VALUE;
        while (iter < evaluaciones) {
            iter++;

            mejora = false;

            //tipo=pos;     //SI NO HAY CARGA ALEATORIA ESTA OPCION DA EL MISMO RESULTADO AUN CAMBIANDO SEMILLA
            //tipo = rand.nextInt(0, tam - 1);   //PRIMERA UNIDAD DE INTERCAMBIO ALEATORIA

            CosteMejorPeor = Integer.MAX_VALUE;  //cada iteracion
            int filuni = -1, colpos = -1;
            //comenzar por el principio y llegar hasta el punto de partida
            for (int i = pos, cont = 0; cont < tam && !mejora; i++, cont++) {
                if (i == tam) i = 0;  //para que cicle
                if (dlb[i] == 0) {
                    boolean improve_flag = false;

                    for (int j = i + 1, cont1 = 0; cont1 < tam && !mejora; j++, cont1++) {
                        //checkMove(i,j)
                        if (j == tam) j = 0;  //para que cicle

                        //vemos si es Tabu con la primera Lista Tabu
                        boolean tabu = false;
                        aux2 = SolActual;
                        Solution newSol = new Solution(swapSolution(aux2, i, j));
                        //boolean iguales = Arrays.equals(SolActual.getSolutionList(), newSol.getSolutionList());
                        //System.out.println(iguales);
                        for (int l = 0; l < lTabu.size(); l++) {
                            if (lTabu.get(l).getSolutionList() == newSol.getSolutionList()) {
                                tabu = true;  //esta en lista tabu
                                break;
                            }
                        }
                        if (!tabu) {
                            //vemos si es Tabu con la segunda Lista Tabu
                            filuni = i;
                            colpos = j;
                            //System.out.println("no Tabu ");
                            if (filuni > colpos)
                                lTabu2 = swap(lTabu2, filuni, colpos); //Para trabajar con la triangular superior

                            if (lTabu2[filuni][colpos] > 0)
                                tabu = true;
                        }

                        //si no es Tabu
                        if (!tabu) {
                            //funcion de factorizacion para ver si mejora o no si lo intercambiaramos
                            int C = Factorization2Opt(flu, loc, tam, SolActual, CosteActual, i, j);
                            if (C < CosteActual) {
                                //iter++; //YA esta PUESTO ARRIBA
                                CosteActual = C;
                                SolActual = (swapSolution(SolActual, i, j));

                                filuni = i;
                                colpos = j;  //me quedo el par de intercambio

                                dlb[i] = dlb[j] = 0;
                                pos = j;    //ULTIMA UNIDAD DE INTERCAMBIO
                                improve_flag = true;
                                mejora = true;
                            } else {
                                if (C < CosteMejorPeor) {  //ojo como actualiza
                                    //System.out.println("actualiza mejor peor");
                                    CosteMejorPeor = C;
                                    posmejorPeores.setSolutionList(SolActual.getSolutionList());
                                    posmejorPeores.setCost(SolActual.getCost());
                                    mejorPeores = swapSolution(posmejorPeores, i, j);

                                    filuni = i;
                                    colpos = j; //me quedo el par de intercambio
                                }
                            }
                        }
                    }
                    if (improve_flag == false) {
                        dlb[i] = 1;
                    }
                }
            }
            //UNA VEZ VISITO TODO EL VECINDARIO O HAY MEJORA
            //TENEMOS UN MOVIMIENTO Y ACTUALIZAMOS MEMORIAS

            if (mejora) {
                //ACTUALIZO la memoria de frecuencias
                for (int k = 0; k < tam; k++) {
                    memFrec[k][SolActual.getSolutionList()[k]]++;
                }
                if (lTabu.size() >= tenenciaTabu) {
                    lTabu.remove(0);
                }
                lTabu.add(SolActual);

            } else {
                //ACTUALIZO la memoria de frecuencias
                for (int k = 0; k < tam; k++) {
                    memFrec[k][mejorPeores.getSolutionList()[k]]++;
                }
                if (lTabu.size() >= tenenciaTabu) {
                    lTabu.remove(0);
                }
                lTabu.add(mejorPeores);
            }

            //ACTUALIZO tabu2 con pares de intercambio
            //solo recorro la triangular superior para ir más rápido
            for (int k = 0; k < tam - 1; k++) {
                for (int l = k + 1; l < tam; l++) {
                    if (lTabu2[k][l] > 0)
                        lTabu2[k][l]--;
                }
            }

            if (filuni > colpos) lTabu2 = swap(lTabu2, filuni, colpos);
            lTabu2[filuni][colpos] = tenenciaTabu;

            if (!mejora) {
                estancaCont++;

                if (CosteMejorPeor != Integer.MAX_VALUE) { //evita posibles dlb nuevas con todo 1's
                    CosteActual = CosteMejorPeor;
                    SolActual = mejorPeores;
                }
                //CosteMejorPeor++;
                //Para los del viernes
                dlb25(dlb, tam);

            } else {

                if (CosteMejorMomentoAnt > CosteActual) {  //Asi es la ultima forma que ha dicho
                    estancaCont = 0;
                    CosteMejorMomentoAnt = CosteActual;
                } else
                    estancaCont++;

                if (CosteActual < CGlobal) {
                    CGlobal = CosteActual;
                    SolGlobal = SolActual;
                }
            }

            if (estancaCont == estancamientosMax) {
                System.out.println("** Reinicializo");
                if (osc == 0) {
                    if (CosteMejorMomento > CosteActual) {
                        CosteMejorMomento = CosteActual;
                        OEMejoraDive++;

                    } else {
                        OEnoMejoraDive++;
                    }
                } else {
                    if (CosteMejorMomento > CosteActual) {
                        CosteMejorMomento = CosteActual;
                        OEMejoraInte++;

                    } else {
                        OEnoMejoraInte++;
                    }
                }

                estancaCont = 0;
                int prob = rand.nextInt(1, tam);
                //mostrarmatriz(memFrec);
                if (prob <= Tabuprob) {
                    osc = 0;
                    menosVisitados(memFrec, nuevaSol);
                } else {
                    osc = 1;
                    masVisitados(memFrec, nuevaSol);
                }
                System.out.println("act solucion: " + Arrays.toString(actualSolution.getSolutionList()) + actualSolution.getSolutionList().length);
                actualSolution.setSolutionList(nuevaSol.getSolutionList());
                actualSolution.setCost(nuevaSol.getCost());
                CosteActual = actualSolution.getCost();
                System.out.println("nue solucion: " + Arrays.toString(actualSolution.getSolutionList()) + actualSolution.getSolutionList().length);
                CosteMejorMomentoAnt = 0;
                if (CosteActual < CGlobal) {
                    CGlobal = CosteActual;
                    SolGlobal = SolActual;
                }

                //iniciamos esta variable para los empeoramientos
                CosteMejorMomentoAnt = 0;

                // Borramos la matriz de frecuencias
                for (int i = 0; i < tam; i++)
                    for (int j = 0; j < tam; j++)
                        memFrec[i][j] = 0;

                // Borramos la lista tabu


                for (int i = 0; i < tam - 1; i++)
                    for (int j = i + 1; j < tam; j++)
                        lTabu2[i][j] = 0;

                //reinicializamos la dlb


                dlb25(dlb,tam);
            }

            System.out.println();
            System.out.println("estancamiento: " + estancaCont);
            System.out.println("Paso = " + iter);
            System.out.println("Coste Actual: " + CosteActual);
            System.out.println("Coste MejorPeor: " + CosteMejorPeor);
            System.out.println("Coste Mejor Global: " + CGlobal);

            System.out.println("dlb actual: " + Arrays.toString(dlb));
//            for (int i = 0; i < memFrec.length; i++) {
//                System.out.println(Arrays.toString(memFrec[i]));
//            }
        }

        System.out.println("MEJORAS-Dive: " + OEMejoraDive + " NO MEJORAS-Dive: " + OEnoMejoraDive);
        System.out.println("MEJORAS-Inte: " + OEMejoraInte + " NO MEJORAS-Inte: " + OEnoMejoraInte);

        SolActual = SolGlobal;
        return CGlobal;
    }
}
