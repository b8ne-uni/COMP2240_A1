package Dispatcher;

import Scheduler.FCFS.FCFSScheduler;
import Scheduler.Scheduler;
import Scheduler.Process;

import java.util.ArrayDeque;
import java.util.LinkedList;

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

        this.schedulers.addLast(fcfs);
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
                    for (Process p : processes) {
                        if (p.getArriveTime() == s.getCurrentTick() + 1) {
                            s.processIncoming(p);
                        }
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
     * Outputs Dispatcher summary
     */
    public void printSummary() {
        System.out.println("\nSummary Algorithm Average Waiting Time Average Turnaround Time");
        for (Scheduler s : schedulers) {
            System.out.println(s.schedulerName() + " " + s.getAverageWaitTime() + " " + s.getAverageTurnaroundTime());
        }
    }
}
