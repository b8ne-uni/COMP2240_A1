package Scheduler.NRR;

import Scheduler.Scheduler;
import Scheduler.Process;

/**
 * Institution: University of Newcastle
 * Programmer:  Ben Sutter
 * Course Code: COMP2240
 * UID: 3063467
 * Assignment 1
 * NRRProcess Process Extension
 * NRRProcess.java
 * Last Modified: 01/09/2016
 */
public class NRRProcess extends Process {
    private int timeQuantum;

    public NRRProcess(Process p) {
        super(p);
        this.timeQuantum = Scheduler.TIME_QUANTUM;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }
}
