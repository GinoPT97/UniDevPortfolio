package com.pellegrinoprincipe;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JCheckBox;

public class JCheckBoxlDemo extends JFrame
{
    private JCheckBox[] check_boxes = new JCheckBox[5];
    private String[] colors = {"RED", "GREEN", "BLUE", "YELLOW", "MAGENTA"};

    public JCheckBoxlDemo()
    {
        super("JCheckBox DEMO");

        setLayout(new GridLayout(0, 1)); // layout con 0 righe e una colonna

        // creo i check boxes e li aggiungo al container
        for (int i = 0; i < check_boxes.length; i++)
        {
            check_boxes[i] = new JCheckBox(colors[i], i % 2 == 0 ? true : false);
            add(check_boxes[i]);
        }
    }

    public static void main(String args[])
    {
        JCheckBoxlDemo window = new JCheckBoxlDemo();

        // imposto le dimensioni della finestra
        window.setSize(250, 200);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
