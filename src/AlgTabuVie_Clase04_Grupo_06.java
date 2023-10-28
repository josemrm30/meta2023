import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlgTabuVie_Clase04_Grupo_06 {
    private final Random rand;
    private final Logger log;
    private final int Tabuprob;
    private final double probabilitySet;
    private final double percent;


    public int Cost(int[][] flow, int[][] loc, int[] sol, int size) {
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

    public AlgTabuVie_Clase04_Grupo_06(long seed, int Tabuprob, Logger log, double percent, double probabilitySet) {
        this.Tabuprob = Tabuprob;
        this.probabilitySet = probabilitySet;
        this.log = log;
        this.percent = percent;
        rand = new Random(seed);
    }

    public Solution getInitialSolution(Problem problem) {
        AlgGreedy_Clase04_Grupo_06 greedy = new AlgGreedy_Clase04_Grupo_06(problem.getMatrixSize());
        return greedy.SoluGreedy(problem.getFlowMatrix(), problem.getDistMatrix());
    }

    void mostVisited(int[][] mat, Solution provnuevaSol) {
        int[] newSol = provnuevaSol.getSolutionList();
        int size = newSol.length;
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
            newSol[rowunit] = columloc;
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

    public Solution swapSolution(Solution actualSolution, int i, int j) {
        int temp = actualSolution.getSolutionList()[i];
        actualSolution.getSolutionList()[i] = actualSolution.getSolutionList()[j];
        actualSolution.getSolutionList()[j] = temp;
        return actualSolution;
    }

    public void dlbPercent(int[] dlb, int tam, double percent) {
        double random;
        int countzero = 0;
        double cantidad = tam * percent;
        for (int i = 0; i < tam; i++) {
            if (countzero <= cantidad) {
                countzero++;
                random = rand.nextDouble();
                if (random < probabilitySet) {
                    dlb[i] = 0;
                } else {
                    dlb[i] = 1;
                }
            } else {
                dlb[i] = 1;
            }
        }
        for (int i = tam - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = dlb[i];
            dlb[i] = dlb[j];
            dlb[j] = temp;
        }
    }

    int search(int[][] flow, int[][] loc,
               int size, int evaluations, int tabuTenure, int blockMax,
               Solution ActualSolution) {

        int blockCount;
        int actualCost = ActualSolution.getCost();

        int costBestworst, globalCost = Integer.MAX_VALUE;
        int prevBestCost = 0;

        Solution auxBestWorst = new Solution(size);

        int[][] memFrec = new int[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                memFrec[i][j] = 0;

        ArrayList<Solution> tabuList = new ArrayList<>();

        tabuList.add(ActualSolution);
        int[][] tabuList2 = new int[size][size];

        int[] dlb = new int[size];

        Solution bestWorst = new Solution(size);
        Solution globalSol = new Solution(size);
        Solution newSol = new Solution(size);

        dlbPercent(dlb, size, percent);

        int iter = 0;

        blockCount = 0;
        boolean improvement;
        int pos = rand.nextInt(0, size - 1);

        while (iter < evaluations) {
            iter++;
            improvement = false;

            costBestworst = Integer.MAX_VALUE;
            int rowuni = 0, colpos = 0;
            for (int i = pos, cont = 0; cont < size && !improvement; i++, cont++) {
                if (i == size) i = 0;
                if (dlb[i] == 0) {
                    boolean improve_flag = false;
                    for (int j = i + 1, count1 = 0; count1 < size && !improvement; j++, count1++) {
                        if (j == size) {
                            j = 0;
                        }

                        boolean tabu = false;
                        newSol = new Solution(ActualSolution);
                        newSol = swapSolution(newSol, i, j);
                        for (Solution solution : tabuList) {
                            if (solution.getSolutionList() == newSol.getSolutionList()) {
                                tabu = true;
                                break;
                            }
                        }
                        if (!tabu) {
                            rowuni = i;
                            colpos = j;
                            if (rowuni > colpos) {
                                int temp = rowuni;
                                rowuni = colpos;
                                colpos = temp;
                            }
                            if (tabuList2[rowuni][colpos] > 0)
                                tabu = true;
                        }
                        if (!tabu) {
                            int cost = Factorization2Opt(flow, loc, size, ActualSolution, actualCost, i, j);
                            if (cost < actualCost) {
                                actualCost = cost;

                                ActualSolution = swapSolution(ActualSolution, i, j);
                                ActualSolution.setCost(Cost(flow, loc, ActualSolution.getSolutionList(), size));

                                rowuni = i;
                                colpos = j;

                                dlb[i] = dlb[j] = 0;
                                pos = j;
                                improve_flag = true;
                                improvement = true;
                            } else {
                                if (cost < costBestworst) {
                                    costBestworst = cost;
                                    auxBestWorst.setSolutionList(ActualSolution.getSolutionList());
                                    auxBestWorst.setCost(costBestworst);
                                    bestWorst = swapSolution(auxBestWorst, i, j);
                                    bestWorst.setCost(Cost(flow, loc, bestWorst.getSolutionList(), size));
                                    rowuni = i;
                                    colpos = j;
                                }
                            }
                        }
                    }
                    if (!improve_flag) {
                        dlb[i] = 1;
                    }
                }
            }

            if (improvement) {
                for (int k = 0; k < size; k++) {
                    memFrec[k][ActualSolution.getSolutionList()[k]]++;
                }
                tabuList.add(ActualSolution);
            } else {
                for (int k = 0; k < size; k++) {
                    memFrec[k][bestWorst.getSolutionList()[k]]++;
                }
                tabuList.add(bestWorst);
            }
            for (int k = 0; k < size - 1; k++) {
                for (int l = k + 1; l < size; l++) {
                    if (tabuList2[k][l] > 0)
                        tabuList2[k][l]--;
                }
            }

            if (rowuni > colpos) {
                int temp = rowuni;
                rowuni = colpos;
                colpos = temp;
            }

            tabuList2[rowuni][colpos] = tabuTenure;

            if (!improvement) {
                blockCount++;

                if (costBestworst != Integer.MAX_VALUE) {
                    actualCost = costBestworst;
                    ActualSolution = bestWorst;
                }
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
                log.log(Level.INFO, "** Reboot");

                blockCount = 0;
                int prob = rand.nextInt(1, 100);

                if (prob <= Tabuprob) {
                    lessVisited(memFrec, newSol);
                } else {
                    mostVisited(memFrec, newSol);
                }
                prevBestCost = 0;
                log.log(Level.INFO, "Actual solution: " + ActualSolution);
                ActualSolution.setSolutionList(newSol.getSolutionList());
                ActualSolution.setCost(Cost(flow, loc, ActualSolution.getSolutionList(), size));
                actualCost = ActualSolution.getCost();
                log.log(Level.INFO, "New solution: " + ActualSolution);

                if (actualCost < globalCost) {
                    globalCost = actualCost;
                    globalSol = ActualSolution;
                }

                for (int i = 0; i < size; i++)
                    for (int j = 0; j < size; j++)
                        memFrec[i][j] = 0;

                for (int i = 0; i < size - 1; i++)
                    for (int j = i + 1; j < size; j++)
                        tabuList2[i][j] = 0;

                dlbPercent(dlb, size, percent);
            }
            log.log(Level.INFO, "Blockage: " + blockCount);
            log.log(Level.INFO, "Iteration: " + iter);
            log.log(Level.INFO, "Actual cost: " + actualCost + " Actual solution: " + ActualSolution);
            log.log(Level.INFO, "Cost Best Worst: " + costBestworst + " Best Worst: " + bestWorst);
            log.log(Level.INFO, "Best Global Cost: " + globalCost + " Global Solution: " + globalSol);
            log.log(Level.INFO, "Actual dlb: " + Arrays.toString(dlb));
        }

        return globalCost;
    }
}
