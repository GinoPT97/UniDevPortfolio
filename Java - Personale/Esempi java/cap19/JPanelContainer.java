package com.pellegrinoprincipe;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;

public class JPanelContainer extends JFrame
{
    private JLabel label_top;
    private JLabel label_bottom;
    private JTextField text_top;
    private JTextField text_bottom;
    private JButton button;
    private JPanel panel_top;
    private JPanel panel_bottom;
    private JPanel panel_button;

    public JPanelContainer()
    {
        super("JPanel container DEMO");

        label_top = new JLabel("Digita il codice: ");
        text_top = new JTextField("****************");

        label_bottom = new JLabel("Digita il secondo codice: ");
        text_bottom = new JTextField("******************");

        button = new JButton("Invia...");

        // creazione dei pannelli
        panel_top = new JPanel();
        panel_top.setBackground(Color.GRAY);
        panel_top.add(label_top);
        panel_top.add(text_top);

        panel_bottom = new JPanel();
        panel_bottom.setBackground(Color.MAGENTA);
        BoxLayout bl = new BoxLayout(panel_bottom, BoxLayout.PAGE_AXIS);
        panel_bottom.setLayout(bl);
        panel_bottom.add(label_bottom);
        panel_bottom.add(text_bottom);

        panel_button = new JPanel();
        panel_button.setBackground(Color.YELLOW);
        panel_button.add(button);

        // aggiungo i componenti alla finestra
        add(panel_top, BorderLayout.PAGE_START);
        add(panel_bottom, BorderLayout.CENTER);
        add(panel_button, BorderLayout.PAGE_END);
    }

    public static void main(String args[])
    {
        JPanelContainer window = new JPanelContainer();

        window.setSize(600, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
