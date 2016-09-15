package Scheduler.Feedback;

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
 * FB Algorithm
 * FBScheduler.java
 * Last Modified: 01/09/2016
 */
public class FBScheduler extends Scheduler {
    private ArrayDeque<Process>[] readyQueues;
    private int timeRemaining;
    private int currentPriority;

    public FBScheduler() {
        this.readyQueues = new ArrayDeque[6];
        for (int i = 0; i < readyQueues.length; i++) {
            readyQueues[i] = new ArrayDeque<>();
        }
        timeRemaining = TIME_QUANTUM;
        currentPriority = 0;
    }

    /**
     * getNextProcess()
     * Private Helper Function to get next process
     * @return
     */
    private Process getNextProcess() {
        for (int i = 0; i < readyQueues.length; i++) {
            if (!readyQueues[i].isEmpty()) {
                currentPriority = i;
                return readyQueues[i].poll();
            }
        }
        return null;
    }

    /**
     * readyIsEmpty()
     * Private helper function
     * Determines if there are any processes in any queues
     * @return
     */
    private boolean readyIsEmpty() {
        for (int i = 0; i < readyQueues.length; i++) {
            if (!readyQueues[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String schedulerName() {
        return "FB:";
    }

    @Override
    public void onTick() {
        // If a process is running, increment its running times
        if (currentProcess != null) {
            currentProcess.setRunningTime(currentProcess.getRunningTime() + 1);
            timeRemaining--;

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

            // Not complete, but see if time is up
            if (timeRemaining == 0 && currentProcess != null) {
                if (readyIsEmpty()) {
                    // Reset time
                    timeRemaining = TIME_QUANTUM;
                } else {
                    // Put this process in a lower queue
                    readyQueues[currentPriority + 1].addLast(currentProcess);
                    currentProcess = null;
                    this.dispFlag = true;
                }
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
            if (currentProcess == null && !readyIsEmpty()) {
                currentProcess = getNextProcess();

                // Print this process entry
                loadProcess(currentProcess);

                // Set start time after this increment, as thats when it will start
                currentProcess.setStartTime(this.getCurrentTick());
                // Set time remaining
                timeRemaining = TIME_QUANTUM;
            }
        }
    }

    @Override
    public void processIncoming(Process process) {
        readyQueues[0].addLast(process);
    }
}