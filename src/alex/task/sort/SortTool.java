package alex.task.sort;

import java.io.*;
import java.rmi.server.ExportException;
import java.util.*;

public class SortTool {
    private String inputFile;
    private String outputFile;
    private Mode flag;
    private int iNumber;
    private BufferedReader fileReader;
    private BufferedWriter fileWriter;
    private ArrayList<String> strings;
    private HashMap<String, Integer> counter;

    public Mode getFlag() {
        return this.flag;
    }
    public void setFlag(Mode flag) {
        this.flag = flag;
    }
    public int getNumber(){
        return iNumber;
    }
    public void setNumber(int iNumber) {
        this.iNumber = iNumber;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
    
    SortTool(String input, String output) {
        this.inputFile = input;
        this.outputFile = output;
        strings = new ArrayList<>();
        counter = new HashMap<>();
    }
    SortTool(String[] args) throws IllegalArgumentException{
        this.validateArgs(args);
        this.inputFile = args[0];
        this.outputFile = args[1];
        strings = new ArrayList<>();
        counter = new HashMap<>();
        switch (args[2]) {
            case "0":
                this.flag = Mode.Alphabet;
                break;
            case "1":
                this.flag = Mode.Length;
                break;
            case "2":
                this.flag = Mode.Word;
                this.iNumber = Integer.parseInt(args[3]);
                break;
        }
    }
    public void validateArgs(String[] args) throws IllegalArgumentException{
        if (args.length != 3 && args.length != 4)
            throw new IllegalArgumentException("Wrong number of command arguments");
        File input = new File(args[0]);
        if (!input.exists()) {
            throw new IllegalArgumentException("Wrong input file path");
        }
        int mode = Integer.parseInt(args[2]);
        if (mode == 0 || mode == 1) {
            if(args.length != 3)
                throw new IllegalArgumentException("Wrong number of command arguments");
        } else if (mode == 2) {
            if (args.length != 4)
                throw new IllegalArgumentException("Wrong number of command arguments");
            int iNumber = Integer.parseInt(args[3]);
            if (iNumber <= 0)
                throw new IllegalArgumentException("Fourth argument must be positive");
        } else {
            throw new IllegalArgumentException("Third argument must be in {0, 1, 2}");
        }
    }
    public void createStreams() throws IOException {
        fileReader = new BufferedReader(new FileReader(inputFile));
        fileWriter = new BufferedWriter(new FileWriter(outputFile));
    }
    public void closeStreams() throws IOException {
        fileReader.close();
        fileWriter.close();
    }
    public void readFile() throws IOException, IllegalArgumentException {
        String newLine;
        while((newLine = fileReader.readLine()) != null) {
            if(newLine.split(" ").length < iNumber)
                throw new IllegalArgumentException("Fourth argument too big");
            if (counter.containsKey(newLine)) {
                Integer tmp = counter.get(newLine) + 1;
                counter.put(newLine, tmp);
            } else {
                counter.put(newLine, 1);
            }
            strings.add(newLine);
        }
    }
    public void writeFile() throws IOException {
        for (String s : strings) {
            fileWriter.write(s + " " + counter.get(s) + "\n");
        }
    }

    public void sort(){
        switch (flag) {
            case Alphabet:
                Collections.sort(strings);
                break;
            case Length:
                Comparator<String> comparatorLength = new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        int l1 = o1.length();
                        int l2 = o2.length();
                        return l1 - l2;
                    }
                };
                strings.sort(comparatorLength);
                break;
            case Word:
                Comparator<String> comparatorWord = new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        String[] arr1 = o1.split(" ");
                        String[] arr2 = o2.split(" ");
                        return arr1[iNumber - 1].compareTo(arr2[iNumber - 1]);
                    }
                };
                strings.sort(comparatorWord);
        }
    }
    public void execute() throws IOException, IllegalArgumentException {
        this.createStreams();
        this.readFile();
        this.sort();
        this.writeFile();
        this.closeStreams();
    }

    /**
     *
     * @param args:
     *            [0] input file
     *            [1] output file
     *            [2] Flag(0 - sort by alphabet; 1 - sort by length; 2 - sort by i-th word)
     *            [3] For 2-th flag; i-th word;
     *            Example:
     *                  SortTool C:\input.txt C:\output.txt 2 1
     */
    public static void main(String[] args) throws Exception {
        SortTool tool = new SortTool(args);
        tool.execute();
    }
}
