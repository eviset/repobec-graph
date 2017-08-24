package pro.filatov.workstation4ceb.form.terminal.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Egor on 03.07.2017.
 */
public class GraphTextField extends JTextField{


    private final Color COLOR_ON = new Color(235, 235, 235);


    private String labelGraph;
    private String labelTextField;


    public void  addPoint(String valueTextField, Double valueGraph){



    }


    private int index = -1;





    public GraphTextField(int index) {
        this.setIndex(index);
        super.setBackground(COLOR_ON);
        super.setEnabled(false);
        super.setDisabledTextColor(Color.black);
        super.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GraphTextField.this.getBackground() != Color.orange) {
                    GraphTextField.this.setBackground(Color.orange);
                }
                else {
                    GraphTextField.this.setBackground(COLOR_ON);
                }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }
            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public GraphTextField(String name, Color color) {
        this.setIndex(index);
        super.setBackground(COLOR_ON);
        super.setEnabled(false);
        super.setDisabledTextColor(Color.black);

        super.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GraphTextField.this.getBackground() != Color.orange) {
                    GraphTextField.this.setBackground(Color.orange);
                }
                else {
                    GraphTextField.this.setBackground(COLOR_ON);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public int getIndex() {
        return index;
    }

    public Color getColor(){
        return new Color(255, 255, 255);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    //sinGO.setText(getSensor(resp[2], resp[3]));

    public void setTextCheck(String sensor, PointPackage newPoint){  //add new point to list while BackGround = orange
        super.setText(sensor);
        if (this.getBackground() == Color.orange)
            //newPoint.addPointStruct(Integer.parseInt(sensor), 0);
            newPoint.addPointStruct(Integer.parseInt(sensor), index);
    }
}
