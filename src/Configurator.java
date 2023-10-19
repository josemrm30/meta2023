import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Configurator {
    ArrayList<String> files;
    ArrayList<String> algorithms;
    ArrayList<Long> seeds;
    ArrayList<String> solutions;
    private int iterations;
    private int Tabuprob;
    private int tenenciaTabu;
    private int estancamientos;
    Boolean consoleLog;

    public Configurator(String path) {
        files = new ArrayList<>();
        seeds = new ArrayList<>();
        algorithms = new ArrayList<>();
        solutions = new ArrayList<>();

        String line;
        FileReader f = null;
        try {
            f = new FileReader(path);
            BufferedReader b = new BufferedReader(f);

            while ((line = b.readLine()) != null) {
                String[] splited = line.split("=");

                switch (splited[0]) {
                    case "Files":
                        String[] v = splited[1].split(" ");
                        for (int i = 0; i < v.length; i++) {
                            files.add(v[i]);
                        }
                        break;
                    case "Seeds":
                        String[] vSeeds = splited[1].split(" ");
                        for (int i = 0; i < vSeeds.length; i++) {
                            seeds.add(Long.parseLong(vSeeds[i]));
                        }
                        break;
                    case "Algorithms":
                        String[] vAlgorithms = splited[1].split(" ");
                        for (int i = 0; i < vAlgorithms.length; i++) {
                            algorithms.add(vAlgorithms[i]);
                        }
                        break;
                    case "Iterations":
                        iterations = Integer.parseInt(splited[1]);
                        break;
                    case "TabuProb":
                        Tabuprob = Integer.parseInt(splited[1]);
                        break;
                    case "TenenciaTabu":
                        tenenciaTabu = Integer.parseInt(splited[1]);
                        break;
                    case "Estancamientos":
                        estancamientos = Integer.parseInt(splited[1]);
                        break;
                    case "ConsoleLog":
                        consoleLog = Boolean.parseBoolean(splited[1]);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + splited[0]);
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public ArrayList<String> getAlgorithms() {
        return algorithms;
    }

    public ArrayList<Long> getSeeds() {
        return seeds;
    }

    public int getIterations() {
        return iterations;
    }

    public int getTabuprob(){return Tabuprob;}

    public int getTenenciaTabu(){return tenenciaTabu;}

    public int getEstancamientos(){return estancamientos;}

}


