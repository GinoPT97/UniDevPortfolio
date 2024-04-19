package com.pellegrinoprincipe;

import javax.swing.JButton;
import javax.swing.JFrame;

public class AbsolutePositioningDemo extends JFrame
{
    private JButton button_top;
    private JButton button_left;
    private JButton button_right;
    private JButton button_bottom;

    public AbsolutePositioningDemo()
    {
        super("Absolute positioning DEMO");
        
        setLayout(null); // non imposto layout managers

        // vari pulsanti...
        button_top = new JButton("PULSANTE NELL'AREA SUPERIORE");
        button_left = new JButton("PULSANTE NELL'AREA SINISTRA");
        button_right = new JButton("PULSANTE NELL'AREA DESTRA");
        button_bottom = new JButton("PULSANTE NELL'AREA INFERIORE");

        // aggiungo i pulsanti al container
        add(button_top);
        add(button_bottom);
        add(button_left);
        add(button_right);

        // imposto posizione e dimensione dei pulsanti
        button_top.setBounds(200, 10, 300, 30);
        button_left.setBounds(10, 60, 300, 30);
        button_right.setBounds(350, 60, 300, 30);
        button_bottom.setBounds(200, 250, 300, 30);
    }
    
    public static void main(String args[])
    {
        AbsolutePositioningDemo window = new AbsolutePositioningDemo();

        // imposto le dimensioni della finestra
        window.setSize(750, 400);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
