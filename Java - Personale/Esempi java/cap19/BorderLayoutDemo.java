package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class BorderLayoutDemo extends JFrame
{
    private JButton button_top;
    private JButton button_left;
    private JButton button_right;
    private JButton button_bottom;
    private JButton button_center;
    private BorderLayout border_layout;

    public BorderLayoutDemo()
    {
        super("BorderLayout DEMO");
        
        border_layout = new BorderLayout(3, 3); // creo il layout manager e lo assegno al container
        setLayout(border_layout);

        // vari pulsanti...
        button_top = new JButton("PULSANTE NELL'AREA SUPERIORE");
        button_left = new JButton("PULSANTE NELL'AREA SINISTRA");
        button_right = new JButton("PULSANTE NELL'AREA DESTRA");
        button_bottom = new JButton("PULSANTE NELL'AREA INFERIORE");
        button_center = new JButton("PULSANTE NELL'AREA CENTRALE");

        // aggiungo i pulsanti al container
        add(button_top, BorderLayout.PAGE_START);
        add(button_bottom, BorderLayout.PAGE_END);
        add(button_center, BorderLayout.CENTER);
        add(button_left, BorderLayout.LINE_START);
        add(button_right, BorderLayout.LINE_END);
    }

    public static void main(String args[])
    {
        BorderLayoutDemo window = new BorderLayoutDemo();

        window.setSize(800, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
