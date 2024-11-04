package com.pellegrinoprincipe;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class JScrollPaneContainer extends JFrame
{
    private ImageIcon stallman_img;
    private JScrollPane scroll_pane;

    public JScrollPaneContainer()
    {
        super("JScrollPane container DEMO");
        
        URL stallman_url = getClass().getResource("stallman.jpg"); // path dell'immagine   
        stallman_img = new ImageIcon(stallman_url); // immagine da visualizzare       
        scroll_pane = new JScrollPane(new JLabel(stallman_img)); // creo il container con le scroll bars
        add(scroll_pane, BorderLayout.CENTER);
    }

    public static void main(String args[])
    {
        JScrollPaneContainer window = new JScrollPaneContainer();

        window.setSize(400, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
