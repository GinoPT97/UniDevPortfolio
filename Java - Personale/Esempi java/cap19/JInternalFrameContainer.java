package com.pellegrinoprincipe;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;

public class JInternalFrameContainer extends JFrame
{
    // controlli text area
    private JTextArea edit_1;
    private JTextArea edit_2;
    // finestre interne
    private JInternalFrame inner_window_1;
    private JInternalFrame inner_window_2;

    public JInternalFrameContainer()
    {
        super("JInternalFrame container DEMO");

        // creo le text area
        edit_1 = new JTextArea(100, 200);
        edit_2 = new JTextArea(130, 220);

        // creo le finestre interne
        inner_window_1 = new JInternalFrame("EDIT 1", true, true, true, true);
        inner_window_2 = new JInternalFrame("EDIT 2", true, true, true, true);

        // aggiungo le text area alle finestre interne
        inner_window_1.add(edit_1);
        inner_window_2.add(edit_2);

        // creo un oggetto desktop pane e gli aggiungo le finestre interne
        JDesktopPane dpane = new JDesktopPane();
        dpane.add(inner_window_1);
        dpane.add(inner_window_2);
        
        add(dpane); // aggiungo al JFrame il desktop pane

        // rendo visibili le finestre interne e le posiziono
        inner_window_1.setLocation(0, 0);
        inner_window_1.setSize(400, 400);
        inner_window_1.setVisible(true);

        inner_window_2.setLocation(300, 0);
        inner_window_2.setSize(400, 400);
        inner_window_2.setVisible(true);
    }

    public static void main(String args[])
    {
        JInternalFrameContainer window = new JInternalFrameContainer();

        window.setSize(820, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
