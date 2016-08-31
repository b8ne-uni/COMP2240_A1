package Scheduler;

/**
 * Created by ben on 23/08/2016.
 */
public class Process implements Comparable<Process> {
    // Input vars
    private String id;
    private int arriveTime;
    private int execSize;
    private int startTime;
    private int exitTime;
    private int runningTime;

    // Copy Constructor
    public Process(Process currentProcess) {
        this(
                currentProcess.getId(),
                currentProcess.getArriveTime(),
                currentProcess.getExecSize()
        );
    }

    // Helper Constructor
    public Process(String id, int arriveTime, int execSize) {
        this(id, arriveTime, execSize, 0, 0, 0);
    }

    // Complete Constructor
    public Process(String id, int arriveTime, int execSize, int exitTime, int runningTime, int startTime) {
        this.id = id;
        this.arriveTime = arriveTime;
        this.execSize = execSize;
        this.exitTime = exitTime;
        this.runningTime = runningTime;
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(int arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getExecSize() {
        return execSize;
    }

    public void setExecSize(int execSize) {
        this.execSize = execSize;
    }

    public int getExitTime() {
        return exitTime;
    }

    public void setExitTime(int exitTime) {
        this.exitTime = exitTime;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public int getStartTime() {
        return startTime;
    }

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
