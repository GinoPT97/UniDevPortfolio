package com.pellegrinoprincipe;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class JLabelDemo extends JFrame
{
    private JLabel only_text;
    private JLabel only_image;
    private JLabel text_and_image;

    public JLabelDemo()
    {
        super("JLabel DEMO");

        String msg = "Prego uscire dall'applicazione";

        ImageIcon icon = getIcon("exit.png");

        // label di solo testo
        only_text = new JLabel(msg);

        // label con solo un'immagine
        only_image = new JLabel(icon);
        only_image.setToolTipText(msg);

        // label con testo e immagine
        text_and_image = new JLabel(msg, icon, SwingConstants.LEADING);

        // imposto la posizione del testo relativamente all'icona
        text_and_image.setVerticalTextPosition(SwingConstants.BOTTOM);
        text_and_image.setHorizontalTextPosition(SwingConstants.CENTER);

        // aggiungo i componenti al JFrame
        add(only_text, BorderLayout.PAGE_START);
        add(only_image, BorderLayout.CENTER);
        add(text_and_image, BorderLayout.PAGE_END);
    }

    // ritorna un oggetto di tipo ImageIcon
    private ImageIcon getIcon(String name)
    {
        // path delle icone
        URL _url = getClass().getResource(name);

        // immagine da visualizzare
        return new ImageIcon(_url);
    }

    public static void main(String args[])
    {
        JLabelDemo window = new JLabelDemo();

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
