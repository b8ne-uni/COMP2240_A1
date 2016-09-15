package Scheduler;

/**
 * Institution: University of Newcastle
 * Programmer:  Ben Sutter
 * Course Code: COMP2240
 * UID: 3063467
 * Assignment 1
 * Process Class
 * Process.java
 * Last Modified: 01/09/2016
 */
public class Process implements Comparable<Process> {
    // Input vars
    private String id;
    private int arriveTime;
    private int execSize;
    private int startTime;
    private int exitTime;
    private int runningTime;

    /**
     * Copy Constructor
     * @param currentProcess
     */
    public Process(Process currentProcess) {
        this(
                currentProcess.getId(),
                currentProcess.getArriveTime(),
                currentProcess.getExecSize()
        );
    }

    /**
     * Helper Constructor
     * @param id
     * @param arriveTime
     * @param execSize
     */
    public Process(String id, int arriveTime, int execSize) {
        this(id, arriveTime, execSize, 0, 0, 0);
    }

    /**
     * Full Constructor
     * @param id
     * @param arriveTime
     * @param execSize
     * @param exitTime
     * @param runningTime
     * @param startTime
     */
    public Process(String id, int arriveTime, int execSize, int exitTime, int runningTime, int startTime) {
        this.id = id;
        this.arriveTime = arriveTime;
        this.execSize = execSize;
        this.exitTime = exitTime;
        this.runningTime = runningTime;
        this.startTime = startTime;
    }

    /**
     * Getter
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Getter
     * @return
     */
    public int getArriveTime() {
        return arriveTime;
    }

    /**
     * Getter
     * @return
     */
    public int getExecSize() {
        return execSize;
    }

    /**
     * Getter
     * @return
     */
    public int getExitTime() {
        return exitTime;
    }

    /**
     * Setter
     * @param exitTime
     */
    public void setExitTime(int exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * Getter
     * @return
     */
    public int getRunningTime() {
        return runningTime;
    }

    /**
     * Setter
     * @param runningTime
     */
    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    /**
     * Setter
     * @param startTime
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    @Override
    public int compareTo(Process o) {
        int compareId = Integer.parseInt(o.getId().replaceAll("[\\D]", ""));
        int thisId = Integer.parseInt(this.getId().replaceAll("[\\D]", ""));
        return thisId - compareId;
    }
}
