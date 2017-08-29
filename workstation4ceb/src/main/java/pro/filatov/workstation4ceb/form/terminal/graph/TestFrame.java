package pro.filatov.workstation4ceb.form.terminal.graph;

import pro.filatov.workstation4ceb.form.terminal.GridBagHelper;
import pro.filatov.workstation4ceb.model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by user on 16.08.2017.
 */
public class TestFrame {
    boolean flag = true;
    static boolean flag_1 = false;
    static double resp[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    static  PointData pointData = new PointData(500);

    static GraphTextField
            sinGO = new GraphTextField("SinGO1", new Color(255, 0, 0)),
            cosGO = new GraphTextField("CosGO1", new Color(0, 255, 0)),
            sinTO = new GraphTextField("SinTO1", new Color(0, 0, 255)),
            cosTO = new GraphTextField("CosTO1", new Color(128, 128, 0)),
            sinGO1 = new GraphTextField("SinGO2", new Color(128, 0, 128)),
            cosGO1 = new GraphTextField("CosGO2", new Color(0, 128, 128)),
            sinTO1 = new GraphTextField("SinTO2", new Color(128, 0, 0)),
            cosTO1 = new GraphTextField("CosTO2", new Color(0, 128, 0)),
            sinGO2 = new GraphTextField("SinGO3", new Color(0, 0, 128)),
            cosGO2 = new GraphTextField("CosGO3", new Color(192, 0, 0)),
            sinTO2 = new GraphTextField("SinTO3", new Color(0, 192, 0)),
            cosTO2 = new GraphTextField("CosTO3", new Color(0, 0, 192));
    public static void main(String[] args) {


        //final boolean flag = true;

        final JFrame frame2 = new JFrame (" Basic Frame");
        JButton button = new JButton("Start Test");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        GraphFrame grapgFrame = new GraphFrame(pointData, 5000, 10);
                    }
                });
            }
        });
/*
        final Timer testPoint = new Timer(10, new ActionListener() {
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
        });*/
        final Timer testPoint = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resp[0] = Math.sin(System.currentTimeMillis());
                resp[1] = Math.cos(System.currentTimeMillis())/1000;
                resp[2] = Math.sin(System.currentTimeMillis())/100+10000;
                resp[3] = Math.cos(System.currentTimeMillis())/100-10;
                resp[4] = Math.sin(System.currentTimeMillis());
                resp[5] = Math.cos(System.currentTimeMillis())*1000;
                resp[6] = Math.sin(System.currentTimeMillis())/100+10;
                resp[7] = Math.cos(System.currentTimeMillis())/100-10;
                resp[8] = Math.sin(System.currentTimeMillis());
                resp[9] = Math.cos(System.currentTimeMillis())/10;
                resp[10] = Math.sin(System.currentTimeMillis())+ 20;
                resp[11] = Math.cos(System.currentTimeMillis())/-10;
                refresh();
            }
        });

        //private void refreshGraphTextFields();
        final JButton test = new JButton("Test1");
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

        GridBagHelper helper = new GridBagHelper();
        JPanel settingsPlotPanel = new JPanel(new GridBagLayout());
        //helper.setWeights(0.33f, 0.06f).fillBoth();
        settingsPlotPanel.setBackground(Color.white);
        settingsPlotPanel.add(new Label("str1"), helper.fillBoth().get());
        settingsPlotPanel.add(sinGO, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(cosGO, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(sinTO, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(cosTO, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(sinGO1, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(cosGO1, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(new Label("str2"), helper.nextRow().fillBoth().get());
        settingsPlotPanel.add(sinTO1, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(cosTO1, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(sinTO2, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(cosTO2, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(sinGO2, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(cosGO2, helper.rightColumn().fillBoth().get());
        frame2.setSize(500, 500);
        frame2.setVisible(true);
        frame2.add(button, BorderLayout.CENTER);
        frame2.add(test, BorderLayout.EAST);
        frame2.add(settingsPlotPanel, BorderLayout.SOUTH);

/*
        sinGO.setText(getSensor(resp[2], resp[3]));
        cosGO.setText(getSensor(resp[4], resp[5]));
        sinTO.setText(getSensor(resp[6], resp[7]));
        cosTO.setText(getSensor(resp[8], resp[9]));

        sinGO.addPoint(getSensorDouble(resp[2], resp[3]));
        cosGO.addPoint(getSensorDouble(resp[4], resp[5]));
        sinTO.addPoint(getSensorDouble(resp[6], resp[7]));
        cosTO.addPoint(getSensorDouble(resp[8], resp[9]));
*/
        frame2.setDefaultCloseOperation(frame2.EXIT_ON_CLOSE);

        //PointData data = new PointData(1000);
        //BasicFrame basicFrame = new BasicFrame(data, 5000, 10);
    }
    private static void refresh(){
        pointData.addPointPackage();
        sinGO.setText(Double.toString(resp[0]));
        cosGO.setText(Double.toString(resp[1]));
        sinTO.setText(Double.toString(resp[2]));
        cosTO.setText(Double.toString(resp[3]));

        sinGO.addPoint(resp[0], pointData);
        cosGO.addPoint(resp[1], pointData);
        sinTO.addPoint(resp[2], pointData);
        cosTO.addPoint(resp[3], pointData);
        sinGO1.addPoint(resp[4], pointData);
        cosGO1.addPoint(resp[5], pointData);
        sinTO1.addPoint(resp[6], pointData);
        cosTO1.addPoint(resp[7], pointData);
        sinGO2.addPoint(resp[8], pointData);
        cosGO2.addPoint(resp[9], pointData);
        sinTO2.addPoint(resp[10], pointData);
        cosTO2.addPoint(resp[11], pointData);
    }
}
