package Simulator;

import Dispatcher.Dispatcher;
import Scheduler.Process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ben on 23/08/2016.
 */

public class ProcessSimulator {
    private LinkedList<Process> processList;
    private Dispatcher dispatcher;

    private static String INPUT_FILE;
    public static BufferedWriter OUTPUT_FILE;

    private void run() {
        // Init Processes and dispatcher
        dispatcher = new Dispatcher();
        processList = new LinkedList<>();

        // Begin by reading in processes from supplied file
        System.out.println("Reading in input file...");

        // Setup Output file
        try {
            String strippedName = INPUT_FILE.substring(0, INPUT_FILE.lastIndexOf("."));
            OUTPUT_FILE = new BufferedWriter(new FileWriter("./out/production/A1/" + strippedName + "_output.txt"));
        } catch (IOException ex) {
            System.out.println("Unable to generate output file.");
        }

        // Alter input to Project path
        // @TODO: This is for intelliJ, check in terminal
        INPUT_FILE = "./out/production/A1/" + INPUT_FILE;
        try {
            String input = readFile(INPUT_FILE, StandardCharsets.UTF_8);
            // Setup Pattern and Matcher
            // Dispatcher
            String exp = "DISP:[\\s]+(?<DISP>[\\d]+)";
            Pattern p = Pattern.compile(exp);
            Matcher m = p.matcher(input);

            // Processes
            String exp2 = "ID:[\\s]+(?<ID>[\\w]*)[\\s]Arrive:[\\s]+(?<Arrive>[\\d]+)[\\s]+ExecSize:[\\s]+(?<SIZE>[\\d]+)[\\s]+END";
            Pattern p2 = Pattern.compile(exp2);
            Matcher m2 = p2.matcher(input);

            // Find the Dispatcher time
            while(m.find()) {
                dispatcher.setDispatchTime(Integer.parseInt(m.group("DISP")));
            }

            // Iterate through each pattern found, creating Processes as we go
            while (m2.find()) {
                processList.add(new Process(
                        m2.group("ID"),
                        Integer.parseInt(m2.group("Arrive")),
                        Integer.parseInt(m2.group("SIZE"))
                ));
            }
            System.out.println("Finished reading input file...");
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }

        // Store send processes to Dispatcher
        dispatcher.setProcesses(processList);
        // Now processes are stored, we can run simulations
        dispatcher.runDispatcher();
    }

    /**
     * Private helper function to read in file
     * @param path
     * @param encoding
     * @return
     * @throws IOException
     */
    private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    /**
     * Main Java entry point
     * @param args
     */
    public static void main(String[] args) {
        // Pull input param and store statically
        if (args.length < 1) {
            System.out.println("Error: No input file provided.");
        } else {
            for (String s : args) {
                INPUT_FILE = s;
            }

            // Run the dispatcher
            ProcessSimulator program = new ProcessSimulator();
            program.run();
        }
    }
}
