
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author pedro
 */
public class Solution {
    
    ArrayList<Integer> seeds;
    int size;
    ArrayList<Integer> solution;

    public ArrayList<Integer> getSeeds() {
        return seeds;
    }
    public int getSize() {
        return size;
    }

    public ArrayList<Integer> getSolution() {
        return solution;
    }

    public Solution(String path) {
        solution = new ArrayList<>();
        seeds = new ArrayList<>();
        String line;
        FileReader f = null;
        try {
            f = new FileReader(path);
            BufferedReader b = new BufferedReader(f);
            int linenumb=0;
            while ((line = b.readLine()) != null) {
                try {
                        if(linenumb == 0){
                            String[] splited = line.split("  ");
                            size = Integer.parseInt(splited[0]);
                            String[] splitedSeed = line.split(" ");
                            for(int i=0; i < size; i++){
                                seeds.add(Integer.parseInt(splitedSeed[i]));
                            }

                        }else{
                            String[] splitedSol = line.split(" ");
                            for(int i=0; i < size; i++){
                                solution.add(Integer.parseInt(splitedSol[i]));
                            }
                        }
                        linenumb++;
                    } catch (NumberFormatException ex) {
                        
                    }
                
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    
    
}
