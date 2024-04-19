package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class JTextAreaDemo extends JFrame
{
    // text area
    private JTextArea text_area;

    public JTextAreaDemo()
    {
        super("JTextArea DEMO");

        String text = "[thp@xdevel]$ ls -la | grep java";
        
        text_area = new JTextArea(text, 20, 80); // controllo text area

        // imposto le proprietà di visualizzazione del 
        // controllo
        text_area.setBackground(Color.BLACK);
        text_area.setForeground(Color.GREEN);
        text_area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        text_area.setCaretColor(Color.GREEN);

        // quando una parola raggiunge il bordo destro del controllo
        // vai a capo
        text_area.setLineWrap(true);

        // vai a capo in base alle parole e non ai caratteri
        text_area.setWrapStyleWord(true);

        // spostiamo la posizione del cursore
        text_area.setCaretPosition(text.length());

        add(text_area, BorderLayout.LINE_START);
    }

    public static void main(String args[])
    {
        JTextAreaDemo window = new JTextAreaDemo();

        // imposto le dimensioni della finestra
        window.setSize(655, 150);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
