import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;
import java.util.ArrayList;

public class TabuSearch {
    private Solution actualSolution;

    private Solution finalSolution;
    private Random rand;
    private Logger log;
    private final Problem problem;
    private final int iterations;
    private int Tabuprob;
    private int Tenenciatabu;
    private double porcentaje;


    public int Cost(int[][] flow, int[][] loc, int[] sol,int tam) {
        int cost = 0;
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (i != j) {
                    cost += flow[i][j] * loc[sol[i]][sol[j]];
                }
            }
        }
        return cost;
    }
    public TabuSearch(Problem problem, int iterations, long seed, int Tabuprob, Logger log, int tenenciaTabu,double porcentaje) {
        this.problem = problem;
        this.iterations = iterations;
        this.Tabuprob = Tabuprob;
        this.Tenenciatabu = tenenciaTabu;
        this.log = log;
        this.porcentaje = porcentaje;
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
        int pfuni = 0;
        int pcloc = 0;
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
            marcafuni[pfuni] = true;
            marcacloc[pcloc] = true;
            mayor = Integer.MIN_VALUE;
        }

    }

    void menosVisitados(int[][] memfrec, Solution provnuevaSol) {
        int tam = provnuevaSol.getSolutionList().length;
        int[] nuevaSol = provnuevaSol.getSolutionList();
        int menor = Integer.MAX_VALUE;
        int pfuni = 0;
        int pcloc = 0;
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
            marcafuni[pfuni] = true;
            marcacloc[pcloc] = true;
            menor = Integer.MAX_VALUE;
        }
    }

    private int Factorization2Opt(int[][] flow, int[][] loc, int tam, Solution actualSolution, int ActualCost, int r, int s) {
        for (int k = 0; k < tam; k++) {
            if (k != r && k != s) {
                ActualCost += flow[r][k] * (loc[actualSolution.getSolutionList()[s]][actualSolution.getSolutionList()[k]] - loc[actualSolution.getSolutionList()[r]][actualSolution.getSolutionList()[k]]) +
                        flow[s][k] * (loc[actualSolution.getSolutionList()[r]][actualSolution.getSolutionList()[k]] - loc[actualSolution.getSolutionList()[s]][actualSolution.getSolutionList()[k]]) +
                        flow[k][r] * (loc[actualSolution.getSolutionList()[k]][actualSolution.getSolutionList()[s]] - loc[actualSolution.getSolutionList()[k]][actualSolution.getSolutionList()[r]]) +
                        flow[k][s] * (loc[actualSolution.getSolutionList()[k]][actualSolution.getSolutionList()[r]] - loc[actualSolution.getSolutionList()[k]][actualSolution.getSolutionList()[s]]);
            }
        }
        return ActualCost;
    }
    private int Factorization2Opt2(int[][] flow, int[][] loc, int tam, Solution actualSolution, int ActualCost, int i, int j) {
        int actualCost= 0;
        for (int k = 0; k < tam; k++) {
            if(k!=i && k!=j){
                actualCost += flow[i][k] * (loc[actualSolution.getSolutionList()[j]][actualSolution.getSolutionList()[k]] - loc[actualSolution.getSolutionList()[i]][actualSolution.getSolutionList()[k]]);
                actualCost += flow[j][k] * (loc[actualSolution.getSolutionList()[i]][actualSolution.getSolutionList()[k]] - loc[actualSolution.getSolutionList()[j]][actualSolution.getSolutionList()[k]]);
            }
        }
    return actualCost;
    }

    private int Factorization2Opt3(int[][] flow, int[][] loc, int tam, Solution actualSolution, int ActualCost, int r, int s) {
        int actualCost = 0;
        for (int k = 0;0 < r && r < s && k < tam; k++) {
            if(k != r && k != s){
                s+= 2*(flow[k][r] - flow[k][s]) * (loc[actualSolution.getSolutionList()[k]][actualSolution.getSolutionList()[s]] - loc[actualSolution.getSolutionList()[k]][actualSolution.getSolutionList()[r]]);
            }
        }
        return actualCost;
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

    public void swap(int fila,int columna){
        int temp = fila;
        fila = columna;
        columna = temp;
    }

    public int[][] swap(int[][] lTabu, int fil, int col) {
        int temp = lTabu[fil][col];
        lTabu[fil][col] = lTabu[col][fil];
        lTabu[col][fil] = temp;
        return lTabu;
    }

    public void dlbPorcent(int[] dlb, int tam, double porcentaje) {
        double random;
        int contador = 0;
        double cantidad = tam * porcentaje;
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
        for (int i = tam - 1; i > 0; i--) {
            int j = rand.nextInt(i+1);
            int temp = dlb[i];
            dlb[i] = dlb[j];
            dlb[j] = temp;
        }
    }

    int TabuSearch(int[][] flu, int[][] loc,
                   int tam, int evaluaciones, int tenenciaTabu, int estancamientosMax,
                   Solution SolActual) {

        int estancaCont;
        //Calculamos el coste de la Solucion inicial
        int CosteActual = SolActual.getCost();

        //costes de soluciones de apoyo
        int CosteMejorPeor = Integer.MAX_VALUE, CGlobal = Integer.MAX_VALUE, CosteMejorMomento = Integer.MAX_VALUE;
        int CosteMejorMomentoAnt = 0;

        //calculo de veces q mejora o no la reinicializacion
        int OEMejoraInte = 0, OEnoMejoraInte = 0, OEMejoraDive = 0, OEnoMejoraDive = 0, osc = 0;
        Solution posmejorPeores = new Solution(tam);
        Solution aux2 = new Solution(tam);
        //memorias a corto y largo plazo

        //memoria de frecuencias
        int[][] memFrec = new int[tam][tam];

        for (int i = 0; i < tam; i++)
            for (int j = 0; j < tam; j++)
                memFrec[i][j] = 0;

        //lista tabu explicita (solución entera)
        ArrayList<Solution> lTabu = new ArrayList<>();

        //metemos la solucion inicial en tabu
        lTabu.add(SolActual);
        //lista tabu implicita (movimientos)
        int[][] lTabu2 = new int[tam][tam];

        //dlb y vectores de apoyo
        int[] dlb = new int[tam];

        Solution mejorPeores = new Solution(tam);
        Solution SolGlobal = new Solution(tam);
        Solution nuevaSol = new Solution(tam);

        //inicializo la dlb
        dlbPorcent(dlb,tam,porcentaje);

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
            int filuni = 0, colpos = 0;
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
                        Solution newSol = new Solution(SolActual);
                        newSol = swapSolution(newSol,i,j);
                        //boolean iguales = Arrays.equals(SolActual.getSolutionList(), newSol.getSolutionList());
                        //System.out.println("iguales: " + iguales);
                        for (int l = 0; l < lTabu.size(); l++) {
                            if (lTabu.get(l).getSolutionList() == newSol.getSolutionList()) {
                                tabu = true;  //esta en lista tabu
                                System.out.println("es tabu: " + tabu);
                                break;
                            }
                        }
                        if (!tabu) {
                            //vemos si es Tabu con la segunda Lista Tabu
                            filuni = i;
                            colpos = j;
                            //System.out.println("no Tabu ");
                            if (filuni > colpos){
                                int temp= filuni;
                                filuni= colpos;
                                colpos = temp;
                            }

                                 //swap (filuni, colpos); //Para trabajar con la triangular superior

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

                                //System.out.println("solActual antes:" + SolActual);
                                SolActual = swapSolution(SolActual, i, j);

                                SolActual.setCost(Cost(flu,loc,SolActual.getSolutionList(),tam));
                                //System.out.println("solActual despu:" + SolActual);
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
                                    posmejorPeores.setCost(CosteMejorPeor);
                                    mejorPeores = swapSolution(posmejorPeores, i, j);
                                    mejorPeores.setCost(Cost(flu,loc, mejorPeores.getSolutionList(), tam));
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
                lTabu.add(SolActual);

            } else {
                //ACTUALIZO la memoria de frecuencias
                for (int k = 0; k < tam; k++) {
                    memFrec[k][mejorPeores.getSolutionList()[k]]++;
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

            if (filuni > colpos) {
                int temp = filuni;
                filuni= colpos;
                colpos = temp;
            }
                //swap(filuni, colpos);
            lTabu2[filuni][colpos] = tenenciaTabu;

            if (!mejora) {
                estancaCont++;

                if (CosteMejorPeor != Integer.MAX_VALUE) { //evita posibles dlb nuevas con todo 1's
                    CosteActual = CosteMejorPeor;
                    SolActual = mejorPeores;
                }
                //CosteMejorPeor++;
                //Para los del viernes
                dlbPorcent(dlb, tam,porcentaje);

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
                int prob = rand.nextInt(1, 100);
                //mostrarmatriz(memFrec);

                if (prob <= Tabuprob) {
                    osc = 0;
                    menosVisitados(memFrec, nuevaSol);
                } else {
                    osc = 1;
                    masVisitados(memFrec, nuevaSol);
                }
                System.out.println("act solucion: " + SolActual + SolActual.getSolutionList().length);
                SolActual.setSolutionList(nuevaSol.getSolutionList());
                SolActual.setCost(Cost(flu, loc, SolActual.getSolutionList(), tam));
                CosteActual = SolActual.getCost();
                System.out.println("nue solucion: " + SolActual + SolActual.getSolutionList().length);
                CosteMejorMomentoAnt = 0;
                if (CosteActual < CGlobal) {
                    CGlobal = CosteActual;
                    SolGlobal = SolActual;
                }

                //iniciamos esta variable para los empeoramientos
                //CosteMejorMomentoAnt = 0;

                // Borramos la matriz de frecuencias
                for (int i = 0; i < tam; i++)
                    for (int j = 0; j < tam; j++)
                        memFrec[i][j] = 0;

                // Borramos la lista tabu


                for (int i = 0; i < tam - 1; i++)
                    for (int j = i + 1; j < tam; j++)
                        lTabu2[i][j] = 0;

                //reinicializamos la dlb


                dlbPorcent(dlb,tam,porcentaje);
            }

            System.out.println();
            System.out.println("estancamiento: " + estancaCont);
            System.out.println("Paso = " + iter);
            System.out.println("Coste Actual: " + CosteActual + SolActual);
            System.out.println("Coste MejorPeor: " + CosteMejorPeor + mejorPeores);
            System.out.println("Coste Mejor Global: " + CGlobal + SolGlobal);

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
