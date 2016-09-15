package Scheduler;

import Dispatcher.Dispatcher;
import Simulator.ProcessSimulator;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Institution: University of Newcastle
 * Programmer:  Ben Sutter
 * Course Code: COMP2240
 * UID: 3063467
 * Assignment 1
 * Scheduler Abstract Class
 * Scheduler.java
 * Last Modified: 01/09/2016
 */
public abstract class Scheduler {
    
    protected boolean isRunning;
    protected int runningTime;
    protected int waitingTime;
    protected LinkedList<Process> completedProcesses;
    protected int averageWaitTime;
    protected int averageTurnaroundTime;
    protected boolean dispFlag;
    protected int remainingDispTime;

    public static final int TIME_QUANTUM = 4;

    protected Process currentProcess;
    private int currentTick;

    /**
     * scheduler()
     * Constructor
     */
    public Scheduler() {
        this.isRunning = false;
        this.runningTime = 0;
        this.waitingTime = 0;
        this.averageWaitTime = 0;
        this.averageTurnaroundTime = 0;
        this.currentTick = -1;
        this.completedProcesses = new LinkedList<>();
        this.dispFlag = true;
    }

    /**
     * startScheduler()
     * Implements scheduler start code
     */
    public void startScheduler() {
        this.isRunning = true;
        this.remainingDispTime = Dispatcher.DISPATCH_TIME;
        this.onStart();
    }

    /**
     * stopScheduler()
     * Implements scheduler stop code
     */
    public void stopScheduler() {
        this.isRunning = false;
        this.printReport();
    }

    /**
     * getIsRunning()
     * Checks if scheduler is still running
     * @return
     */
    public boolean getIsRunning() {
        return isRunning;
    }

    /**
     * getCompletedProcessesSize
     * Returns size of completed process list
     * @return
     */
    public int getCompletedProcessesSize() {
        if (completedProcesses.isEmpty()) {
            return 0;
        } else {
            return completedProcesses.size();
        }
    }

    /**
     * getCurrentTick()
     * Returns current time tick
     * @return
     */
    public int getCurrentTick() {
        return currentTick;
    }

    /**
     * setCurrentTick()
     * Sets current time tick
     * @param currentTick
     */
    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    /**
     * getAverageWaitTime()
     * Calculates average wait time
     * @return
     */
    public double getAverageWaitTime() {
        return (double)this.averageWaitTime/this.completedProcesses.size();
    }

    /**
     * getAverageTurnaroundTime()
     * Calculated average turnaround time
     * @return
     */
    public double getAverageTurnaroundTime() {
        return (double)this.averageTurnaroundTime/this.completedProcesses.size();
    }

    /**
     * printReport()
     * Prints finalised data to console and file
     */
    public void printReport() {
        try {
            // First of all, sort completed processes back into order
            Collections.sort(completedProcesses);

            // Print process report for this scheduler
            // Prints to console and file
            ProcessSimulator.OUTPUT_FILE.write("\n");
            System.out.println();
            String header = String.format("%-7s%16s%19s", "Process", "Waiting Time", "Turnaround Time");
            ProcessSimulator.OUTPUT_FILE.write(header + "\n");
            System.out.println(header);

            for (Process p : completedProcesses) {
                int waitTime = p.getExitTime() - p.getArriveTime() - p.getExecSize();
                int turnAroundTime = p.getExitTime() - p.getArriveTime();
                this.averageWaitTime += waitTime;
                this.averageTurnaroundTime += turnAroundTime;
                String process = String.format("%-7s%16d%19d", p.getId(),waitTime, turnAroundTime);
                ProcessSimulator.OUTPUT_FILE.write(process + "\n");
                System.out.println(process);
            }
            ProcessSimulator.OUTPUT_FILE.write("\n");
            System.out.println();
        } catch (IOException ex) {
            System.out.println("Unable to write " + this.schedulerName() + " to file.");
        }
    }

    /**
     * onStart()
     * Implements scheduler start code
     */
    public void onStart() {
        try {
            ProcessSimulator.OUTPUT_FILE.write("\n");
            System.out.println();
            ProcessSimulator.OUTPUT_FILE.write("\n" + this.schedulerName() + "\n");
            System.out.println(this.schedulerName());
        } catch (IOException ex) {
            System.out.println("Unable to write " + this.schedulerName() + " to file.");
        }
    }

    /**
     * loadProcess()
     * Prints to console and file on process load
     * @param p
     */
    public void loadProcess(Process p) {
        // Print this process entry
        try {
            String process = String.format("%-5s%3s", "T" + (this.getCurrentTick()) +":", p.getId());
            ProcessSimulator.OUTPUT_FILE.write(process + "\n");
            System.out.println(process);
        } catch (IOException ex) {
            System.out.println("Unable to write " + this.schedulerName() + " to file.");
        }
    }

    /**
     * Abstract Classes
     */
    public abstract String schedulerName();
    public abstract void onTick();
    public abstract void processIncoming(Process process);
}
