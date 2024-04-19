package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;

public class GridBagLayoutDemo extends JFrame
{
    private JButton[] buttons = new JButton[2];
    private JTextField[] text_fields = new JTextField[2];
    private JPanel box = new JPanel();

    public GridBagLayoutDemo()
    {
        super("GridBagLayout DEMO");
        
        for (int i = 0; i < 2; i++) // creo 2 pulsanti
            buttons[i] = new JButton("Pulsante " + i);
               
        for (int i = 0; i < 2; i++) // creo 2 campi di testo
            text_fields[i] = new JTextField("Campo di testo " + i);
               
        box.setLayout(new GridBagLayout()); // imposto dimensione e layout manager per il pannello       
        GridBagConstraints constraints = new GridBagConstraints(); //  creo un oggetto per le regole

        // dispongo l'etichetta e l'aggiungo
        constraints.gridx = 0; // 1° colonna
        constraints.gridy = 0; // 1° riga
        constraints.weightx = 0.2; // distribuisci lo spazio in più in questa colonna
        box.add(new JLabel("...  testo 1 ..."), constraints);

        //  creo un oggetto per le regole
        constraints = new GridBagConstraints();
        // dispongo il primo pulsante e l'aggiungo al container
        constraints.gridx = 1; // 2° colonna
        constraints.gridy = 0; // 1° riga
        constraints.anchor = GridBagConstraints.LAST_LINE_END; // ancoraggio
        constraints.insets = new Insets(5, 0, 5, 0); // padding esterno
        constraints.weightx = 0.8; // distribuisci lo spazio in più in questa colonna
        buttons[0].setPreferredSize(new Dimension(25, 10)); // dimensioni del pulsante
        box.add(buttons[0], constraints);
        
        constraints = new GridBagConstraints(); //  creo un oggetto per le regole
        // dispongo l'etichetta e l'aggiungo
        constraints.gridx = 2; // 3° colonna
        constraints.gridy = 0; // 1° riga
        box.add(new JLabel("...  testo 2 ..."), constraints);
        
        constraints = new GridBagConstraints(); //  creo un oggetto per le regole
        // dispongo il secondo pulsante e l'aggiungo al container
        constraints.gridx = 0; // 1° colonna
        constraints.gridy = 1; // 2° riga
        constraints.fill = GridBagConstraints.HORIZONTAL; // estendo in larghezza il pulsante
        constraints.gridwidth = 2; // estendo per 2 colonne
        box.add(buttons[1], constraints);

        constraints = new GridBagConstraints(); //  creo un oggetto per le regole
        // dispongo la prima casella di testo e l'aggiungo al container
        constraints.gridx = 2; // 3° colonna
        constraints.gridy = 1; // 2° riga
        constraints.gridheight = 2; // estendo per 2 righe
        constraints.fill = GridBagConstraints.VERTICAL; // lo allargo in verticale
        box.add(text_fields[0], constraints);

        constraints = new GridBagConstraints(); //  creo un oggetto per le regole
        // dispongo la seconda casella di testo e l'aggiungo al container
        constraints.gridx = 0; // 1° colonna
        constraints.gridy = 2; // 3° riga
        box.add(text_fields[1], constraints);

        // dispongo l'etichetta e l'aggiungo
        constraints.gridx = 1; // 2° colonna
        constraints.gridy = 2; // 3° riga
        box.add(new JLabel("...  testo 3 ..."), constraints);

        add(box, BorderLayout.PAGE_START);
    }
    
    public static void main(String args[])
    {
        GridBagLayoutDemo window = new GridBagLayoutDemo();

        // imposto le dimensioni della finestra
        window.setSize(310, 200);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
