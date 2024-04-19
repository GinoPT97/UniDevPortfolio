package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class CaretEventDemo extends JFrame
{
    private JTextField input;
    private JTextArea area;

    public CaretEventDemo()
    {
        super("CaretEvent Demo");
        
        setLayout(new FlowLayout(FlowLayout.LEFT)); // layout manager

        // componenti
        area = new JTextArea(4, 26);
        input = new JTextField(26);

        // aggiungo i componenti al container
        add(area);
        add(input);

        area.addCaretListener(new CaretListener()
        {
            public void caretUpdate(CaretEvent e)
            {
                input.setText("Posizione caret: " + e.getDot());
            }
        });
    }

    public static void main(String args[])
    {
        // creo la finestra
        CaretEventDemo window = new CaretEventDemo();

        window.setSize(310, 150);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
