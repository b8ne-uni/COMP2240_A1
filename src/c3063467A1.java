import Simulator.ProcessSimulator;

/**
 * Created by b8ne on 2/09/2016.
 */
public class c3063467A1 {

    /**
     * Main Java entry point
     * @param args
     */
    public static void main(String[] args) {
        // Pull input param and store statically
        if (args.length < 1) {
            System.out.println("Error: No input file provided. Please run again with a input param.");
        } else {
            String input = "";
            for (String s : args) {
                input = s;
            }

            // Run the dispatcher
            ProcessSimulator program = new ProcessSimulator();
            program.run(input);
        }
    }
}
