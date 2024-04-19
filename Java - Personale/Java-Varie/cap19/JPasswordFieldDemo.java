package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

public class JPasswordFieldDemo extends JFrame
{
    private JLabel label = new JLabel("Digitare la password"); // label    
    private JPasswordField pwd = new JPasswordField(10); // password field    
    private JButton button_ok = new JButton("OK"); // pulsante

    public JPasswordFieldDemo()
    {
        super("JPasswordField DEMO");

        setLayout(new FlowLayout());

        pwd.setEchoChar('^'); // cambiamo il carattere di echo del password field

        // aggiungiamo i componenti al container
        add(label);
        add(pwd);
        add(button_ok);
    }

    public static void main(String args[])
    {
        JPasswordFieldDemo window = new JPasswordFieldDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 80);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
