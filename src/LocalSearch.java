public class LocalSearch {

    /*private Solution initialSolution;
    public LocalSearch() {
        initialSolution = getInitialSolution();
    }*/

    public Solution getInitialSolution(int size, int[][] flow, int[][] distance) {
        Greedy greedy = new Greedy(size);
        return greedy.SoluGreedy(flow, distance, size);
    }

    public int[] swapSolution(int[] actualSolution, int i, int j) {
        int[] newSol = actualSolution;
        int temp = newSol[i];
        newSol[i] = newSol[j];
        newSol[j] = temp;
        return newSol;
    }

    public void LocalSolution(int[][] flow, int[][] loc, int size, int maxIterations, Solution actualSolution) {
        int[] dlb = new int[size];
        int iter = 0;
        boolean improvement = true;
        int pos = 0;
        int[] solutionList = actualSolution.getSolutionList();
        int actualCost = actualSolution.getCost();


        while (improvement && iter < maxIterations) {
            improvement = false;

            for (int i = pos; i < size - 1; i++) {
                if (dlb[i] == 0) {
                    for (int j = i + 1; j < size; j++) {
                        int[] newSolution = swapSolution(solutionList, i, j);
                        // Calcular el costo de la nueva solución
                        int newCost = Factorization2Opt(flow, loc, size, newSolution, actualCost, i, j);

                        // Si la nueva solución es mejor, actualizar solActual y resetear la DLB
                        if (newCost < actualCost) {
                            solutionList = newSolution;
                            actualCost = newCost;
                            dlb[i] = 0;
                            dlb[j] = 0;
                            pos = j;
                            improvement = true;
                            iter++;
                        } else {
                            dlb[i] = 1; // Marcar i como bloqueada en la DLB
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
            System.out.print(solutionList[i] + " ");
        }
    }


    //factorization function  with 2 elements
    private int Factorization2Opt(int[][] flow, int[][] loc, int tam, int[] actualSolution, int ActualCost, int r, int s) {
        for (int k = 0; k < tam; k++) {
            if (k != r && k != s) {
                ActualCost += flow[r][k] * (loc[actualSolution[s] - 1][actualSolution[k] - 1] - loc[actualSolution[r] - 1][actualSolution[k] - 1]) +
                        flow[s][k] * (loc[actualSolution[r] - 1][actualSolution[k] - 1] - loc[actualSolution[s] - 1][actualSolution[k] - 1]) +
                        flow[k][r] * (loc[actualSolution[k] - 1][actualSolution[s] - 1] - loc[actualSolution[k] - 1][actualSolution[r] - 1]) +
                        flow[k][s] * (loc[actualSolution[k] - 1][actualSolution[r] - 1] - loc[actualSolution[k] - 1][actualSolution[s] - 1]);
            }
        }
        return ActualCost;
    }
}
