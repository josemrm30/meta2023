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

    public TabuSearch(Problem problem, int iterations, int seed,int Tabuprob, Logger log, int tenenciaTabu) {
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


    public void generaDLB25(int []dlb, int tam){
        for (int i = 0; i < tam; i++) {
            if (i < tam / 4)
                dlb[i] = rand.nextInt(0, 1);
            else
                dlb[i] = 1;
        }
        // y lo reorganizamos al azar
        int r;
        for (int i = tam - 1; i > 0; i--) {
            r = rand.nextInt(0, 1);
            //intercambio de elementos
            swapSolution(dlb, i, r);
        }
    }

    void masVisitados(int[][] mat, Solution provnuevaSol) {
        int[] nuevaSol = provnuevaSol.getSolutionList();
        int tam = nuevaSol.length;
        int mayor = Integer.MIN_VALUE;
        int pf = -1;
        int pc = -1;
        boolean[] marcaf = new boolean[tam];
        boolean[] marcac = new boolean[tam];

        for (int k = 0; k < tam; k++) {
            for (int i = 0; i < tam; i++) {
                if (!marcaf[i]) {
                    for (int j = 0; j < tam; j++) {
                        if (!marcac[j] && mat[i][j] >= mayor) {
                            mayor = mat[i][j];
                            pf = i;
                            pc = j;
                        }
                    }
                }
            }
            nuevaSol[pf] = pc;
            provnuevaSol.setSolutionList(nuevaSol);
            marcaf[pf] = true;
            marcac[pc] = true;
            mayor = Integer.MIN_VALUE;
        }
    }

    void menosVisitados(int[][] memfrec, Solution provnuevaSol) {
        int tam = provnuevaSol.getSize();
        int[] nuevaSol = provnuevaSol.getSolutionList();
        int menor = Integer.MAX_VALUE;
        int pf = -1;
        int pc = -1;
        boolean[] marcaf = new boolean[tam];
        boolean[] marcac = new boolean[tam];

        for (int k = 0; k < tam; k++) {
            for (int i = 0; i < tam; i++) {
                if (!marcaf[i]) {
                    for (int j = 0; j < tam; j++) {
                        if (!marcac[j] && memfrec[i][j] <= menor) {
                            menor = memfrec[i][j];
                            pf = i;
                            pc = j;
                        }
                    }
                }
            }
            nuevaSol[pf] = pc;
            provnuevaSol.setSolutionList(nuevaSol);
            marcaf[pf] = true;
            marcac[pc] = true;
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
        int[] Sol = actualSolution.getSolutionList();
        int temp = Sol[i];
        Sol[i] = Sol[j];
        Sol[j] = temp;
        Solution newSol = new Solution(problem.getMatrixSize());
        newSol.setSolutionList(Sol);
        return newSol;
    }

    public void swap(ArrayList<Solution> lTabu, int fil, int col) {
        int temp = lTabu.get(fil).getSolutionList()[col];
        lTabu.get(fil).getSolutionList()[col] = lTabu.get(col).getSolutionList()[fil];
        lTabu.get(col).getSolutionList()[fil] = temp;
    }


    void TabuSearch(int[][] flu, int[][] loc,
                    int tam, int evaluaciones, int tenenciaTabu, int estancamientos,
                    Solution SolActual) {

        int contadorTenencia = 0;
        int tipo, contador;
        //Calculamos el coste de la Solucion inicial
        int CosteActual = actualSolution.getCost();
        //costes de soluciones de apoyo
        int CosteMejorPeor, CGlobal = Integer.MAX_VALUE, CosteMejorMomento = Integer.MAX_VALUE;
        int CosteMejorMomentoAnt = 0;

        //calculo de veces q mejora o no la reinicializacion
        int OEMejoraI = 0, OEnoMejoraI = 0, OEMejoraD = 0, OEnoMejoraD = 0, osc = 0;
        Solution aux = new Solution(tam);
        Solution aux2 = new Solution(tam);
        //memorias a corto y largo plazo

        //memoria de frecuencias
        int[][] memFrec = new int[tam][tam];

        for (int i = 0; i < tam; i++)
            for (int j = 0; j < tam; j++)
                memFrec[i][j] = 0;

        //lista tabu explicita
        //list<vector<int> > lTabu;
        //lTabu.resize(tenenciaTabu-1,vector<int>(tam,0));
        ArrayList<Solution> lTabu = new ArrayList<>();


        //metemos la solucion inicial en tabu
        lTabu.add(actualSolution);
        //lista tabu implicita
        ArrayList<Solution> lTabu2 = new ArrayList<>();


        //dlb y vectores de apoyo
        int[] dlb = new int[tam];

        Solution mejorPeores = new Solution(tam);
        Solution SolGlobal = new Solution(tam);
        Solution nuevaSol = new Solution(tam);


        //inicializo la dlb
        for (int k = 0; k < tam; k++)
            dlb[k] = 0;

        int iter = 0;

        contador = 0;
        boolean mejora;
        int pos = rand.nextInt(0, tam - 1);//random para darle dinamismo inicialmente //PARA ANOTAR LA ULTIMA POSICIÓN DE INTERCAMBIO ANTERIOR


        CosteMejorPeor = Integer.MAX_VALUE;
        while (iter < evaluaciones) {
            iter++;

            mejora = false;

            //tipo=pos;     //SI NO HAY CARGA ALEATORIA ESTA OPCION DA EL MISMO RESULTADO AUN CAMBIANDO SEMILLA
            tipo = rand.nextInt(0, tam - 1);   //PRIMERA UNIDAD DE INTERCAMBIO ALEATORIA

            CosteMejorPeor = Integer.MAX_VALUE;  //cada iteracion
            int fil = -1, col = -1;
            //comenzar por el principio y llegar hasta el punto de partida
            for (int i = tipo, cont = 0; cont < tam && !mejora; i++, cont++) {
                if (i == tam) i = 0;  //para que cicle
                if (dlb[i] == 0) {
                    boolean improve_flag = false;

                    for (int j = i + 1, cont1 = 0; cont1 < tam && !mejora; j++, cont1++) {
                        //checkMove(i,j)
                        if (j == tam) j = 0;  //para que cicle

                        //vemos si es Tabu con la primera Lista Tabu
                        boolean tabu = false;
                        aux2 = SolActual;
                        swapSolution(aux2, i, j);

                        for (int l = 0; l < lTabu.size(); l++) {
                            if (lTabu.get(l) == aux2) {
                                tabu = true;  //esta en lista tabu
                                break;
                            }
                        }


                        if (!tabu) {
                            //vemos si es Tabu con la segunda Lista Tabu
                            fil = i;
                            col = j;
                            if (fil > col) swap(lTabu2, fil, col);
                            if (lTabu2.get(fil).getSolutionList()[col] > 0)
                                tabu = true;
                        }

                        //si no es Tabu
                        if (!tabu) {
                            //funcion de factorizacion para ver si mejora o no si lo intercambiaramos
                            int C = Factorization2Opt(flu, loc, tam, SolActual, CosteActual, i, j);
                            if (C < CosteActual) {
                                //iter++; //YA esta PUESTO ARRIBA
                                CosteActual = C;
                                swapSolution(SolActual, i, j);

                                fil = i;
                                col = j;  //me quedo el par de intercambio

                                dlb[i] = dlb[j] = 0;
                                pos = j;    //ULTIMA UNIDAD DE INTERCAMBIO
                                improve_flag = true;
                                mejora = true;
                            } else {
                                if (C < CosteMejorPeor) {  //ojo como actualiza
                                    CosteMejorPeor = C;
                                    aux.setSolutionList(SolActual.getSolutionList());
                                    swapSolution(aux, i, j);
                                    mejorPeores = aux;

                                    fil = i;
                                    col = j; //me quedo el par de intercambio
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
            for (int k = 0; k < tam - 1; k++) {
                for (int l = k + 1; l < tam; l++) {
                    if (lTabu2.get(k).getSolutionList()[l] > 0)
                        lTabu2.get(k).getSolutionList()[l]--;
                }
            }

            if (fil > col) swap(lTabu2, fil, col);
            lTabu2.get(fil).getSolutionList()[col] = tenenciaTabu;

            if (!mejora) {

                contador++;

                if (CosteMejorPeor != Integer.MAX_VALUE) { //evita posibles dlb nuevas con todo 1's
                    CosteActual = CosteMejorPeor;
                    SolActual = mejorPeores;
                }
                //CosteMejorPeor++;
                //Para los del viernes
                for (int i = 0; i < tam; i++) {
                    if (i < tam / 4)
                        dlb[i] = rand.nextInt(0, 1);
                    else
                        dlb[i] = 1;
                }
                // y lo reorganizamos al azar
                int r;
                for (int i = tam - 1; i > 0; i--) {
                    r = rand.nextInt(0, 1);
                    //intercambio de elementos
                    swapSolution(dlb, i, r);
                }

            } else {

                if (CosteMejorMomentoAnt > CosteActual) {  //Asi es la ultima forma que ha dicho
                    contador = 0;
                    CosteMejorMomentoAnt = CosteActual;
                } else
                    contador++;

                if (CosteActual < CGlobal) {
                    CGlobal = CosteActual;
                    SolGlobal = SolActual;

                }
            }

            if (contador == estancamientos) {
                System.out.println("** Reinicializo");
                if (osc == 0) {
                    if (CosteMejorMomento > CosteActual) {
                        CosteMejorMomento = CosteActual;
                        OEMejoraD++;

                    } else {
                        OEnoMejoraD++;
                    }
                } else {
                    if (CosteMejorMomento > CosteActual) {
                        CosteMejorMomento = CosteActual;
                        OEMejoraI++;

                    } else {
                        OEnoMejoraI++;
                    }
                }

                contador = 0;
                int prob = rand.nextInt(1, tam);
                //mostrarmatriz(memFrec);
                if (prob <= Tabuprob) {
                    osc = 0;
                    menosVisitados(memFrec, nuevaSol);
                } else {
                    osc = 1;
                    masVisitados(memFrec, nuevaSol);

                }

                actualSolution = nuevaSol;
                CosteActual = actualSolution.getCost();

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
                        lTabu2.get(i).getSolutionList()[j] = 0;

                //reinicializamos la dlb

                for (int i = 0; i < tam; i++) {
                    dlb[i] = 0;
                }

            }

            System.out.println();
            System.out.println("Paso = " + iter);
            System.out.println("Coste Actual: " + CosteActual);
            System.out.println("Coste MejorPeor: " + CosteMejorPeor);
            System.out.println("Coste Mejor Global: " + CGlobal);


        }

        System.out.println("MEJORAS-D: " + OEMejoraD + "NO MEJORAS-D: " + OEnoMejoraD);
        System.out.println("MEJORAS-I: " + OEMejoraI + "NO MEJORAS-I: " + OEnoMejoraI);

        SolActual = SolGlobal;
    }


}
