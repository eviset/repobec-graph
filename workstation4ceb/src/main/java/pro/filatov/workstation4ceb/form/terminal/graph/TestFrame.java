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
    static boolean flag_1 = false;
    static boolean flag_2 = false;
    static boolean flag_3 = false;
    static boolean flagTest2 = false;
    static boolean flagTest3 = false;
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
                    if (flagTest2)data.addPointStruct(-2, "Test 2", Color.RED);
                    if (flagTest3)data.addPointStruct(-1, "Test 3", Color.GREEN);
                }
                else
                {
                    data.addPointPackage();
                    data.addPointStruct(0, "Test 1", Color.BLUE);
                    if (flagTest2)data.addPointStruct(4, "Test 2", Color.RED);
                    if (flagTest3)data.addPointStruct(2, "Test 3", Color.GREEN);
                }
                flag = !flag;
            }
        });
        final JButton test = new JButton("Test1");
        final JButton test2 = new JButton("Test2");
        final JButton test3 = new JButton("Test3");
        test.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flag_1 = !flag_1;
                if (flag_1) {
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
        test2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag_2 = !flag_2;
                if (flag_2) {
                    flagTest2 = true;
                }else{
                    flagTest2 = false;
                }
            }
        });

        test3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag_3 = !flag_3;
                if (flag_3) {
                    flagTest3 = true;
                }else{
                    flagTest3 = false;
                }
            }
        });


        frame2.setSize(500, 500);
        frame2.setVisible(true);
        frame2.add(button, BorderLayout.CENTER);
        frame2.add(test, BorderLayout.EAST);
        frame2.add(test2, BorderLayout.WEST);
        frame2.add(test3, BorderLayout.SOUTH);

        frame2.setDefaultCloseOperation(frame2.EXIT_ON_CLOSE);

        //PointData data = new PointData(1000);
        //BasicFrame basicFrame = new BasicFrame(data, 5000, 10);
    }
}
