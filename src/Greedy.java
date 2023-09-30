/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * @author pedro
 */

import java.util.*;


public class Greedy {

    public int minorDist(ArrayList<Integer> vec, int size) {
        int pminor = 0;
        for (int i = 0; i < size; i++) {
            if (vec.get(i) < vec.get(pminor)) {
                pminor = i;
            }
        }

        vec.set(pminor, 99999);// marcamos poniendo un numero grande
        return pminor;

    }

    public int majorFlow(ArrayList<Integer> vec, int size) {
        int pmajor = 0;
        for (int i = 0; i < size; i++) {
            if (vec.get(i) > vec.get(pmajor)) {
                pmajor = i;
            }
        }

        vec.set(pmajor, -99999);// marcamos poniendo un numero pequeño
        return pmajor;
    }

    public void CreatePotentials(ArrayList<Integer> flowPotential, ArrayList<Integer> distPotential, int size, int flow[][], int loc[][]) {
        for (int i = 0; i < size; i++) {
            flowPotential.add(0);
            distPotential.add(0);
            for (int j = 0; j < size; j++) {
                flowPotential.set(i, flowPotential.get(i) + flow[i][j]);
                distPotential.set(i, distPotential.get(i) + loc[i][j]);
            }
            System.out.println("flow " + flowPotential.get(i) + " -- " + "dist " + distPotential.get(i));
        }

    }

    public void SoluGreedy(int[][] flow, int[][] loc, int size, int[] s) {
        int minorDist;
        int majorFlow;
        ArrayList<Integer> distPotential = new ArrayList<>();
        ArrayList<Integer> flowPotential = new ArrayList<>();


        CreatePotentials(flowPotential, distPotential, size, flow, loc);

        for (int i = 0; i < size; i++) {
            majorFlow = majorFlow(flowPotential, size);
            minorDist = minorDist(distPotential, size);

            s[majorFlow] = minorDist + 1;
        }
            int contador = 0;
        for (int i = 0; i < size; i++) {
            contador += flowPotential.get(i);
        }
        System.out.println(flowPotential.toString());
        System.out.println(distPotential.toString());
        System.out.println(Arrays.toString(s));
    }

    int Cost(int []s,  int [][]flow , int [][] loc, int size){
        int cost = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(i!= j)
                    cost += flow[i][j] * loc[s[i]][s[j]];
            }
        }
        return cost;
    }




}
