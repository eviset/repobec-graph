package pro.filatov.workstation4ceb.form.terminal.graph;

/**
 * Created by user on 12.07.2017.
 */

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.awt.*;
import javax.swing.*;
import com.jogamp.opengl.util.awt.TextRenderer;
import pro.filatov.workstation4ceb.form.terminal.GridBagHelper;


import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class GraphFrame implements GLEventListener{
    private static PointData pointData;
    private static double sizeY;
    private static boolean flag = false;
    private static boolean flagGLCanvas = false;
    private static boolean flagStop = false;
    private static boolean flagPressed = true;
    private static boolean flagPushMatrix = true;
    private static boolean flagPopMatrix = false;
    private static float timeX, timeStop;
    private static int width, height, delX, delY;
    private static long timeForX, timeZero, offsetDel, offsetSize, offsetSizeZero, gmaMax = 0, pressedX, pressedXZero, range, delXRange;
    private static GL2 gl2_display;
    private static float scaleX = 0.0f, scaleY = 0.0f, scaleXZero = 0.0f, scaleTime = 0.0f, timeOffset = 0.0f, xOffset = 0.0f;
    TextRenderer renderer = new TextRenderer(new Font("Serif", Font.PLAIN, 14), true, true);




    GraphFrame(PointData data, long rangeStart, int delXStart, double rangeYStart, float sacleYStart){
        range = rangeStart;
        delX = delXStart;
        sizeY = rangeYStart;
        scaleY = sacleYStart;
        this.start(this, data);
    }

    GraphFrame(PointData data, long rangeStart, int delXStart, long rangeYStart){
        range = rangeStart;
        delX = delXStart;
        sizeY = rangeYStart;
        scaleY = 2.0f;
        this.start(this, data);
    }

    GraphFrame(PointData data, long rangeStart, int delXStart){
        range = rangeStart;
        delX = delXStart;
        sizeY = 8;
        scaleY = 2.0f;
        this.start(this, data);
    }

    GraphFrame(PointData data, long rangeStart){
        range = rangeStart;
        delX = 10;
        sizeY = 8;
        scaleY = 2.0f;
        this.start(this, data);
    }

    GraphFrame(PointData data){
        range = 5000;
        delX = 10;
        sizeY = 8;
        scaleY = 2.0f;
        this.start(this, data);
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        final GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    public void display(GLAutoDrawable glAutoDrawable) {

        gl2_display = glAutoDrawable.getGL().getGL2();
        gl2_display.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        if (flagGLCanvas)
        {
            if (flagPushMatrix)
            {
                gl2_display.glPushMatrix();
                flagPushMatrix = false;
            }
            if (flagPopMatrix)
            {
                gl2_display.glPopMatrix();
                gl2_display.glPushMatrix();
                flagPopMatrix = false;
            }
            else {
                scaleXZero += scaleX;
                xOffset = 1.9f * ((range * scaleXZero / 1.9f) % delXRange) / range;
                timeOffset = -scaleTime*(((long)(range * scaleXZero / 1.9f)) / delXRange );
                gl2_display.glTranslated(scaleX, 0, 0);
                pressedXZero = pressedX;
            }
            flagGLCanvas = false;
        }


        xNet();
        yNet();
        if (pointData.getSize() != 0) printFunc();
        gl2_display.glColor3f(1.0f,1.0f,1.0f);
        gl2_display.glBegin(GL2.GL_QUADS);
        gl2_display.glVertex2f( -1.0f - scaleXZero, -0.95f);
        gl2_display.glVertex2f( 1.0f - scaleXZero, -0.95f);
        gl2_display.glVertex2f( 1.0f - scaleXZero,-1.0f);
        gl2_display.glVertex2f( -1.0f - scaleXZero,-1.0f);
        gl2_display.glEnd();
        xAxis(width, height);

        gl2_display.glColor3f(1.0f,1.0f,1.0f);
        gl2_display.glBegin(GL2.GL_QUADS);
        gl2_display.glVertex2f(-1.0f - scaleXZero, 1.0f);
        gl2_display.glVertex2f( -0.9f - scaleXZero, 1.0f);
        gl2_display.glVertex2f( -0.9f - scaleXZero,-1.0f);
        gl2_display.glVertex2f( -1.0f - scaleXZero,-1.0f);
        gl2_display.glEnd();
        width = glAutoDrawable.getWidth();
        height = glAutoDrawable.getHeight();
        yAxis(width, height);
    }

    public void reshape(GLAutoDrawable drawable, int x,  int y, int width, int height) {

    }

    private void printFunc(){
        double x1, x2, y1, y2;
        int n = pointData.getSize();
        int k;
        for (int i = 0; i < n - 1; i++) { //i - buffer; j - sum of ID
            k = pointData.getPointPackage(i).getSize();
            for (int j = 0; j < k; j++) {
                gl2_display.glBegin(GL2.GL_LINES);

                indColor(gl2_display, pointData.getPointPackage(i).getPointInd(j));

                x1 = 1.9f*((float)(pointData.getPointPackage(i).getTime() - timeForX) / range) - 0.9f;
                y1 = 0.9f*pointData.getPointPackage(i).getPointValue(j) / sizeY;

                x2 = 1.9f*((float)(pointData.getPointPackage(i + 1).getTime() - timeForX) / range) - 0.9f;
                y2 = 0.9f*pointData.getPointPackage(i + 1).getPointValue(j) / sizeY;

                gl2_display.glVertex2d(x1, y1);
                gl2_display.glVertex2d(x2, y2);

                gl2_display.glEnd();
            }
        }
    }

    private void xNet (){
        gl2_display.glBegin(GL2.GL_LINES);
        gl2_display.glColor3f(0.9f, 0.9f, 1.0f);
        for (int i = -1; i <= delX + 1; i++) {
            gl2_display.glVertex2f(1.9f*(range/2 - delXRange*i - offsetSize + offsetDel)/range + 0.05f - scaleXZero + xOffset, -1.0f);
            gl2_display.glVertex2f(1.9f*(range/2 - delXRange*i - offsetSize + offsetDel)/range + 0.05f - scaleXZero + xOffset, 1.0f);
        }
        gl2_display.glEnd();
    }

    private void xAxis(int x, int y){
        Float X;
        float x1, y1;
        gl2_display.glBegin(GL2.GL_LINES);
        gl2_display.glColor3f(0.0f, 0.0f, 0.0f);
        gl2_display.glVertex2f(-1.2f - scaleXZero + xOffset, -0.95f); //-0.3f(x)
        gl2_display.glVertex2f(1.3f - scaleXZero + xOffset, -0.95f); // +0.3f(x)
        gl2_display.glEnd();
        for (int i = -1; i <= delX + 1; i++){
            gl2_display.glColor3f(0.0f, 0.0f, 0.0f);
            x1 = 1.9f*(-range/2 + delXRange*i - offsetSize + offsetDel)/range + 0.05f + xOffset;
            y1 = -0.95f;
            gl2_display.glBegin(GL2.GL_LINES);
            gl2_display.glVertex2f(x1 - scaleXZero, y1);
            gl2_display.glVertex2f(x1 - scaleXZero, y1 + 0.02f);
            gl2_display.glEnd();
            renderer.beginRendering(x, y);
            renderer.setColor(0.0f, 0.0f, 0.0f, 1);
            //X = timeX + i*0.5f + timeOffset; scaleTime
            X = timeX + i*scaleTime + timeOffset;
            renderer.draw(X.toString(), (int)(x*(x1+1)/2 - X.toString().length()*3), 5);
            renderer.endRendering();
            gl2_display.glEnd();
        }
    }
    /*
        double minValue = pointData.getMinValue();
        double maxValue = pointData.getMaxValue();

            if (maxValue > -minValue){
            if (maxValue > 1){sizeY = maxValue;}else{sizeY = 1;}
        }
            else{
            if (-minValue > 1){sizeY = -minValue;}else{sizeY = 1;}
        }
    */
    private void yNet(){

        //sizeY = sizeY * scaleYStart;
        gl2_display.glBegin(GL2.GL_LINES);
        gl2_display.glColor3f(0.9f, 0.9f, 1.0f);
        for (int i = -delY - 1; i < delY; i++){
            gl2_display.glVertex2f(-1.0f - scaleXZero, 0.9f*(i+1)/delY);
            gl2_display.glVertex2f(1.0f - scaleXZero, 0.9f*(i+1)/delY);
        }
        gl2_display.glEnd();
    }

    private void yAxis(int x, int y){
        float x1, y1;
        Float X;
        int j = 0;
        gl2_display.glBegin(GL2.GL_LINES);
        gl2_display.glColor3f(0.0f, 0.0f, 0.0f);
        gl2_display.glVertex2f(-0.9f - scaleXZero, -0.95f);
        gl2_display.glVertex2f(-0.9f - scaleXZero, 0.95f);
        gl2_display.glEnd();
        for (int i = -delY - 1; i < delY; i++){
            gl2_display.glColor3f(0.0f, 0.0f, 0.0f);
            gl2_display.glBegin(GL2.GL_LINES);
            x1 = -0.9f - scaleXZero;
            y1 = 0.9f*(i+1)/delY;
            gl2_display.glVertex2f(x1, y1);
            if (i%2 != 0) {
                gl2_display.glVertex2f(x1 + 0.02f, y1);
                gl2_display.glEnd();
                renderer.beginRendering(x, y);
                renderer.setColor(0.0f, 0.0f, 0.0f, 1);
                X = -(float)sizeY + (float)((sizeY/8)*j);
                renderer.draw(X.toString(), 5, (int)(y*(y1+1)/2 - 5));
                renderer.endRendering();
            }
            else {
                gl2_display.glVertex2f(x1 + 0.01f, y1);
                gl2_display.glEnd();
            }
            j++;
        }
    }

    private static boolean flag_2 = true;

    private static void indColor(GL2 gl, int IND){
        int mod = IND % 3;
        double x = 0, y = 0, z = 0;

        int k = IND / 3 + 1;
        switch (mod) {
            case 0:
                x = 1.0;
                if (k%2 == 0) {
                    y = cycle(k/ 2);
                    z = cycle(k/ 2 - 1);
                }
                else
                {
                    z = cycle(k / 2);
                    y = cycle(k / 2 - 1);
                }
                break;
            case 1:
                y = 1.0;
                if (k%2 == 0) {
                    x = cycle(k / 2);
                    z = cycle(k / 2 - 1);
                }
                else
                {
                    z = cycle(k / 2);
                    x = cycle(k / 2 - 1);
                }
                break;
            case 2:
                z = 1.0;
                if (k%2 == 0) {
                    x = cycle(k / 2);
                    y = cycle(k / 2 - 1);
                }
                else
                {
                    y = cycle(k / 2);
                    x = cycle(k / 2 - 1);
                }
                break;
        }
        gl.glColor3d(x, y, z);
    }

    private static double cycle(double IND){
        double s = 0.0, k = 0.5;
        for (int i = 0; i < IND; i++){
            s = s + k;
            k = k / 2;
        }
        return s;
    }

    private void changeRange(){
        //timeStop = - range / 1000;
        delXRange = range / delX;
        scaleTime = delXRange/1000.0f;

        offsetDel = timeForX % delXRange;
    }


    public void start(GraphFrame b, PointData data){

        delY = 8;
        timeStop = - range / 1000;

        //timeStop = - range / 1000;
        //delXRange = range / delX;
        //scaleTime = delXRange/1000.0f;

        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        final GLCapabilities capabilities = new GLCapabilities(profile);

        final GLCanvas glcanvas = new GLCanvas(capabilities);
        glcanvas.addGLEventListener(b);
        //glcanvas.setSize(400, 400);
        glcanvas.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                pressedXZero = e.getX();
                flagPressed = true;
            }

            public void mouseReleased(MouseEvent e) {
                flagPressed = false;
                flagGLCanvas = false;
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        glcanvas.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if (flagPressed){
                    pressedX = e.getX();
                    scaleX = 2.0f*(pressedX - pressedXZero)/(width);
                    flagGLCanvas = true;
                }
            }

            public void mouseMoved(MouseEvent e) {

            }
        });
        glcanvas.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    sizeY = sizeY * scaleY;
                } else {
                    sizeY = sizeY / scaleY;
                }
            }
        });
        pointData = data;
        final JFrame frame = new JFrame ("Graph Frame");

        final JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flagStop = !flagStop;
                if (flagStop) {stopButton.setText("Run");}
                else {stopButton.setText("Stop");}
            }
        });

        final JButton returnTranslated = new JButton("Return");
        returnTranslated.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flagGLCanvas = true;
                flagPopMatrix = true;
                scaleXZero = 0;
                xOffset = 0;
                timeOffset = 0;
            }
        });

        final JButton scale = new JButton("Scale");
        scale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sizeY = pointData.scan();
            }
        });

        final Timer updateTimer = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeZero = System.currentTimeMillis() - range;
                offsetSizeZero = timeZero % delXRange;
                if (offsetSizeZero < gmaMax) {
                    timeStop +=(float)range/(delX*1000);
                }
                gmaMax = offsetSizeZero;
                if (!flagStop){
                    timeForX = timeZero;
                    pointData.setxZero(timeForX);
                    offsetSize = offsetSizeZero;
                    timeX = timeStop;
                }
                glcanvas.display();
            }
        });

        final JTextField rangeXText = new JTextField(5);
        final JTextField delXText = new JTextField(5);
        final JTextField rangeYText = new JTextField(5);
        //final JTextField scaleYText = new JTextField(5);
        JLabel rangeXName = new JLabel("Range X:");
        JLabel delXName = new JLabel("Del X:");
        final JLabel rangeYName = new JLabel("Range Y:");
        //JLabel scaleYName = new JLabel("Scale Y:");


        rangeXText.setText(String.valueOf(range/1000));
        rangeXText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                range = Long.parseLong(rangeXText.getText()) * 1000;
                changeRange();
            }
        });

        delXText.setText(String.valueOf(delX));
        delXText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delX = Integer.parseInt(delXText.getText());
                changeRange();
            }
        });

        rangeYText.setText(String.valueOf(sizeY));
        rangeYText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sizeY = Double.parseDouble(rangeYText.getText());
                changeRange();
            }
        });
/*
        scaleYText.setText(String.valueOf(scaleY));
        scaleYText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaleY = Float.parseFloat(scaleYText.getText());
                changeRange();
            }
        });*/

        GridBagHelper helper = new GridBagHelper();
        JPanel settingsPlotPanel = new JPanel(new GridBagLayout());
        //helper.setWeights(0.33f, 0.06f).fillBoth();
        settingsPlotPanel.setBackground(Color.white);
        settingsPlotPanel.add(rangeXName, helper.get());
        settingsPlotPanel.add(rangeXText, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(rangeYName, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(rangeYText, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(stopButton, helper.rightColumn().fillBoth().get());
        //settingsPlotPanel.add(scale, helper.rightColumn().get());

        settingsPlotPanel.add(delXName, helper.nextRow().get());
        settingsPlotPanel.add(delXText, helper.rightColumn().fillBoth().get());
        //settingsPlotPanel.add(scaleYName, helper.rightColumn().fillBoth().get());
        //settingsPlotPanel.add(scaleYText, helper.rightColumn().fillBoth().get());
        settingsPlotPanel.add(scale, helper.rightColumn(2).fillBoth().get());
        settingsPlotPanel.add(returnTranslated, helper.rightColumn().fillBoth().get());
        //settingsPlotPanel.add(but, helper.rightColumn().fillBoth().get());

        //settingsPlotPanel.add(stopButton, helper.rightColumn().fillBoth().get());
        //settingsPlotPanel.add(rangeX, helper.rightColumn().get());
        //settingsPlotPanel.add(returnTranslated, helper.nextRow().get());

        frame.setSize(1200, 750);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.add(glcanvas, BorderLayout.CENTER);
        frame.add(settingsPlotPanel, BorderLayout.SOUTH);

        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {

            }

            public void windowClosing(WindowEvent e) {
                flagGLCanvas = true;
                flagPopMatrix = true;
                scaleXZero = 0;
                xOffset = 0;
                timeOffset = 0;
                flagStop = false;
                glcanvas.destroy();
                frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
            }

            public void windowClosed(WindowEvent e) {

            }

            public void windowIconified(WindowEvent e) {

            }

            public void windowDeiconified(WindowEvent e) {

            }

            public void windowActivated(WindowEvent e) {

            }

            public void windowDeactivated(WindowEvent e) {

            }
        });
        //frame.
        timeForX = System.currentTimeMillis() - range;
        //offsetDel = timeForX % delXRange;
        changeRange();
        //offsetDel = timeForX % delXRange;
        updateTimer.start();
    }
/*
    public static void main(String[] args) {

        PointData data = new PointData(1000);
        BasicFrame basicFrame = new BasicFrame(data, 5000, 10);
    }*/

}