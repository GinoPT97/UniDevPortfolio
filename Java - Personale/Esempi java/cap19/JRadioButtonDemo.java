package com.pellegrinoprincipe;

import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

public class JRadioButtonDemo extends JFrame
{
    private JRadioButton[] radio_buttons = new JRadioButton[5];
    private String[] colors = {"RED", "GREEN", "BLUE", "YELLOW", "MAGENTA"};
    // per raggruppare i radio buttons
    private ButtonGroup button_group = new ButtonGroup();

    public JRadioButtonDemo()
    {
        super("JRadioButton DEMO");
        
        setLayout(new GridLayout(0, 1)); // layout con 0 righe e una colonna

        // creo i radio button e li aggiungo al container
        for (int i = 0; i < radio_buttons.length; i++)
        {
            radio_buttons[i] = new JRadioButton(colors[i]);
            button_group.add(radio_buttons[i]);
            add(radio_buttons[i]);
        }
        
        radio_buttons[0].setSelected(true); // seleziono il primo radio
    }

    public static void main(String args[])
    {
        JRadioButtonDemo window = new JRadioButtonDemo();

        // imposto le dimensioni della finestra
        window.setSize(250, 200);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
