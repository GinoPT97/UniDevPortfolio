package com.pellegrinoprincipe;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class KeyEventDemo extends JFrame
{
    private JTextField input;
    private JTextArea area;
    
    private class MyKeyAdapter extends KeyAdapter // classe adapter
    {
        public void keyPressed(KeyEvent e)
        {
            area.setText("");
            String kt = e.getKeyText(e.getKeyCode());
            area.append("Key Pressed: " + kt);
        }

        public void keyTyped(KeyEvent e)
        {
            char c = e.getKeyChar();
            area.append("\nKey Typed: " + c);
        }
    }

    public KeyEventDemo()
    {
        super("KeyEvent Demo");
        
        setLayout(new FlowLayout(FlowLayout.LEFT)); // layout manager

        // componenti
        area = new JTextArea(4, 26);
        area.setEditable(false);
        area.setBackground(Color.GRAY);
        input = new JTextField(26);
        
        MyKeyAdapter mka = new MyKeyAdapter(); // listener
        input.addKeyListener(mka);

        // aggiungo i componenti al container
        add(input);
        add(area);
    }

    public static void main(String args[])
    {
        // creo la finestra
        KeyEventDemo window = new KeyEventDemo();

        window.setSize(330, 150);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
