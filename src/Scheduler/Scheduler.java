package Scheduler;

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
        return this.averageWaitTime/this.completedProcesses.size();
    }

    public double getAverageTurnaroundTime() {
        return this.averageTurnaroundTime/this.completedProcesses.size();
    }

    public void printReport() {
        // Print process report for this scheduler
        System.out.println(this.schedulerName());
        for (Process p : completedProcesses) {
            System.out.println("T" + p.getStartTime() + ": " + p.getId());
        }
        System.out.println();
        System.out.println("Process Waiting Time Turnaround Time");
        for (Process p : completedProcesses) {
            int waitTime = p.getExitTime() - p.getArriveTime() - p.getExecSize();
            int turnAroundTime = p.getExitTime() - p.getArriveTime();
            this.averageWaitTime += waitTime;
            this.averageTurnaroundTime += turnAroundTime;
            System.out.println(p.getId() + " " + waitTime + " " + turnAroundTime);
        }
    }

    public abstract String schedulerName();
    public abstract void onStart();
    public abstract void onTick();
    public abstract void processIncoming(Process process);
}
