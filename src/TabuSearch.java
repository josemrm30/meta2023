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
    private int tabuTenure;
    private double percent;


    public int Cost(int[][] flow, int[][] loc, int[] sol,int size) {
        int cost = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    cost += flow[i][j] * loc[sol[i]][sol[j]];
                }
            }
        }
        return cost;
    }
    public TabuSearch(Problem problem, int iterations, long seed, int Tabuprob, Logger log, int tabuTenure,double percent) {
        this.problem = problem;
        this.iterations = iterations;
        this.Tabuprob = Tabuprob;
        this.tabuTenure = tabuTenure;
        this.log = log;
        this.percent = percent;
        rand = new Random(seed);
        actualSolution = getInitialSolution(problem);
    }

    public Solution getInitialSolution(Problem problem) {
        Greedy greedy = new Greedy(problem.getMatrixSize());
        return greedy.SoluGreedy(problem.getFlowMatrix(), problem.getDistMatrix());
    }

    void mostVisited(int[][] mat, Solution provnuevaSol) {
        int[] nuevaSol = provnuevaSol.getSolutionList();
        int size = nuevaSol.length;
        int major = Integer.MIN_VALUE;
        int rowunit = 0;
        int columloc = 0;
        boolean[] markrowuni = new boolean[size];
        boolean[] markcolumloc = new boolean[size];
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                if (!markrowuni[i]) {
                    for (int j = 0; j < size; j++) {
                        if (!markcolumloc[j] && mat[i][j] >= major) {
                            major = mat[i][j];
                            rowunit = i;
                            columloc = j;
                        }
                    }
                }
            }
            nuevaSol[rowunit] = columloc;
            markrowuni[rowunit] = true;
            markcolumloc[columloc] = true;
            major = Integer.MIN_VALUE;
        }
    }

    void lessVisited(int[][] memfrec, Solution provNewSol) {
        int size = provNewSol.getSolutionList().length;
        int[] newSol = provNewSol.getSolutionList();
        int minor = Integer.MAX_VALUE;
        int rowunit = 0;
        int columloc = 0;
        boolean[] markrowunit = new boolean[size];
        boolean[] markcolumloc = new boolean[size];

        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                if (!markrowunit[i]) {
                    for (int j = 0; j < size; j++) {
                        if (!markcolumloc[j] && memfrec[i][j] <= minor) {
                            minor = memfrec[i][j];
                            rowunit = i;
                            columloc = j;
                        }
                    }
                }
            }
            newSol[rowunit] = columloc;
            markrowunit[rowunit] = true;
            markcolumloc[columloc] = true;
            minor = Integer.MAX_VALUE;
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

    public void dlbPercent(int[] dlb, int tam, double percent) {
        double random;
        int countzero = 0;
        double cantidad = tam * percent;
        for (int i = 0; i < tam; i++) {
            if (countzero <= cantidad) {
                countzero++;
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

    int TabuSearch(int[][] flow, int[][] loc,
                   int size, int evaluations, int tabuTenure, int blockMax,
                   Solution ActualSolution) {

        int blockCount;
        //coste de la Solucion inicial (Greedy)
        int actualCost = ActualSolution.getCost();

        //costes de soluciones de apoyo
        int costBestworst = Integer.MAX_VALUE, globalCost = Integer.MAX_VALUE, bestMomentCost = Integer.MAX_VALUE;
        int prevBestCost = 0;

        Solution auxBestWorst = new Solution(size);
        Solution aux2 = new Solution(size);
        //memorias a corto y largo plazo

        //memoria de frecuencias
        int[][] memFrec = new int[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                memFrec[i][j] = 0;

        //lista tabu explicita (solución entera)
        ArrayList<Solution> tabuList = new ArrayList<>();

        //metemos la solucion inicial en tabu
        tabuList.add(ActualSolution);
        //lista tabu implicita (movimientos)
        int[][] tabuList2 = new int[size][size];

        //dlb y vectores de apoyo
        int[] dlb = new int[size];

        Solution bestWorst = new Solution(size);
        Solution globalSol = new Solution(size);
        Solution newSol = new Solution(size);

        //inicializo la dlb
        dlbPercent(dlb,size, percent);

        int iter = 0;

        blockCount = 0;
        boolean improvement;
        int pos = rand.nextInt(0, size - 1);//primera posicion aleatoria

        while (iter < evaluations) {
            iter++;

            improvement = false;

            costBestworst = Integer.MAX_VALUE;  //cada iteracion
            int rowuni = 0, colpos = 0;
            //comenzar por el principio y llegar hasta el punto de partida
            for (int i = pos, cont = 0; cont < size && !improvement; i++, cont++) {
                if (i == size) i = 0;  //para que cicle
                if (dlb[i] == 0) {
                    boolean improve_flag = false;

                    for (int j = i + 1, count1 = 0; count1 < size && !improvement; j++, count1++) {
                        //checkMove(i,j)
                        if (j == size) j = 0;  //para que cicle

                        //vemos si es Tabu con la primera Lista Tabu
                        boolean tabu = false;
                        Solution newSol = new Solution(ActualSolution);
                        newSol = swapSolution(newSol,i,j);
                        //boolean iguales = Arrays.equals(SolActual.getSolutionList(), newSol.getSolutionList());
                        //System.out.println("iguales: " + iguales);
                        for (int l = 0; l < tabuList.size(); l++) {
                            if (tabuList.get(l).getSolutionList() == newSol.getSolutionList()) {
                                tabu = true;  //esta en lista tabu
                                System.out.println("es tabu: " + tabu);
                                break;
                            }
                        }
                        if (!tabu) {

                            rowuni = i;
                            colpos = j;
                            //trabajamos solo con la triangular superior
                            if (rowuni > colpos){
                                int temp= rowuni;
                                rowuni= colpos;
                                colpos = temp;
                            }



                            if (tabuList2[rowuni][colpos] > 0)
                                tabu = true;
                        }

                        //si no es Tabu
                        if (!tabu) {
                            //funcion de factorizacion para ver si mejora o no si lo intercambiaramos
                            int cost = Factorization2Opt(flow, loc, size, ActualSolution, actualCost, i, j);
                            if (cost < actualCost) {
                                //iter++; //YA esta PUESTO ARRIBA
                                actualCost = cost;

                                //System.out.println("solActual antes:" + SolActual);
                                ActualSolution = swapSolution(ActualSolution, i, j);

                                ActualSolution.setCost(Cost(flow,loc,ActualSolution.getSolutionList(),size));
                                //System.out.println("solActual despu:" + SolActual);
                                rowuni = i;
                                colpos = j;  //me quedo el par de intercambio

                                dlb[i] = dlb[j] = 0;
                                pos = j;    //ULTIMA UNIDAD DE INTERCAMBIO
                                improve_flag = true;
                                improvement = true;
                            } else {
                                if (cost < costBestworst) {  //ojo como actualiza
                                    //System.out.println("actualiza mejor peor");
                                    costBestworst = cost;
                                    auxBestWorst.setSolutionList(ActualSolution.getSolutionList());
                                    auxBestWorst.setCost(costBestworst);
                                    bestWorst = swapSolution(auxBestWorst, i, j);
                                    bestWorst.setCost(Cost(flow,loc, bestWorst.getSolutionList(), size));
                                    rowuni = i;
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
            //UNA VEZ VISITO  EL VECINDARIO O HAY MEJORA
            //TENEMOS UN MOVIMIENTO Y ACTUALIZAMOS MEMORIAS

            if (improvement) {
                //ACTUALIZO la memoria de frecuencias
                for (int k = 0; k < size; k++) {
                    memFrec[k][ActualSolution.getSolutionList()[k]]++;
                }
                tabuList.add(ActualSolution);
            } else {
                //ACTUALIZO la memoria de frecuencias
                for (int k = 0; k < size; k++) {
                    memFrec[k][bestWorst.getSolutionList()[k]]++;
                }
                tabuList.add(bestWorst);
            }

            //ACTUALIZO tabu2 con pares de intercambio
            //solo recorro la triangular superior para ir más rápido
            for (int k = 0; k < size - 1; k++) {
                for (int l = k + 1; l < size; l++) {
                    if (tabuList2[k][l] > 0)
                        tabuList2[k][l]--;
                }
            }


            if (rowuni > colpos) {
                int temp = rowuni;
                rowuni= colpos;
                colpos = temp;
            }

            tabuList2[rowuni][colpos] = tabuTenure;

            if (!improvement) {
                blockCount++;

                if (costBestworst != Integer.MAX_VALUE) { //evita dlb completa de 1s
                    actualCost = costBestworst;
                    ActualSolution = bestWorst;
                }
                //CosteMejorPeor++;
                //Reinicializamos la dlb con un porcentaje de 0s pasado por fichero
                dlbPercent(dlb, size, percent);

            } else {

                if (prevBestCost > actualCost) {
                    blockCount = 0;
                    prevBestCost = actualCost;
                } else
                    blockCount++;

                if (actualCost < globalCost) {
                    globalCost = actualCost;
                    globalSol = ActualSolution;
                }
            }

            if (blockCount == blockMax) {
                System.out.println("** Reboot");

                blockCount = 0;
                int prob = rand.nextInt(1, 100);

                if (prob <= Tabuprob) {
                    osc = 0;
                    lessVisited(memFrec, newSol);
                } else {
                    osc = 1;
                    mostVisited(memFrec, newSol);
                }
                prevBestCost = Integer.MIN_VALUE;
                System.out.println("actual solution: " + ActualSolution + ActualSolution.getSolutionList().length);
                ActualSolution.setSolutionList(newSol.getSolutionList());
                ActualSolution.setCost(Cost(flow, loc, ActualSolution.getSolutionList(), size));
                actualCost = ActualSolution.getCost();
                System.out.println("new solution: " + ActualSolution + ActualSolution.getSolutionList().length);

                if (actualCost < globalCost) {
                    globalCost = actualCost;
                    globalSol = ActualSolution;
                }

                // Borramos la matriz de frecuencias
                for (int i = 0; i < size; i++)
                    for (int j = 0; j < size; j++)
                        memFrec[i][j] = 0;

                // Borramos la lista tabu


                for (int i = 0; i < size - 1; i++)
                    for (int j = i + 1; j < size; j++)
                        tabuList2[i][j] = 0;

                //reinicializamos la dlb
                dlbPercent(dlb,size, percent);
            }

            System.out.println();
            System.out.println("blockage: " + blockCount);
            System.out.println("Iteration: " + iter);
            System.out.println("actual cost: " + actualCost + "actual solution: " + ActualSolution);
            System.out.println("Cost Best Worst: " + costBestworst + "Best Worst: " + bestWorst);
            System.out.println("Best Global Cost: " + globalCost+ "Global Solution: " + globalSol);

            System.out.println("dlb actual: " + Arrays.toString(dlb));
        }

        ActualSolution = globalSol;
        return globalCost;
    }
}
