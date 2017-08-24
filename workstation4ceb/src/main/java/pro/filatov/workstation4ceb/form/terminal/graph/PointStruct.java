package pro.filatov.workstation4ceb.form.terminal.graph;


import java.awt.*;

/**
 * Created by user on 18.07.2017.
 */
public class PointStruct {
    private double value;
    private int ind = -1;

    public PointStruct(double value, int ind) {
        this.value = value;
        this.ind = ind;
    }

    public double getValue() {
        return value;
    }

    public int getInd() {
        return ind;
    }
}
