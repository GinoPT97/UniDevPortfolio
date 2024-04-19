package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DepthPositioningDemo extends JFrame
{
    private JPanel[] panels = new JPanel[5];

    public DepthPositioningDemo()
    {
        super("Depth positioning DEMO");

        setLayout(null);

        // creo i pannelli ciascuno con un colore differente e li posiziono
        (panels[0] = new JPanel()).setBackground(Color.RED);
        panels[0].setBounds(0, 0, 200, 100);
        panels[0].add(new JLabel("RED"));

        (panels[1] = new JPanel()).setBackground(Color.GREEN);
        panels[1].setBounds(20, 20, 200, 100);
        panels[1].add(new JLabel("GREEN"));

        (panels[2] = new JPanel()).setBackground(Color.BLUE);
        panels[2].setBounds(60, 60, 200, 100);
        panels[2].add(new JLabel("BLUE"));

        (panels[3] = new JPanel()).setBackground(Color.YELLOW);
        panels[3].setBounds(100, 100, 200, 100);
        panels[3].add(new JLabel("YELLOW"));

        (panels[4] = new JPanel()).setBackground(Color.MAGENTA);
        panels[4].setLayout(new BorderLayout());
        panels[4].setBounds(30, 90, 200, 100);
        panels[4].add(new JLabel("MAGENTA"), BorderLayout.LINE_START);

        // ottengo il layerd pane
        JLayeredPane layered_pane = getLayeredPane();

        // aggiungo i pannelli al container in ordine di profondità
        layered_pane.add(panels[0], new Integer(0));
        layered_pane.add(panels[1], new Integer(1));

        // stesso livello e pertanto decidiamo chi tra essi dovrà avere priorità
        layered_pane.add(panels[2], new Integer(2), 1);
        layered_pane.add(panels[4], new Integer(2), 0);

        layered_pane.add(panels[3], new Integer(3));

        layered_pane.remove(3);
    }

    public static void main(String args[])
    {
        DepthPositioningDemo window = new DepthPositioningDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 400);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
