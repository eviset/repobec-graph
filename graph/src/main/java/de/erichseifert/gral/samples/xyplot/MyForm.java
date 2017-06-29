package de.erichseifert.gral.samples.xyplot;

/**
 * Created by user on 08.02.2017.
 */
import javax.swing.JDialog;
import javax.swing.JButton;

public class  MyForm {

    public static void main(String[] args) {

        // Dialog
        JDialog dialog = new JDialog();
        dialog.setSize(316, 145);
        dialog.setLocation(300,200);
        dialog.getContentPane().setLayout(null);

        // Button
        JButton btnOk = new JButton("OK");
        btnOk.setBounds(108, 54, 89, 23);
        dialog.getContentPane().add(btnOk);
        dialog.setVisible(true);

    }

}