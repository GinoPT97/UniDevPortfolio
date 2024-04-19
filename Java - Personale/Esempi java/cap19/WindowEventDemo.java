package com.pellegrinoprincipe;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class WindowEventDemo extends JFrame
{
    private JTextArea area;

    public WindowEventDemo(String title)
    {
        super(title);

        // componenti
        area = new JTextArea();
        area.setLineWrap(true);
        add(area);
        
        WindowAdapter MyWindowAdapter = new WindowAdapter() // window adapter
        {
            public void windowClosing(WindowEvent e)
            {
                int ans = JOptionPane.showConfirmDialog(null, "Chiudo la finestra?",
                        "ATTENZIONE", JOptionPane.YES_NO_OPTION);

                if (ans == JOptionPane.YES_OPTION)
                {
                    dispose();
                }
            }

            public void windowGainedFocus(WindowEvent e)
            {
                Window w = e.getOppositeWindow();

                if (w != null)
                    area.setText(w.toString());
            }

            public void windowStateChanged(WindowEvent e)
            {
                switch (e.getNewState())
                {
                    case Frame.ICONIFIED:                        
                        JOptionPane.showMessageDialog(null, "STOP"); // stop a work...
                        break;
                    case Frame.NORMAL:                        
                        JOptionPane.showMessageDialog(null, "RESUME"); // resume a work...
                        break;
                }
            }
        };

        // window listener
        addWindowListener(MyWindowAdapter);
        addWindowFocusListener(MyWindowAdapter);
        addWindowStateListener(MyWindowAdapter);
    }

    public static void main(String args[])
    {
        // creo 2 finestre
        WindowEventDemo window = new WindowEventDemo("Finestra 1");
        WindowEventDemo other_window = new WindowEventDemo("Finestra 2");

        window.setSize(350, 200);
        // non fare nulla alla chiusura della finestra
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setLocation(200, 200);

        other_window.setSize(350, 200);
        // non fare nulla alla chiusura della finestra
        other_window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        other_window.setVisible(true);
        other_window.setLocationRelativeTo(null);
        other_window.setLocation(600, 200);
    }
}
