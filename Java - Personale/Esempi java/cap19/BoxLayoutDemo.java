package com.pellegrinoprincipe;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

public class BoxLayoutDemo extends JFrame
{
    private JButton[] buttons = new JButton[5];
    private BoxLayout box_layout;

    public BoxLayoutDemo()
    {
        super("BoxLayout DEMO");

        // creo il layout manager e lo assegno al container
        box_layout = new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS);
        setLayout(box_layout);

        for (int i = 0; i < buttons.length; i++) // aggiungo dei componenti al container
        {
            buttons[i] = new JButton("PULSANTE " + i);
            add(buttons[i]);

            if (i > 1)
                add(Box.createVerticalGlue());
            else
                add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    public static void main(String args[])
    {
        BoxLayoutDemo window = new BoxLayoutDemo();

        window.setSize(300, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
