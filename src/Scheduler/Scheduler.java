package Scheduler;

import Simulator.ProcessSimulator;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by ben on 24/08/2016.
 */
public abstract class Scheduler {
    
    protected boolean isRunning;
    protected int runningTime;
    protected int waitingTime;
    protected LinkedList<Process> completedProcesses;
    protected int averageWaitTime;
    protected int averageTurnaroundTime;

    public static final int TIME_QUANTUM = 4;

    protected Process currentProcess;
    private int currentTick;

    public Scheduler() {
        this.isRunning = false;
        this.runningTime = 0;
        this.waitingTime = 0;
        this.averageWaitTime = 0;
        this.averageTurnaroundTime = 0;
        this.currentTick = -1;
        this.completedProcesses = new LinkedList<>();
    }

    public void startScheduler() {
        this.isRunning = true;
        this.onStart();
    }

    public void stopScheduler() {
        this.isRunning = false;
        this.printReport();
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public int getCompletedProcessesSize() {
        if (completedProcesses.isEmpty()) {
            return 0;
        } else {
            return completedProcesses.size();
        }
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public double getAverageWaitTime() {
        return (double)this.averageWaitTime/this.completedProcesses.size();
    }

    public double getAverageTurnaroundTime() {
        return (double)this.averageTurnaroundTime/this.completedProcesses.size();
    }

    public void printReport() {
        try {
            // First of all, sort completed processes back into order
            Collections.sort(completedProcesses);

            // Print process report for this scheduler
            // Prints to console and file
            ProcessSimulator.OUTPUT_FILE.write("\n" + this.schedulerName() + "\n");
            System.out.println(this.schedulerName());

            for (Process p : completedProcesses) {
                String process = String.format("%-5s%3s", "T"+p.getStartTime()+":", p.getId());
                ProcessSimulator.OUTPUT_FILE.write(process + "\n");
                System.out.println(process);
            }

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
        } catch (IOException ex) {
            System.out.println("Unable to write " + this.schedulerName() + " to file.");
        }

    }

    public abstract String schedulerName();
    public abstract void onStart();
    public abstract void onStop();
    public abstract void onTick();
    public abstract void processIncoming(Process process);
}
