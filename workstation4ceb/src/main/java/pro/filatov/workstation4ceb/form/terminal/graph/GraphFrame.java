package pro.filatov.workstation4ceb.form.terminal.graph;

/**
 * Created by user on 12.07.2017.
 */

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import com.jogamp.opengl.util.awt.TextRenderer;



import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;
import pro.filatov.workstation4ceb.form.terminal.GridBagHelper;

public class GraphFrame implements GLEventListener{
    private PointData pointData;
    private double rangeY, yMax, yMin, delYRange, textOffset = 0.0d;
    private boolean flagLabelList = true;
    private boolean flagGLCanvas = false;
    private boolean flagStop = false;
    private boolean flagPressed = true;
    private boolean flagPushMatrix = true;
    private boolean flagPopMatrix = false;
    private float timeX, timeStop, scaleWidth = 1.0f, scaleHeight = 1.0f, scaleOffsetWheelX = 0.0f, scaleOffsetWheelY = 0.0f;
    private int width, height, delX, delY, scaleOffsetZeroX, scaleOffsetZeroY;
    private long timeForX, timeZero, offsetDelX, offsetSize, offsetSizeZero, gmaMax = 0, pressedX, pressedY, pressedXZero, pressedYZero, rangeX, delXRange;
    private GL2 gl2_display;
    private float scaleX = 0.0f, scaleY = 0.0f, scaleOffsetY = 0.0f, scaleXZero = 0.0f, scaleYZero = 0.0f, scaleTime = 0.0f, timeOffset = 0.0f, xOffset = 0.0f, yOffset = 0.0f, scaleTimeOffset = 0.0f;
    TextRenderer renderer = new TextRenderer(new Font("Serif", Font.PLAIN, 14), true, true);
    private LinkedList<LabelList> labelList;
    private DecimalFormat df;



/*
    GraphFrame(PointData data, long rangeStart, int delXStart, double rangeYStart, float sacleYStart){
        range = rangeStart;
        delX = delXStart;
        sizeY = rangeYStart;
        scaleY = sacleYStart;
        this.start(this, data);
    }*/

    GraphFrame(PointData data, long rangeStart, int delXStart, double max, double min){
        rangeX = rangeStart;
        delX = delXStart;
        rangeY = max - min;
        yMax = max;
        yMin = min;
        //scaleY = 2.0f;
        this.start(this, data);
    }

    GraphFrame(PointData data, long rangeStart, int delXStart){
        rangeX = rangeStart;
        delX = delXStart;
        rangeY = 20;
        yMax = 10;
        yMin = -10;
        //scaleY = 2.0f;
        this.start(this, data);
    }

    GraphFrame(PointData data, long rangeStart){
        rangeX = rangeStart;
        delX = 10;
        rangeY = 20;
        yMax = 10;
        yMin = -10;
        //scaleY = 2.0f;
        this.start(this, data);
    }

    GraphFrame(PointData data){
        rangeX = 5000;
        delX = 10;
        rangeY = 20;
        yMax = 10;
        yMin = -10;
        //scaleY = 2.0f;
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
            if (!flagPopMatrix)
            {
                scaleXZero += scaleX - scaleOffsetWheelX;
                scaleYZero += scaleY + scaleOffsetY - scaleOffsetWheelY;
                xOffset = 1.7f * ((rangeX * scaleXZero / 1.7f) % delXRange) / rangeX;
                yOffset = 1.9f * (float)(((rangeY * scaleYZero / 1.9d) % delYRange) / rangeY);
                timeOffset = -scaleTime*(((long)(rangeX * scaleXZero / 1.7f)) / delXRange );
                textOffset = delYRange*((long)(rangeY * scaleYZero / 1.9f / delYRange) );
                //scaleTimeOffset = (long)(((long)(rangeX*scaleOffsetWheelX/2))/(scaleTime*1000));
                gl2_display.glTranslated(scaleX - scaleOffsetWheelX, -scaleY - scaleOffsetY + scaleOffsetWheelY, 0);
                pressedXZero = pressedX;
                pressedYZero = pressedY;
                scaleOffsetY = 0.0f;
                scaleOffsetWheelX = 0.0f;
                scaleOffsetWheelY = 0.0f;

            }
            flagGLCanvas = false;
        }


        xNet();
        yNet();
        if (pointData.getSize() != 0) printFunc();
        gl2_display.glColor3f(1.0f,1.0f,1.0f);
        gl2_display.glBegin(GL2.GL_QUADS);
        gl2_display.glVertex2f( -1.0f - scaleXZero, -0.95f + scaleYZero);
        gl2_display.glVertex2f( 1.0f - scaleXZero, -0.95f + scaleYZero);
        gl2_display.glVertex2f( 1.0f - scaleXZero,-1.0f + scaleYZero);
        gl2_display.glVertex2f( -1.0f - scaleXZero,-1.0f + scaleYZero);
        gl2_display.glEnd();
        width = glAutoDrawable.getWidth();
        height = glAutoDrawable.getHeight();

        xAxis(width, height);
        //yAxis(width, height);
        gl2_display.glColor3f(1.0f,1.0f,1.0f);
        gl2_display.glBegin(GL2.GL_QUADS);
        gl2_display.glVertex2f(-1.0f - scaleXZero, 1.0f + scaleYZero);
        gl2_display.glVertex2f( -0.9f - scaleXZero, 1.0f + scaleYZero);
        gl2_display.glVertex2f( -0.9f - scaleXZero,-1.0f + scaleYZero);
        gl2_display.glVertex2f( -1.0f - scaleXZero,-1.0f + scaleYZero);
        gl2_display.glEnd();
        //width = glAutoDrawable.getWidth();
        //height = glAutoDrawable.getHeight();
        yAxis(width, height);



        gl2_display.glColor3f(0.0f,0.5f,0.5f);
        gl2_display.glBegin(GL2.GL_QUADS);
        gl2_display.glVertex2f( 0.005f, 0.005f);
        gl2_display.glVertex2f( -0.005f, 0.005f);
        gl2_display.glVertex2f( -0.005f,-0.005f);
        gl2_display.glVertex2f( 0.005f,-0.005f);
        gl2_display.glEnd();
        gl2_display.glColor3f(0.0f,0.5f,0.5f);
        gl2_display.glBegin(GL2.GL_QUADS);
        gl2_display.glVertex2f( 0.005f - scaleXZero, 0.005f + scaleYZero);
        gl2_display.glVertex2f( -0.005f - scaleXZero, 0.005f + scaleYZero);
        gl2_display.glVertex2f( -0.005f - scaleXZero,-0.005f + scaleYZero);
        gl2_display.glVertex2f( 0.005f - scaleXZero,-0.005f + scaleYZero);
        gl2_display.glEnd();/*
        renderer.beginRendering(width,height);
        renderer.setColor(1.0f, 1.0f, 1.0f, 1);
        renderer.draw(".", width/2 - 1, height/2 - 1);
        renderer.endRendering();*/
        /*gl2_display.glColor3f(0.5f,0.5f,1.0f);
        gl2_display.glBegin(GL2.GL_QUADS);
        gl2_display.glVertex2f( (width/2) + 0.005f - scaleXZero, (height/2)/height + 0.005f + scaleYZero);
        gl2_display.glVertex2f( (width/2) - 0.005f - scaleXZero, (height/2) + 0.005f + scaleYZero);
        gl2_display.glVertex2f( (width/2) - 0.005f - scaleXZero, (height/2) - 0.005f + scaleYZero);
        gl2_display.glVertex2f( (width/2) + 0.005f - scaleXZero, (height/2) - 0.005f + scaleYZero);
        gl2_display.glEnd();*/
        printLabel(width, height);
        while (labelList.size() != 0) {
            labelList.removeLast();
        }
        //scaleOffsetWheelX = 0.0f;
        //scaleOffsetWheelY = 0.0f;
    }

    public void reshape(GLAutoDrawable drawable, int x,  int y, int width, int height) {

    }

    private void printFunc(){
        double x1, x2, y1, y2;
        int n = pointData.getSize();
        int k, sizeLabelList;
        for (int i = 0; i < n - 1; i++) { //i - buffer; j - sum of ID
            k = pointData.getPointPackage(i).getSize();
            sizeLabelList = labelList.size();
            for (int j = 0; j < k; j++) {
                gl2_display.glBegin(GL2.GL_LINES);

                //indColor(gl2_display, pointData.getPointPackage(i).getPointInd(j));
                setColor(gl2_display, pointData.getPointPackage(i).getPointColor(j));

                for (int numLabel = 0; numLabel < sizeLabelList; numLabel++){
                    if (sizeLabelList > 0){
                        if (labelList.get(numLabel).getLabel() == pointData.getPointPackage(i).getPointInd(j)){
                            flagLabelList = false;
                        }
                    }
                }
                if (flagLabelList){
                    labelList.add(new LabelList(pointData.getPointPackage(i).getPointInd(j), pointData.getPointPackage(i).getPointColor(j)));
                }
                flagLabelList = true;

                x1 = scaleWidth*(1.7f*((float)(pointData.getPointPackage(i).getTime() - timeForX) / rangeX) - 0.9f);
                y1 = scaleHeight*(1.9f*pointData.getPointPackage(i).getPointValue(j)) / rangeY;
                //scaleOffsetZeroX = (float)x1;
                //scaleOffsetZeroY = (float)y1;
                //x1 = x1*scaleWidth;
                //y1 = y1*scaleHeight;

                x2 = scaleWidth*(1.7f*((float)(pointData.getPointPackage(i + 1).getTime() - timeForX) / rangeX) - 0.9f);
                y2 = scaleHeight*(1.9f*pointData.getPointPackage(i + 1).getPointValue(j)) / rangeY;
                //x2 = x2*scaleWidth;
                //y2 = y2*scaleHeight;

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
            gl2_display.glVertex2f(1.7f*(rangeX/2 - delXRange*i - offsetSize + offsetDelX)/rangeX + 0.05f - scaleXZero + xOffset, -1.0f + scaleYZero);
            gl2_display.glVertex2f(1.7f*(rangeX/2 - delXRange*i - offsetSize + offsetDelX)/rangeX + 0.05f - scaleXZero + xOffset, 1.0f + scaleYZero);
        }
        gl2_display.glEnd();
    }

    private void xAxis(int x, int y){
        Float X;
        float x1, y1;
        gl2_display.glBegin(GL2.GL_LINES);
        gl2_display.glColor3f(0.0f, 0.0f, 0.0f);
        gl2_display.glVertex2f(-1.2f - scaleXZero + xOffset, -0.95f + scaleYZero); //-0.3f(x)
        gl2_display.glVertex2f(1.3f - scaleXZero + xOffset, -0.95f + scaleYZero); // +0.3f(x)
        gl2_display.glEnd();
        for (int i = -1; i <= delX + 1; i++){
            gl2_display.glColor3f(0.0f, 0.0f, 0.0f);
            x1 = 1.7f*(-rangeX/2 + delXRange*i - offsetSize + offsetDelX)/rangeX + 0.05f + xOffset;
            y1 = -0.95f + scaleYZero;
            gl2_display.glBegin(GL2.GL_LINES);
            gl2_display.glVertex2f(x1 - scaleXZero, y1);
            gl2_display.glVertex2f(x1 - scaleXZero, y1 + 0.02f);
            gl2_display.glEnd();
            renderer.beginRendering(x, y);
            renderer.setColor(0.0f, 0.0f, 0.0f, 1);
            //X = timeX + i*0.5f + timeOffset; scaleTime
            //X = timeX + scaleTimeOffset + i * scaleTime/scaleWidth + timeOffset/scaleWidth;
            //X = timeX;
            //renderer.draw(X.toString(), (int)(x*(x1+1)/2 - X.toString().length()*3), 5);
           /* if (scaleWidth > 1.0f){
                X = timeX + scaleTimeOffset + i * scaleTime/scaleWidth + timeOffset/scaleWidth;
            }
            else{
                if (scaleWidth < 1.0f){
                    X = timeX - scaleTimeOffset + i * scaleTime/scaleWidth + timeOffset/scaleWidth;
                }
                else{
                    X = timeX + i * scaleTime/scaleWidth + timeOffset/scaleWidth;
                }
            }*/
            //X = timeX + scaleTimeOffset + i * scaleTime/scaleWidth + timeOffset/scaleWidth;
            X = timeX + i * scaleTime/scaleWidth + timeOffset/scaleWidth;
            renderer.draw(X.toString(), (int)(x*(x1+1)/2 - X.toString().length()*3), 20);
            X = timeX;
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
        for (int i = -1; i <= delY + 2; i++){
            gl2_display.glVertex2f(-1.0f - scaleXZero, 1.9f*(float)((yMin + delYRange*i)/rangeY) - yOffset + scaleYZero);
            gl2_display.glVertex2f(1.0f - scaleXZero, 1.9f*(float)((yMin + delYRange*i)/rangeY) - yOffset + scaleYZero);
        }
        gl2_display.glEnd();
    }

    private void yAxis(int x, int y){
        float x1, y1;
        Double X;
        gl2_display.glBegin(GL2.GL_LINES);
        gl2_display.glColor3f(0.0f, 0.0f, 0.0f);
        gl2_display.glVertex2f(-0.9f - scaleXZero, -0.95f + scaleYZero);
        gl2_display.glVertex2f(-0.9f - scaleXZero, 0.95f + scaleYZero);
        gl2_display.glEnd();
        for (int i = -1; i <= delY + 2; i++){
            gl2_display.glColor3f(0.0f, 0.0f, 0.0f);
            gl2_display.glBegin(GL2.GL_LINES);
            x1 = -0.9f - scaleXZero;
            y1 = 1.9f*(float)((yMin + delYRange*i)/rangeY) - yOffset;
            gl2_display.glVertex2f(x1, y1 + scaleYZero);
            gl2_display.glVertex2f(x1 + 0.01f, y1 + scaleYZero);
            gl2_display.glEnd();
            renderer.beginRendering(x, y);
            renderer.setColor(0.0f, 0.0f, 0.0f, 1);
            X = (yMin + rangeY/delY*i + textOffset)/scaleHeight;
            renderer.draw(df.format(X), 5, (int)(y*(y1+1)/2 - 5));
            renderer.endRendering();
        }
    }

    private void printLabel(int x, int y){
        gl2_display.glColor3f(1.0f,1.0f,1.0f);
        gl2_display.glBegin(GL2.GL_QUADS);
        gl2_display.glVertex2f(0.8f - scaleXZero, 1.0f + scaleYZero);
        gl2_display.glVertex2f( 1.0f - scaleXZero, 1.0f + scaleYZero);
        gl2_display.glVertex2f( 1.0f - scaleXZero,-1.0f + scaleYZero);
        gl2_display.glVertex2f( 0.8f - scaleXZero,-1.0f + scaleYZero);
        gl2_display.glEnd();

        renderer.setColor(0.0f, 0.0f, 0.0f, 1);
        for (int i = 0; i < labelList.size(); i++){
            renderer.beginRendering(x, y);
            renderer.draw(labelList.get(i).getLabel(), (int)((x*0.9f) + 5), y - 30 - 40*i);
            renderer.endRendering();
            setColor(gl2_display.getGL2(), labelList.get(i).getColor());
            gl2_display.glBegin(GL2.GL_QUADS);
            gl2_display.glVertex2f( 0.8f - scaleXZero + 1.9f*5/width, 1.0f + scaleYZero - 2.0f*(40 + 40*i)/height);
            gl2_display.glVertex2f( 0.8f - scaleXZero + 1.9f*45/width, 1.0f + scaleYZero - 2.0f*(40 + 40*i)/height);
            gl2_display.glVertex2f( 0.8f - scaleXZero + 1.9f*45/width, 1.0f + scaleYZero - 2.0f*(50 + 40*i)/height);
            gl2_display.glVertex2f( 0.8f - scaleXZero + 1.9f*5/width, 1.0f + scaleYZero - 2.0f*(50 + 40*i)/height);
            gl2_display.glEnd();
        }


    }

    private static void setColor(GL2 gl, Color color){
        gl.glColor3f(color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f);
    }

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
        delXRange = rangeX / delX;
        delYRange = rangeY / delY;
        scaleTime = delXRange/1000.0f;
        offsetDelX = timeForX % delXRange;

    }


    public void start(GraphFrame b, PointData data){
        df = new DecimalFormat("#.##");
        labelList = new LinkedList<LabelList>();
        delY = 10;
        timeStop = -rangeX / 1000;
        pointData = data;
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
                pressedYZero = e.getY();
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
                    pressedY = e.getY();
                    scaleY = 2.0f*(pressedY - pressedYZero)/(height);
                    scaleX = 2.0f*(pressedX - pressedXZero)/(width);
                    flagGLCanvas = true;
                }
            }

            public void mouseMoved(MouseEvent e) {

            }
        });
        glcanvas.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (flagStop) {
                    int notches = e.getWheelRotation();
                    //scaleOffsetWheelX = 0.1f*e.getX()/(width);
                    //scaleOffsetWheelY = 0.1f*e.getY()/(height);
                    if (notches < 0) {
                        //scaleOffsetWheelX = scaleOffsetZeroX*1.1f - scaleOffsetZeroX;
                        //scaleOffsetWheelY = scaleOffsetZeroY*1.1f - scaleOffsetZeroY;
                        scaleOffsetWheelX = 2.0f * ((e.getX() - (width / 2 + width * scaleXZero / 2)) * 2.0f - (e.getX() - (width / 2 + width * scaleXZero / 2))) / (width);
                        scaleOffsetWheelY = 2.0f * ((e.getY() - (height / 2 + height * scaleYZero / 2)) * 2.0f - (e.getY() - (height / 2 + height * scaleYZero / 2))) / (height);
                        scaleWidth = scaleWidth * 2.0f;
                        scaleHeight = scaleHeight * 2.0f;
                    } else {
                        scaleOffsetWheelX = 2.0f * ((e.getX() - (width / 2 + width * scaleXZero / 2)) / 2.0f - (e.getX() - (width / 2 + width * scaleXZero / 2))) / (width);
                        scaleOffsetWheelY = 2.0f * ((e.getY() - (height / 2 + height * scaleYZero / 2)) / 2.0f - (e.getY() - (height / 2 + height * scaleYZero / 2))) / (height);
                        scaleWidth = scaleWidth / 2.0f;
                        scaleHeight = scaleHeight / 2.0f;

                    }/*
                    Math.abs(1.7f*(-rangeX/2 - offsetSize + offsetDelX)/rangeX + 0.05f + xOffset) + scaleOffsetWheelX;
                    //1.7f*(-rangeX/2 - offsetSize + offsetDelX)/rangeX + 0.05f + xOffset
                    //scaleTimeOffset = scaleOffsetWheelX
                    //scaleTimeOffset = -rangeX/2000.0f/scaleWidth;
                    //scaleTimeOffset = rangeX / scaleWidth / 1000;*/
                    //timeX = rangeX*(2.0f*(timeX*1000 + rangeX)/rangeX + (0.9f + scaleOffsetWheelX)/scaleWidth)/2.0f/1000;
                }


                //scaleWidth = scaleWidth;
                flagGLCanvas = true;
            }
        });
        final JFrame frame = new JFrame ("Graph Frame");

        final JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flagStop = !flagStop;
                if (flagStop) {
                    stopButton.setText("Run");
                }
                else {stopButton.setText("Stop");}
            }
        });

        final JButton returnTranslated = new JButton("Return");
        returnTranslated.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flagGLCanvas = true;
                flagPopMatrix = true;
                scaleXZero = 0;
                scaleYZero = 0;
                //xOffset = 0;
                yOffset = 0;
                textOffset = 0;
                timeOffset = 0;
                scaleOffsetY = (float)(1.9f*(yMin + rangeY / 2)/rangeY);
                scaleOffsetWheelX = 0.0f;
                scaleOffsetWheelY = 0.0f;
                scaleWidth = 1.0f;
                scaleHeight = 1.0f;
            }
        });

        final JButton scale = new JButton("Scale");
        scale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //double min, max;
                if(pointData.getSize() != 0) {
                    /*min = pointData.getMin();
                    max = pointData.getMax();
                    scaleOffsetY = (float)(yMin - min + ((yMax - max) - (yMin - min))/2);*/
                    //scaleOffsetY = 1.9f*scaleOffsetY/((float)rangeY);
                    flagGLCanvas = true;
                    flagPopMatrix = true;
                    scaleXZero = 0.0f;
                    scaleYZero = 0.0f;
                    //xOffset = 0;
                    yOffset = 0;
                    textOffset = 0;
                    timeOffset = 0;
                    yMax = pointData.getMax();
                    yMin = pointData.getMin();
                    rangeY = yMax - yMin;
                    scaleOffsetY = (float)(1.9f*(yMin + rangeY / 2)/rangeY);
                    scaleOffsetWheelX = 0.0f;
                    scaleOffsetWheelY = 0.0f;
                    //scaleOffsetY = (float)(1.9f*scaleOffsetY/(rangeY));
                    delYRange = rangeY / delY;

                    //delYRange = rangeY / delY;
                    //changeRange();
                    //flagGLCanvas = true;
                }
            }
        });

        final Timer updateTimer = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeZero = System.currentTimeMillis() - rangeX;
                offsetSizeZero = timeZero % delXRange;
                if (offsetSizeZero < gmaMax) {
                    timeStop +=(float)rangeX/(delX*1000);
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


        rangeXText.setText(String.valueOf(rangeX/1000));
        rangeXText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rangeX = Long.parseLong(rangeXText.getText()) * 1000;
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

        rangeYText.setText(String.valueOf(rangeY));
        rangeYText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rangeY = Double.parseDouble(rangeYText.getText());
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
                yOffset = 0;
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
        timeForX = System.currentTimeMillis() - rangeX;
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