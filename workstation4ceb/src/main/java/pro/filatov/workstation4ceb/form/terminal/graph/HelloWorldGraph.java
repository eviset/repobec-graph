package pro.filatov.workstation4ceb.form.terminal.graph;

        import de.erichseifert.gral.data.Column;
        import de.erichseifert.gral.data.DataTable;
        import de.erichseifert.gral.data.statistics.Statistics;
        import de.erichseifert.gral.graphics.Insets2D;
        import de.erichseifert.gral.plots.XYPlot;
        import de.erichseifert.gral.ui.InteractivePanel;
        import pro.filatov.workstation4ceb.model.Model;
        import pro.filatov.workstation4ceb.model.fpga.Terminal.TerminalModel;

        import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.WindowAdapter;
        import java.awt.event.WindowEvent;
        import java.util.Random;

/**
 * Created by user on 19.07.2017.
 */
public class HelloWorldGraph extends JFrame{

    final JFrame graph;
    private JButton runStopButton;
    private XYPlot plot;
    private InteractivePanel interactivePanel;
   // private DataTable

    public HelloWorldGraph(){
        graph = new JFrame("Hello World");
    }

    public static void main(String[] args){

        XYPlot plot = new XYPlot();

        //creating frame
        final JFrame frame = new JFrame (" Basic Frame");

        //adding canvas to frame
     /*   frame.add(glcanvas);
        frame.setSize( 640, 480 );
        frame.setVisible(true);*/
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
        InteractivePanel plotPanel = new InteractivePanel(plot);
        frame.add(plotPanel, BorderLayout.CENTER);
    }

}