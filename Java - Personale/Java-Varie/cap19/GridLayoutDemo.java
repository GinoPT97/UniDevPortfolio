package com.pellegrinoprincipe;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.GridLayout;

public class GridLayoutDemo extends JFrame
{
    private JButton[] buttons = new JButton[10];

    public GridLayoutDemo()
    {
        super("GridLayout DEMO");
        
        for (int i = 0; i < 10; i++) // creo 7 pulsanti
            buttons[i] = new JButton("Pulsante " + i);
        
        setLayout(new GridLayout(5, 2, 10, 10)); // imposto il layout manager

        for (int i = 0; i < 10; i++) // aggiungo i pulsanti al container
            add(buttons[i]);
    }

    public static void main(String args[])
    {
        GridLayoutDemo window = new GridLayoutDemo();

        // imposto le dimensioni della finestra
        window.setSize(500, 400);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
