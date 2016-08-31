package Dispatcher;

import Scheduler.Process;
import Scheduler.RR.RRScheduler;
import Scheduler.Scheduler;
import Scheduler.FCFS.FCFSScheduler;
import Simulator.ProcessSimulator;

import java.io.IOException;
import java.util.*;

/**
 * Created by ben on 31/08/2016.
 */
public class Dispatcher {
    private ArrayDeque<Scheduler> schedulers;
    private LinkedList<Process> processes;

    public static int DISPATCH_TIME;

    public Dispatcher() {
        // Init schedulers
        this.schedulers = new ArrayDeque<>();
        Scheduler fcfs = new FCFSScheduler();
        Scheduler rr = new RRScheduler();

        this.schedulers.addLast(fcfs);
        this.schedulers.addLast(rr);
    }

    public void setProcesses(LinkedList<Process> processes) {
        this.processes = processes;
    }

    public void setDispatchTime(int dispatchTime) {
        this.DISPATCH_TIME = dispatchTime;
    }

    /**
     * Performs simulations on schedulers
     */
    public void runDispatcher() {
        for (Scheduler s : schedulers) {
            s.startScheduler();
            while (s.getIsRunning()) {
                // If finished, run finishing code
                if (s.getCompletedProcessesSize() == processes.size()) {
                    s.stopScheduler();
                } else {
                    // See if any processes are starting on next tick
                    // Need to check for multiple process, if so prioritise by ID
                    // NOTE: This is overkill, but best solution would be using lambda functions in Java 8
                    LinkedList<Process> startingProcesses = new LinkedList<>();
                    for (Process p : processes) {
                        if (p.getArriveTime() == s.getCurrentTick() + 1) {
                            startingProcesses.add(new Process(p));
                        }
                    }
                    Collections.sort(startingProcesses);
                    while (!startingProcesses.isEmpty()) {
                        s.processIncoming(startingProcesses.removeFirst());
                    }
                    // Go to next tick
                    s.setCurrentTick(s.getCurrentTick() + 1);
                    s.onTick();
                }
            }
            s.onStop();
        }
        this.printSummary();
    }

    /**
     * Outputs Dispatcher summary
     */
    public void printSummary() {
        try {
            String header = String.format("%-9s%23s%26s", "Algorithm", "Average Waiting Time", "Average Turnaround Time");
            ProcessSimulator.OUTPUT_FILE.write(header + "\n");
            System.out.println(header);
            for (Scheduler s : schedulers) {
                String summary = String.format("%-9s%23.2f%26.2f", s.schedulerName(), s.getAverageWaitTime(), s.getAverageTurnaroundTime());
                ProcessSimulator.OUTPUT_FILE.write(summary);
                System.out.println(summary);
            }

            ProcessSimulator.OUTPUT_FILE.close();
        } catch (IOException ex) {
            System.out.println("Unable to write summary to file.");
        }
    }
}
