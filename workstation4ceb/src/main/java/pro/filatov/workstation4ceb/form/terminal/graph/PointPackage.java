package pro.filatov.workstation4ceb.form.terminal.graph;



import java.util.LinkedList;

/**
 * Created by user on 17.07.2017.
 */
public class PointPackage {
    private LinkedList<PointStruct> pointList;
    private long time;
    //private long cycle;



    public PointPackage() {
        this.pointList = new LinkedList<PointStruct>();
    }

    public void addPointStruct(double value, int ind){
        PointStruct point = new PointStruct(value, ind);
        this.pointList.add(point);
    }

    public double getPointValue(int index){
        return pointList.get(index).getValue();
    }

    public int getPointInd(int index){
        return pointList.get(index).getInd();
    }

    public int getSize(){
        return this.pointList.size();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
/*
    public long getCycle() {
        return cycle;
    }

    public void setCycle(long cycle) {
        this.cycle = cycle;
    }*/
}
