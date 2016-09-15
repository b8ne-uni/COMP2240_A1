package Scheduler.FCFS;

import Dispatcher.Dispatcher;
import Scheduler.Scheduler;
import Scheduler.Process;
import java.util.ArrayDeque;

/**
 * Institution: University of Newcastle
 * Programmer:  Ben Sutter
 * Course Code: COMP2240
 * UID: 3063467
 * Assignment 1
 * FCFS Algorithm
 * FCFSScheduler.java
 * Last Modified: 01/09/2016
 */
public class FCFSScheduler extends Scheduler {
    private ArrayDeque<Process> readyQueue;

    /**
     * Constructor
     */
    public FCFSScheduler() {
        this.readyQueue = new ArrayDeque<>();
    }

    @Override
    public String schedulerName() {
        return "FCFS:";
    }

    @Override
    public void onTick() {
        // If a process is running, increment its running times
        if (currentProcess != null) {
            currentProcess.setRunningTime(currentProcess.getRunningTime() + 1);

            // Now see if this process is complete
            if (currentProcess.getRunningTime() == currentProcess.getExecSize()) {
                // Set its exit time
                currentProcess.setExitTime(this.getCurrentTick());

                // Add process to completed queue
                this.completedProcesses.addLast(currentProcess);

                // Clear current process
                currentProcess = null;
                this.dispFlag = true;
            }
        }

        // Check Dispatcher Flag
        if (this.dispFlag && currentProcess == null) {
            // Skip this tick
            this.remainingDispTime--;
            if (remainingDispTime == 0) {
                this.dispFlag = false;
                this.remainingDispTime = Dispatcher.DISPATCH_TIME;
            }
        } else {
            // Start a new process if none are running
            if (currentProcess == null && !readyQueue.isEmpty()) {
                // Load in next process
                currentProcess = readyQueue.removeFirst();

                // Set start time after this increment, as thats when it will start
                currentProcess.setStartTime(this.getCurrentTick());

                // Print this process entry
                loadProcess(currentProcess);
            }
        }
    }

    @Override
    public void processIncoming(Process process) {
        readyQueue.addLast(process);
    }
}
