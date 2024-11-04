package com.pellegrinoprincipe;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class JSeparatorDemo extends JFrame
{
    // label
    private JLabel lbl_name = new JLabel("Nome");
    private JLabel lbl_ID = new JLabel("ID");
    // text box
    private JTextField txt_name = new JTextField(25);
    private JTextField txt_id = new JTextField(25);
    // button
    private JButton btn_ok = new JButton("Ok");
    private JButton btn_cancel = new JButton("Cancel");
    // separatori
    private JSeparator vert_sep;
    private JSeparator[] horiz_sep = new JSeparator[2];

    public JSeparatorDemo()
    {
        super("JSeparator DEMO");

        setLayout(new FlowLayout(FlowLayout.TRAILING));

        // aggiungo i controlli...
        add(lbl_name);
        add(txt_name);

        // separatore orizzontale
        horiz_sep[0] = new JSeparator(JSeparator.HORIZONTAL);
        horiz_sep[0].setPreferredSize(new Dimension(320, 1));
        add(horiz_sep[0]);

        add(lbl_ID);
        add(txt_id);

        // altro separatore orizzontale
        horiz_sep[1] = new JSeparator(JSeparator.HORIZONTAL);
        horiz_sep[1].setPreferredSize(new Dimension(320, 1));
        add(horiz_sep[1]);

        add(btn_ok);

        // separatore verticale
        vert_sep = new JSeparator(JSeparator.VERTICAL);
        vert_sep.setPreferredSize(new Dimension(1, 25));
        add(vert_sep);

        add(btn_cancel);
    }

    public static void main(String args[])
    {
        JSeparatorDemo window = new JSeparatorDemo();

        // imposto le dimensioni della finestra
        window.setSize(350, 140);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
