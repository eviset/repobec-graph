package pro.filatov.workstation4ceb.form.terminal.graph;


import java.util.LinkedList;

/**
 * Created by user on 17.07.2017.
 */
public class PointData {
    private LinkedList<PointPackage> pointPackages;
    private int BUFFER;

    private long xZero = -1;


    public long getxZero() {
        return xZero;
    }

    public void setxZero(long xZero) {
        this.xZero = xZero;
    }



    public PointData(int buf) {
        this.BUFFER = buf;
        this.pointPackages = new LinkedList<PointPackage>();
    }

    public double scan(){
        int n, k;
        double count = 1.0d;
        n = pointPackages.size();
        for (int i = 0; i < n; i++){
            k = getPointPackage(i).getSize();
            for(int j = 0; j < k; j++){
                if(getPointPackage(i).getPointValue(j) > count) {count = getPointPackage(i).getPointValue(j);}
                else {
                    if(-getPointPackage(i).getPointValue(j) > count) {count = -getPointPackage(i).getPointValue(j);}
                }
            }
        }
        return count;
    }

    public void addPointPackage() {
        PointPackage pointPackage = new PointPackage();
        if (this.pointPackages.size() < BUFFER)
            this.pointPackages.addFirst(pointPackage);
        else {
            this.pointPackages.removeLast();
            this.pointPackages.addFirst(pointPackage);
        }
    }


    public int getSize(){
        return this.pointPackages.size();
    }

    public PointPackage getPointPackage(int i) {
        return this.pointPackages.get(i);
    }

    public void addPointStruct(double value, int ind){
        this.getPointPackage(0).addPointStruct(value, ind);
        if (xZero >= 0){
            this.getPointPackage(0).setTime(System.currentTimeMillis());
            //this.getPointPackage(0).setCycle(System.currentTimeMillis() - xZero);
        }

    }


}
