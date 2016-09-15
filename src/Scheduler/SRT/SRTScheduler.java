package Scheduler.SRT;

import Dispatcher.Dispatcher;
import Scheduler.Scheduler;
import Scheduler.Process;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Institution: University of Newcastle
 * Programmer:  Ben Sutter
 * Course Code: COMP2240
 * UID: 3063467
 * Assignment 1
 * SRT Algorithm
 * SRTScheduler.java
 * Last Modified: 01/09/2016
 */
public class SRTScheduler extends Scheduler {
    private PriorityQueue<Process> readyQueue;
    private Comparator<Process> comparator;

    public SRTScheduler() {
        this.comparator = new ProcessComparator();
        this.readyQueue = new PriorityQueue<>(5, comparator);
    }

    private class ProcessComparator implements Comparator<Process> {
        @Override
        public int compare(Process p1, Process p2) {
            int p1Remaining = p1.getExecSize() - p1.getRunningTime();
            int p2Remaining = p2.getExecSize() - p2.getRunningTime();

            if (p1Remaining < p2Remaining) {
                return -1;
            }
            if (p1Remaining > p2Remaining) {
                return 1;
            }
            return 0;
        }
    }

    @Override
    public String schedulerName() {
        return "SRT:";
    }

    @Override
    public void onTick() {
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

        // If a new process arrived that is shorter than the one running
        if (!readyQueue.isEmpty() && currentProcess != null) {
            int currentRemaining = currentProcess.getExecSize() - currentProcess.getRunningTime();
            int peekRemaining = readyQueue.peek().getExecSize() - readyQueue.peek().getRunningTime();
            if (peekRemaining < currentRemaining) {
                readyQueue.add(currentProcess);
                currentProcess = null;
                this.dispFlag = true;
            }
        }

        // Start a new process if none are running
        if (this.dispFlag && currentProcess == null) {
            // Skip this tick
            this.remainingDispTime--;
            if (remainingDispTime == 0) {
                this.dispFlag = false;
                this.remainingDispTime = Dispatcher.DISPATCH_TIME;
            }
        } else {
            if (currentProcess == null && !readyQueue.isEmpty()) {
                // Load in next process
                currentProcess = readyQueue.poll();

                // Print this process entry
                loadProcess(currentProcess);

                // Set start time after this increment, as thats when it will start
                currentProcess.setStartTime(this.getCurrentTick());
            }
        }
    }

    @Override
    public void processIncoming(Process process) {
        readyQueue.add(process);
    }
}
