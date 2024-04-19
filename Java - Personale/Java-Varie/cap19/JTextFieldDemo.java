package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class JTextFieldDemo extends JFrame
{
    // array di text fields
    private JTextField[] text_fields = new JTextField[3];

    public JTextFieldDemo()
    {
        super("JTextField DEMO");

        // imposto il layout
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // creazione dei campi di testo
        text_fields[0] = new JTextField("Sono un campo di testo");

        text_fields[1] = new JTextField(25); // 25 colonne non editabile
        text_fields[1].setText("non sono editabile...");
        text_fields[1].setEditable(false);

        text_fields[2] = new JTextField("...digita...qui...", 30); // 30 colonne

        // creo i text fields e li aggiungo al container
        for (int i = 0; i < text_fields.length; i++)
            add(text_fields[i]);
    }

    public static void main(String args[])
    {
        JTextFieldDemo window = new JTextFieldDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 150);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
