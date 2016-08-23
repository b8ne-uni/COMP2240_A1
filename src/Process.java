/**
 * Created by ben on 23/08/2016.
 */
public class Process {
    private String id;
    private int arriveTime;
    private int execSize;

    public Process() {
        this("", 0, 0);
    }

    public Process(String id, int arriveTime, int execSize) {
        this.id = id;
        this.arriveTime = arriveTime;
        this.execSize = execSize;
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
