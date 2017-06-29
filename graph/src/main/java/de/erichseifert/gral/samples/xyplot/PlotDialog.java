package de.erichseifert.gral.samples.xyplot;

import javax.swing.*;
import java.awt.*;

/**
 * Created by user on 08.02.2017.
 */
public class PlotDialog extends JDialog {

    public PlotDialog() {

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("About Us");
        setBounds(100, 100, 359, 174);
        getContentPane().setLayout(null);

        // Label
        JLabel label = new JLabel("ThaiCreate.Com Version 1.0");
        label.setBounds(86, 37, 175, 29);
        getContentPane().add(label);

    }

}
