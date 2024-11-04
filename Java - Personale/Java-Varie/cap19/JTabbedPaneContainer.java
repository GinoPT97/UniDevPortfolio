package com.pellegrinoprincipe;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

public class JTabbedPaneContainer extends JFrame
{
    private JLabel label_tab_green;
    private JLabel label_tab_red;
    private JLabel label_tab_yellow;
    private JPanel panel_green;
    private JPanel panel_red;
    private JPanel panel_yellow;
    private JTabbedPane tab_pane;

    public JTabbedPaneContainer()
    {
        super("JTabbedPane container DEMO");

        // path delle immagini
        URL icon_green_url = getClass().getResource("book_green.png");
        URL icon_yellow_url = getClass().getResource("book_yellow.png");
        URL icon_red_url = getClass().getResource("book_red.png");

        // immagini da visualizzare
        ImageIcon icon_green = new ImageIcon(icon_green_url);
        ImageIcon icon_yellow = new ImageIcon(icon_yellow_url);
        ImageIcon icon_red = new ImageIcon(icon_red_url);

        label_tab_green = new JLabel("Lorem ipsum dolor sit amet, consectetur");
        label_tab_yellow = new JLabel("Sed ut perspiciatis unde omnis iste natus error");
        label_tab_red = new JLabel("At vero eos et accusamus et iusto odio dignissimos ducimus");

        panel_green = new JPanel();
        panel_green.add(label_tab_green);

        panel_yellow = new JPanel();
        panel_yellow.add(label_tab_yellow);

        panel_red = new JPanel();
        panel_red.add(label_tab_red);

        // creo il container a tab
        tab_pane = new JTabbedPane();
        tab_pane.setPreferredSize(new Dimension(600, 200));
        tab_pane.addTab("Green book...", icon_green, panel_green, "a green book Lorem...");
        tab_pane.addTab("Yellow book...", icon_yellow, panel_yellow, "a yellow book Sed ut...");
        tab_pane.addTab("Red book...", icon_red, panel_red, "a red book Sed At vero eos...");

        tab_pane.setMnemonicAt(0, KeyEvent.VK_G);
        tab_pane.setMnemonicAt(1, KeyEvent.VK_Y);
        tab_pane.setMnemonicAt(2, KeyEvent.VK_R);

        add(tab_pane, BorderLayout.PAGE_START);
    }

    public static void main(String args[])
    {
        JTabbedPaneContainer window = new JTabbedPaneContainer();

        window.setSize(600, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
