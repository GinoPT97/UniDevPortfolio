package com.pellegrinoprincipe;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class JSplitPaneContainer extends JFrame
{
    private ImageIcon stallman_img;
    private ImageIcon gosling_img;
    private JScrollPane scroll_pane_stallman;
    private JScrollPane scroll_pane_gosling;
    private JSplitPane split_pane;

    public JSplitPaneContainer()
    {
        super("JSplitPane container DEMO");

        // path delle immagini
        URL stallman_url = getClass().getResource("stallman.jpg");
        URL gosling_url = getClass().getResource("gosling.jpg");

        // immagini da visualizzare
        stallman_img = new ImageIcon(stallman_url);
        gosling_img = new ImageIcon(gosling_url);

        scroll_pane_stallman = new JScrollPane(new JLabel(stallman_img));
        scroll_pane_gosling = new JScrollPane(new JLabel(gosling_img));

        // creo il container ad aree separate
        split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll_pane_gosling, scroll_pane_stallman);
        split_pane.setDividerLocation(180);
        split_pane.setOneTouchExpandable(true);

        add(split_pane, BorderLayout.CENTER);
    }

    public static void main(String args[])
    {
        JSplitPaneContainer window = new JSplitPaneContainer();

        window.setSize(600, 500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
