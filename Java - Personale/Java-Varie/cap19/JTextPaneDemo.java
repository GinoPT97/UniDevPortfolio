package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class JTextPaneDemo extends JFrame
{
    // JTextPane
    private JTextPane tpane;

    public JTextPaneDemo()
    {
        super("JTextPane DEMO");
        
        tpane = new JTextPane(); // istanzio in text pane

        // creo un set di attributi
        SimpleAttributeSet red = new SimpleAttributeSet();
        StyleConstants.setForeground(red, Color.RED);
        StyleConstants.setBackground(red, Color.BLACK);
        StyleConstants.setFontFamily(red, "Serif");
        StyleConstants.setFontSize(red, 32);

        SimpleAttributeSet italic = new SimpleAttributeSet();
        StyleConstants.setForeground(italic, Color.GREEN);
        StyleConstants.setBackground(italic, Color.WHITE);
        StyleConstants.setFontFamily(italic, "Monospaced");
        StyleConstants.setFontSize(italic, 40);
        StyleConstants.setItalic(italic, true);
        
        StyledDocument sd = tpane.getStyledDocument(); // ottengo il "modello" associato all'editor
       
        try // inserisco il testo con la formattazione personalizzata
        {
            sd.insertString(sd.getLength(), "Java 7 è formidabile!!!!", red);
            sd.insertString(sd.getLength(), "\n... ma bisogna studiare molto ...", italic);
        }
        catch (BadLocationException ex)
        {
            JOptionPane.showMessageDialog(this, ex.toString());
        }
        
        add(tpane, BorderLayout.CENTER); // aggiungo al container il text pane
    }

    public static void main(String args[])
    {
        JTextPaneDemo window = new JTextPaneDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 300);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}