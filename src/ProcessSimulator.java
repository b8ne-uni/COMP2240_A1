import Scheduler.Process;

import java.io.*;
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
    private static String inputFile;
    private LinkedList<Process> processList;

    private void run() {
        // Init Processes
        processList = new LinkedList<>();

        // Begin by reading in processes from supplied file
        System.out.println("Reading in input file...");

        // Alter input to Project path
        inputFile = "./out/production/A1/" + inputFile;
        try {
            String input = readFile(inputFile, StandardCharsets.UTF_8);
            // Setup Pattern and Matcher
            String exp = "ID:[\\s]+(?<ID>[\\w]*)[\\s]Arrive:[\\s]+(?<Arrive>[\\d]+)[\\s]+ExecSize:[\\s]+(?<SIZE>[\\d]+)[\\s]+END";
            Pattern p = Pattern.compile(exp);
            Matcher m = p.matcher(input);

            // Iterate through each pattern found, creating Processes as we go
            while (m.find()) {
                processList.add(new Process(
                        m.group("ID"),
                        Integer.parseInt(m.group("Arrive")),
                        Integer.parseInt(m.group("SIZE"))
                ));
            }
            System.out.println("Finished reading input file...");
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }

        // Now processes are stored, we can run simulations

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
                inputFile = s;
            }

            // Run the dispatcher
            ProcessSimulator program = new ProcessSimulator();
            program.run();
        }
    }
}
