/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author pedro
 */

import java.util.*;
 

public class Greedy {
 
public int minorDist(ArrayList<Integer> vec,int size){
    int pminor = 0; 
    for(int i=0; i < size; i++){
        if(vec.get(i) < vec.get(pminor)){
            pminor = i;
        }
    }
    
    vec.set(pminor, 99999);// marcamaos poniendo un nmmero grande
    return pminor;
        
    }

public int majorFlow(ArrayList<Integer> vec,int size){
    int pmajor = 0; 
    for(int i=0; i < size; i++){
        if(vec.get(i) > vec.get(pmajor)){
            pmajor = i;
        }
    }
    
    vec.set(pmajor, -99999);// marcamaos poniendo un nmmero pequeño
    return pmajor;  
    } 

public void CreatePotentials(ArrayList<Integer> flowpot,ArrayList<Integer> dispot, int size, int flow[][], int loc[][]){
    for(int i=0; i< size; i++){
        
        flowpot.set(i, 0);
        dispot.set(i, 0);
        for(int j=0; j< size; j++){
            flowpot.set(i, flowpot.get(i) + flow[i][j]);
            dispot.set(i, dispot.get(i) + loc[i][j]);
        }
        
        System.out.println(flowpot.get(i) + " -- " + dispot.get(i));
    }
    
}

public void SoluGreedy(int[][] flow, int[][] loc, int size, ArrayList<Integer> s){
        int minord;
        int majorf;
        ArrayList<Integer> dispot = new ArrayList<>();
        ArrayList<Integer> flowpot = new ArrayList<>();
        
        
        CreatePotentials(flowpot, dispot, size, flow, loc);
        
        for(int i=0; i< size; i++){
            majorf = majorFlow(flowpot, size);
            minord = minorDist(dispot, size);
            
            s.set(majorf,minord );
        }
    
    } 

}
