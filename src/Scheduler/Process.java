package Scheduler;

/**
 * Created by ben on 23/08/2016.
 */
public class Process {
    // Input vars
    private String id;
    private int arriveTime;
    private int execSize;
    // Calc vars
    private int runningTime;
    private int waitTime;
    private int timeToFinish;
    private int turnAroundTime;


    public Process(String id, int arriveTime, int execSize) {
        this(id, arriveTime, execSize, 0, 0, 0, 0);
    }

    public Process(String id, int arriveTime, int execSize, int runningTime, int waitTime, int timeToFinish, int turnAroundTime) {
        this.id = id;
        this.arriveTime = arriveTime;
        this.execSize = execSize;
        this.runningTime = runningTime;
        this.waitTime = waitTime;
        this.timeToFinish = timeToFinish;
        this.turnAroundTime = turnAroundTime;
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
}
