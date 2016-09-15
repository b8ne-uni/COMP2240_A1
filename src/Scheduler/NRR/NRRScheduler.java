package Scheduler.NRR;

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
 * NRR Algorithm
 * NRRScheduler.java
 * Last Modified: 01/09/2016
 */
public class NRRScheduler extends Scheduler {
    private ArrayDeque<NRRProcess> readyQueue;
    private int timeRemaining;
    private NRRProcess nrrProcess;

    public NRRScheduler() {
        this.readyQueue = new ArrayDeque<>();
        timeRemaining = Scheduler.TIME_QUANTUM;
    }

    @Override
    public String schedulerName() {
        return "NRR:";
    }

    @Override
    public void onTick() {
        // If a process is running, increment its running times
        if (nrrProcess != null) {
            nrrProcess.setRunningTime(nrrProcess.getRunningTime() + 1);
            timeRemaining--;

            // Now see if this process is complete
            if (nrrProcess.getRunningTime() == nrrProcess.getExecSize()) {
                // Set its exit time
                nrrProcess.setExitTime(this.getCurrentTick());

                // Add process to completed queue
                this.completedProcesses.addLast(nrrProcess);

                // Clear current process
                nrrProcess = null;
                this.dispFlag = true;
            }

            // Not complete, but see if time is up
            if (timeRemaining == 0 && nrrProcess != null) {
                // According to specs, if there are no other waiting processes we can leave this one in
                if (readyQueue.isEmpty()) {
                    // Reset time
                    timeRemaining = nrrProcess.getTimeQuantum();
                } else {
                    // First decrease time quantum
                    if (nrrProcess.getTimeQuantum() > 2) {
                        nrrProcess.setTimeQuantum(nrrProcess.getTimeQuantum() - 1);
                    }
                    // Put this process to the end of ready queue
                    readyQueue.addLast(nrrProcess);
                    nrrProcess = null;
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
            if (nrrProcess == null && !readyQueue.isEmpty()) {
                nrrProcess = readyQueue.removeFirst();

                // Print this process entry
                loadProcess(nrrProcess);

                // Set start time after this increment, as thats when it will start
                nrrProcess.setStartTime(this.getCurrentTick());
                // Set time remaining
                timeRemaining = nrrProcess.getTimeQuantum();
            }
        }
    }

    @Override
    public void processIncoming(Process process) {
        readyQueue.add(new NRRProcess(process));
    }
}
