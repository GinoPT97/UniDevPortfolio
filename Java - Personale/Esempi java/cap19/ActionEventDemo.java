package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ActionEventDemo extends JFrame
{
    private JLabel label;
    private JTextField input;
    private JTextArea area;
    private JButton button;

    public ActionEventDemo()
    {
        super("ActionEvent Demo");
        
        setLayout(new FlowLayout(FlowLayout.LEFT)); // layout manager

        // componenti
        button = new JButton("Accedi all'area riservata...");
        area = new JTextArea(4, 26);

        // aggiungo i componenti al container
        add(button);
        add(area);
        
        button.addActionListener(new ActionListener() // action event per la casella di testo
        {
            public void actionPerformed(ActionEvent e)
            {                
                long time = e.getWhen(); // quando è occorso l'evento?

                // è stato premuto il modificatore CTRL?
                boolean mod = (e.getModifiers() & e.CTRL_MASK) == e.CTRL_MASK;

                area.setText("Label del pulsante: " + e.getActionCommand() +
                             "\nTimestamp:" + new Date(time) + "\nModificatore CTRL?: " + mod);
            }
        });
    }

    public static void main(String args[])
    {
        // creo la finestra
        ActionEventDemo window = new ActionEventDemo();

        window.setSize(310, 150);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
