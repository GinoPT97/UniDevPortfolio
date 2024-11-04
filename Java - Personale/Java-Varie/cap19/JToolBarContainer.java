package com.pellegrinoprincipe;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

public class JToolBarContainer extends JFrame
{
    private JToolBar toolbar;

    public JToolBarContainer()
    {
        super("JToolBar container DEMO");

        JTextArea text_area = new JTextArea(40, 40);
       
        toolbar = new JToolBar("Agenda toolbar"); // creo il container JToolBar

        // aggiungo i componenti alla toolbar
        toolbar.add(new JButton(getIcon("open.png")));
        toolbar.add(new JButton(getIcon("contact.png")));
        toolbar.add(new JButton(getIcon("find.png")));
        toolbar.add(new JButton(getIcon("trash.png")));
        toolbar.addSeparator();
        toolbar.add(new JButton(getIcon("exit.png")));

        // aggiungo i componenti al JFrame
        add(toolbar, BorderLayout.PAGE_START);
        add(text_area, BorderLayout.CENTER);
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
        JToolBarContainer window = new JToolBarContainer();

        window.setSize(400, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
