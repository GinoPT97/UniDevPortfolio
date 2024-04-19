package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;

public class GroupLayoutDemo extends JFrame
{
    private JButton button_ok;
    private JButton button_cancel;
    private JTextField text_nome;
    private JTextField text_cognome;
    private JLabel lbl_nome;
    private JLabel lbl_cognome;
    private JPanel box = new JPanel();

    public GroupLayoutDemo()
    {
        super("GroupLayout DEMO");

        // creo i componenti grafici
        button_ok = new JButton("Ok");
        button_cancel = new JButton("Cancel");
        text_nome = new JTextField(40);
        text_cognome = new JTextField(40);
        lbl_nome = new JLabel("Nome: ");
        lbl_cognome = new JLabel("Cognome: ");

        GroupLayout group_layout = new GroupLayout(box);
       
        box.setLayout(group_layout); // imposto dimensione e layout manager per il pannello       
        group_layout.setAutoCreateGaps(true); // creo un gap automatico tra i componenti  
        // creo un gap automatico tra i componenti e i lati del container
        group_layout.setAutoCreateContainerGaps(true); 

        // definisco i criteri di allineamento
        GroupLayout.Alignment h_align = GroupLayout.Alignment.TRAILING;
        GroupLayout.Alignment v_align = GroupLayout.Alignment.BASELINE;

        // creo il gruppo sequenziale orizzontale
        GroupLayout.SequentialGroup for_horizontal = group_layout.createSequentialGroup();

        // aggiungo i gruppi
        for_horizontal.addGroup(group_layout.createParallelGroup(h_align).
                addComponent(lbl_nome).
                addComponent(lbl_cognome));
        for_horizontal.addGroup(group_layout.createParallelGroup(h_align).
                addComponent(text_nome).
                addComponent(text_cognome).
                addComponent(button_ok)).
                addComponent(button_cancel);
        group_layout.setHorizontalGroup(for_horizontal); // lo imposto

        // creo il gruppo sequenziale verticale
        GroupLayout.SequentialGroup for_vertical = group_layout.createSequentialGroup();

        // aggiungo i gruppi
        for_vertical.addGroup(group_layout.createParallelGroup(v_align).
                addComponent(lbl_nome).
                addComponent(text_nome));
        for_vertical.addGroup(group_layout.createParallelGroup(v_align).
                addComponent(lbl_cognome).
                addComponent(text_cognome));
        for_vertical.addGroup(group_layout.createParallelGroup(v_align).
                addComponent(button_ok).
                addComponent(button_cancel));
        group_layout.setVerticalGroup(for_vertical); // lo imposto

        add(box, BorderLayout.PAGE_START);
    }

    public static void main(String args[])
    {
        GroupLayoutDemo window = new GroupLayoutDemo();

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
