package Scheduler.RR;

import Dispatcher.Dispatcher;
import Scheduler.Scheduler;
import Scheduler.Process;

import java.util.ArrayDeque;

/**
 * Created by ben on 24/08/2016.
 */
public class RRScheduler extends Scheduler {
    private ArrayDeque<Process> readyQueue;
    private int timeRemaining;

    public RRScheduler() {
        this.readyQueue = new ArrayDeque<>();
        timeRemaining = TIME_QUANTUM;
    }
    @Override
    public String schedulerName() {
        return "RR:";
    }

    @Override
    public void onStart() {
        System.out.println("Starting RR");
    }

    @Override
    public void onStop() {
        System.out.println("Finished RR");
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
            }

            // Not complete, but see if time is up
            if (timeRemaining == 0 && currentProcess != null) {
                // According to specs, if there are no other waiting processes we can leave this one in
                if (readyQueue.isEmpty()) {
                    // Reset time
                    timeRemaining = TIME_QUANTUM;
                } else {
                    // Put this process to the end of ready queue
                    readyQueue.addLast(currentProcess);
                    currentProcess = null;
                }
            }
        }

        // Start a new process if none are running
        if (currentProcess == null && !readyQueue.isEmpty()) {
            currentProcess = readyQueue.removeFirst();
            // Increment tick one(1) to account for dispatcher time
            this.setCurrentTick(this.getCurrentTick() + Dispatcher.DISPATCH_TIME);
            // Set start time after this increment, as thats when it will start
            currentProcess.setStartTime(this.getCurrentTick());
            // Set time remaining
            timeRemaining = TIME_QUANTUM;
        }
    }

    @Override
    public void processIncoming(Process process) {
        readyQueue.addLast(process);
    }
}
