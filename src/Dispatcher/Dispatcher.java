package Dispatcher;

import Scheduler.Feedback.FBScheduler;
import Scheduler.NRR.NRRScheduler;
import Scheduler.Process;
import Scheduler.RR.RRScheduler;
import Scheduler.SRT.SRTScheduler;
import Scheduler.Scheduler;
import Scheduler.FCFS.FCFSScheduler;
import Simulator.ProcessSimulator;
import java.io.IOException;
import java.util.*;

/**
 * Institution: University of Newcastle
 * Programmer:  Ben Sutter
 * Course Code: COMP2240
 * UID: 3063467
 * Assignment 1
 * Dispatcher Class
 * Dispatcher.java
 * Last Modified: 01/09/2016
 */
public class Dispatcher {
    private ArrayDeque<Scheduler> schedulers;
    private LinkedList<Process> processes;

    public static int DISPATCH_TIME;

    /**
     * Constructor
     */
    public Dispatcher() {
        // Init schedulers
        this.schedulers = new ArrayDeque<>();
        Scheduler fcfs = new FCFSScheduler();
        Scheduler rr = new RRScheduler();
        Scheduler srt = new SRTScheduler();
        Scheduler nrr = new NRRScheduler();
        Scheduler fb = new FBScheduler();

        this.schedulers.addLast(fcfs);
        this.schedulers.addLast(rr);
        this.schedulers.addLast(srt);
        this.schedulers.addLast(fb);
        this.schedulers.addLast(nrr);
    }

    /**
     * setProcesses()
     * Assigns processes to this dispatcher
     * @param processes
     */
    public void setProcesses(LinkedList<Process> processes) {
        this.processes = processes;
    }

    /**
     * setDispatchTime()
     * Assigns dispatch time to this dispatcher
     * @param dispatchTime
     */
    public void setDispatchTime(int dispatchTime) {
        this.DISPATCH_TIME = dispatchTime;
    }

    /**
     * runDispatcher()
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
        }
        this.printSummary();
    }

    /**
     * printSummary()
     * Outputs Dispatcher summary
     */
    public void printSummary() {
        try {
            ProcessSimulator.OUTPUT_FILE.write("Summary\n");
            System.out.println("Summary");
            String header = String.format("%-9s%23s%26s", "Algorithm", "Average Waiting Time", "Average Turnaround Time");
            ProcessSimulator.OUTPUT_FILE.write(header + "\n");
            System.out.println(header);
            for (Scheduler s : schedulers) {
                String summary = String.format("%-9s%23.2f%26.2f", s.schedulerName(), s.getAverageWaitTime(), s.getAverageTurnaroundTime());
                ProcessSimulator.OUTPUT_FILE.write(summary + "\n");
                System.out.println(summary);
            }

            ProcessSimulator.OUTPUT_FILE.close();
        } catch (IOException ex) {
            System.out.println("Unable to write summary to file.");
        }
    }
}
