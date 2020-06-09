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

    public void setFlag(Mode flag) {
        this.flag = flag;
    }
    public void setNumber(int iNumber) {
        this.iNumber = iNumber;
    }
    SortTool(String input, String output){
        this.inputFile = input;
        this.outputFile = output;
        strings = new ArrayList<>();
        counter = new HashMap<>();
    }
    SortTool(String[] args) {
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
    public void createStreams() throws Exception {
        fileReader = new BufferedReader(new FileReader(inputFile));
        fileWriter = new BufferedWriter(new FileWriter(outputFile));
    }
    public void closeStreams()throws Exception {
        fileReader.close();
        fileWriter.close();
    }
    public void readFile() throws Exception {
        String newLine;
        while((newLine = fileReader.readLine()) != null) {
            if (counter.containsKey(newLine)) {
                Integer tmp = counter.get(newLine) + 1;
                counter.put(newLine, tmp);
            } else {
                counter.put(newLine, 1);
            }
            strings.add(newLine);
        }
    }
    public void writeFile() throws Exception {
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
                        String s1 = "";
                        String s2 = "";
                        if(arr1.length >= iNumber) {
                            s1 = arr1[iNumber - 1];
                        }
                        if(arr2.length >= iNumber) {
                            s2 = arr2[iNumber - 1];
                        }
                        return s1.compareTo(s2);
                    }
                };
                strings.sort(comparatorWord);
                break;
        }
    }
    public void execute() throws Exception{
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
