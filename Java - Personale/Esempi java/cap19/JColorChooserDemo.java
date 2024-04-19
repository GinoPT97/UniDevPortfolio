package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

public class JColorChooserDemo extends JFrame
{    
    private JColorChooser cc; // JColorChooser

    public JColorChooserDemo()
    {
        super("JColorChooser DEMO");
        
        cc = new JColorChooser(Color.yellow); // color chooser
        add(cc, BorderLayout.CENTER);
    }

    public static void main(String args[])
    {
        JColorChooserDemo window = new JColorChooserDemo();

        // imposto le dimensioni della finestra
        window.setSize(650, 350);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
