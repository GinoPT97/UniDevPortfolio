package com.pellegrinoprincipe;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class JButtonDemo extends JFrame
{
    private JButton btn_search;
    private JButton btn_open;

    public JButtonDemo()
    {
        super("JButton DEMO");

        setLayout(new FlowLayout());

        String msg_s = "Inizia la ricerca";
        String msg_o = "Apri la cartella";

        // creo i pulsanti
        btn_search = new JButton(msg_s, getIcon("find.png"));
        btn_search.setHorizontalTextPosition(SwingConstants.LEADING);
        btn_search.setVerticalTextPosition(SwingConstants.CENTER);
        // gli assegno un mnemonico
        btn_search.setMnemonic(KeyEvent.VK_R);

        btn_open = new JButton(msg_o, getIcon("open.png"));
        btn_open.setHorizontalTextPosition(SwingConstants.CENTER);
        btn_open.setVerticalTextPosition(SwingConstants.BOTTOM);
        // lo rendo disattivato
        btn_open.setEnabled(false);

        // aggiungo i componenti al JFrame
        add(btn_search);
        add(btn_open);
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
        JButtonDemo window = new JButtonDemo();

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
