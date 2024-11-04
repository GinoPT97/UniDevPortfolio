package com.pellegrinoprincipe;

import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class JAppletContainer extends JApplet
{
    private JLabel label;
    private JTextField text;
    private JButton button;
    private Container cpane;

    public void init()
    {
        // creo dei widgets
        label = new JLabel("Inserire il proprio ID:");
        text = new JTextField(25);
        button = new JButton("Procedere");
       
        cpane = getContentPane(); // ottengo il content pane       
        cpane.setLayout(new FlowLayout()); // imposto un layout manager

        // aggiungo i widgets al content pane
        cpane.add(label);
        cpane.add(text);
        cpane.add(button);
    }

    public void start() {}
    public void stop() {}
    public void destroy() {}
}
