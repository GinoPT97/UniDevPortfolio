package com.pellegrinoprincipe;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.FlowLayout;

public class FlowLayoutDemo extends JFrame
{
    private JButton[] buttons = new JButton[7];

    public FlowLayoutDemo()
    {
        super("FlowLayout DEMO");

        // creo 7 pulsanti
        for (int i = 0; i < 7; i++)
        {
            buttons[i] = new JButton("Pulsante " + i);
        }

        // imposto il layout manager
        setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10));

        // aggiungo i pulsanti al container
        for (int i = 0; i < 7; i++)
        {
            add(buttons[i]);
        }
    }

    public static void main(String args[])
    {
        FlowLayoutDemo window = new FlowLayoutDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 200);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
