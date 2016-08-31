package Scheduler.FCFS;

import Dispatcher.Dispatcher;
import Scheduler.Scheduler;
import Scheduler.Process;

import java.util.ArrayDeque;

/**
 * Created by ben on 24/08/2016.
 */
public class FCFSScheduler extends Scheduler {
    private ArrayDeque<Process> readyQueue;

    public FCFSScheduler() {
        this.readyQueue = new ArrayDeque<>();
    }

    @Override
    public String schedulerName() {
        return "FCFS:";
    }

    @Override
    public void onStart() {
        System.out.println("Starting FCFS");
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
            }
        }

        // Start a new process if none are running
        if (currentProcess == null && !readyQueue.isEmpty()) {
            currentProcess = readyQueue.removeFirst();
            // Increment tick one(1) to account for dispatcher time
            this.setCurrentTick(this.getCurrentTick() + Dispatcher.DISPATCH_TIME);
            // Set start time after this increment, as thats when it will start
            currentProcess.setStartTime(this.getCurrentTick());
        }
    }

    @Override
    public void processIncoming(Process process) {
        readyQueue.addLast(process);
    }
}
