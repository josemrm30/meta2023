import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Configurator {
    ArrayList<String> files;
    ArrayList<String> algorithms;
    ArrayList<Long> seeds;
    ArrayList<String> solutions;
    private int iterations;
    private int tabuProb;
    private int tenenciaTabu;
    private int estancamientos;
    private double porcentaje;
    private double porcentajeIls;
    private int iteracionesIls;
    Boolean consoleLog;

    public Configurator(String path) throws IOException {
        files = new ArrayList<>();
        seeds = new ArrayList<>();
        algorithms = new ArrayList<>();
        solutions = new ArrayList<>();

        String line;
        FileReader f;
        f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);

        while ((line = b.readLine()) != null) {
            String[] splited = line.split("=");

            switch (splited[0]) {
                case "Files":
                    String[] v = splited[1].split(" ");
                    files.addAll(Arrays.asList(v));
                    break;
                case "Seeds":
                    String[] vSeeds = splited[1].split(" ");
                    for (String vSeed : vSeeds) {
                        seeds.add(Long.parseLong(vSeed));
                    }
                    break;
                case "Algorithms":
                    String[] vAlgorithms = splited[1].split(" ");
                    Collections.addAll(algorithms, vAlgorithms);
                    break;
                case "Iterations":
                    iterations = Integer.parseInt(splited[1]);
                    break;
                case "ConsoleLog":
                    consoleLog = Boolean.parseBoolean(splited[1]);
                    break;
                case "TabuProb":
                    tabuProb = Integer.parseInt(splited[1]);
                    break;
                case "TabuTenure":
                    tenenciaTabu = Integer.parseInt(splited[1]);
                    break;
                case "Blockage":
                    estancamientos = Integer.parseInt(splited[1]);
                    break;
                case "Percent":
                    porcentaje = Double.parseDouble(splited[1]);
                    break;
                case "PercentIls":
                    porcentajeIls = Double.parseDouble(splited[1]);
                    break;
                case "IterationsIls":
                    iteracionesIls = Integer.parseInt(splited[1]);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + splited[0]);
            }
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

    public int getTabuprob(){return tabuProb;}

    public int getTenenciaTabu(){return tenenciaTabu;}

    public int getEstancamientos(){return estancamientos;}

    public double getPorcentaje(){return porcentaje;}

    public double getPorcentajeIls(){return porcentajeIls;}

    public int getIteracionesIls(){return iteracionesIls;}
}


