package pro.filatov.workstation4ceb.form.terminal.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by user on 16.08.2017.
 */
public class TestFrame {
    static boolean flag = true;
    static boolean flag_2 = false;
    public static void main(String[] args) {

        //final boolean flag = true;

        final PointData data = new PointData(1000);
        final JFrame frame2 = new JFrame (" Basic Frame");
        JButton button = new JButton("Start Test");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GraphFrame basicFrame = new GraphFrame(data, 5000, 10);
            }
        });

        final Timer testPoint = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (flag) {
                    data.addPointPackage();
                    data.addPointStruct(2, "Test 1", Color.BLUE);
                    data.addPointStruct(-2, "Test 2", Color.RED);
                }
                else
                {
                    data.addPointPackage();
                    data.addPointStruct(0, "Test 1", Color.BLUE);
                    data.addPointStruct(-4, "Test 2", Color.RED);
                }
                flag = !flag;
            }
        });

        final JButton test = new JButton("Test List");
        test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flag_2 = !flag_2;
                if (flag_2) {
                    test.setBackground(Color.ORANGE);
                    testPoint.start();
                }
                else
                {
                    testPoint.stop();
                    test.setBackground(Color.lightGray);
                }
            }
        });


        frame2.setSize(500, 500);
        frame2.setVisible(true);
        frame2.add(button, BorderLayout.CENTER);
        frame2.add(test, BorderLayout.EAST);

        frame2.setDefaultCloseOperation(frame2.EXIT_ON_CLOSE);

        //PointData data = new PointData(1000);
        //BasicFrame basicFrame = new BasicFrame(data, 5000, 10);
    }
}
