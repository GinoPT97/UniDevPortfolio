package com.pellegrinoprincipe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

// listener con classe principale
public class SimpleEvents extends JFrame implements ActionListener
{
    private JButton button_1;
    private JButton button_2;
    private JButton button_3;

    public void actionPerformed(ActionEvent e)
    {        
        JButton who = (JButton) e.getSource(); // sorgente dell'evento
        JOptionPane.showMessageDialog(null, "Evento di uscita generato da: " + who.getText());        
        System.exit(0); // esci dall'applicazione
    }

    // listener con classe interna
    private class MyActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JOptionPane.showMessageDialog(null, "Ok il gioco sta per partire...");
        }
    }

    public SimpleEvents()
    {
        super("Simple events...");
        
        setLayout(null); // non imposto layout managers

        // vari pulsanti...
        button_1 = new JButton("Avvia il gioco...");
        button_2 = new JButton("Termina il gioco...");
        button_3 = new JButton("Esci dall'applicazione.");

        final MyActionListener mal = new MyActionListener();
        
        button_1.addActionListener(mal); // registro il listener

        // listener con classe anonima interna
        button_2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "Ok il gioco e' terminato...");               
                button_1.removeActionListener(mal); // rimuovo il listener
            }
        });

        button_3.addActionListener(this);

        // aggiungo i pulsanti al container
        add(button_1);
        add(button_2);
        add(button_3);

        // imposto posizione e dimensione dei pulsanti
        button_1.setBounds(100, 10, 300, 30);
        button_2.setBounds(100, 80, 300, 30);
        button_3.setBounds(100, 150, 300, 30);
    }

    public static void main(String args[])
    {
        SimpleEvents window = new SimpleEvents();

        // imposto le dimensioni della finestra
        window.setSize(600, 300);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
