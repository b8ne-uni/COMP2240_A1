package Scheduler.Feedback;

import Scheduler.Scheduler;
import Scheduler.Process;

/**
 * Created by ben on 24/08/2016.
 */
public class FBScheduler extends Scheduler {

    @Override
    public String schedulerName() {
        return null;
    }

    @Override
    public void onStart() {
        System.out.println("Starting Feedback");
    }

    @Override
    public void onStop() {
        System.out.println("Finished Feedback");
    }

    @Override
    public void onTick() {

    }

    @Override
    public void processIncoming(Process process) {

    }
}
