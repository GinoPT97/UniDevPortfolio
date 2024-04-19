package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class FocusEventDemo extends JFrame
{
    private JTextField input_f;
    private JTextField input_t;

    public FocusEventDemo()
    {
        super("FocusEvent Demo");

        final String text = "Windows 7 VS Linux";
        
        setLayout(new FlowLayout(FlowLayout.LEFT)); // layout manager

        // componenti
        input_f = new JTextField(text, 26);
        input_t = new JTextField(26);

        // aggiungo i componenti al container
        add(input_f);
        add(input_t);

        input_t.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent e)
            {
                // il componente che ha perso il focus
                JTextField tf = (JTextField) e.getOppositeComponent();
                input_t.setText(tf.getText());

                tf.setText("");
            }

            public void focusLost(FocusEvent e)
            {
                input_f.setText(text);
            }
        });
    }

    public static void main(String args[])
    {
        // creo la finestra
        FocusEventDemo window = new FocusEventDemo();

        window.setSize(310, 120);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
