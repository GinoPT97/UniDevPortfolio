package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardLayoutDemo extends JFrame
{
    private JPanel main_panel;
    private JPanel photo_1_panel;
    private JPanel photo_2_panel;
    private JPanel photo_3_panel;
    private ImageIcon green_field_img;
    private ImageIcon stockholm_img;
    private ImageIcon autumn_img;
    private CardLayout card_layout;
    private int c_n = -1;

    public CardLayoutDemo()
    {
        super("CardLayout DEMO");

        // url delle immagini
        URL green_field = getClass().getResource("GreenField.jpg");
        URL stockholm = getClass().getResource("Stockholm.jpg");
        URL autumn = getClass().getResource("Autumn.jpg");

        // immagini
        green_field_img = new ImageIcon(green_field);
        stockholm_img = new ImageIcon(stockholm);
        autumn_img = new ImageIcon(autumn);

        // oggetto CardLayout assegnato al JPanel principale
        card_layout = new CardLayout();
        main_panel = new JPanel();
        main_panel.setLayout(card_layout);

        // creo gli altri JPanel e gli aggiungo una JLabel con
        // lo sfondo di un'immagine
        photo_1_panel = new JPanel();
        photo_1_panel.add(new JLabel(green_field_img));
        photo_2_panel = new JPanel();
        photo_2_panel.add(new JLabel(stockholm_img));
        photo_3_panel = new JPanel();
        photo_3_panel.add(new JLabel(autumn_img));

        // aggiungo al main panel gli altri panels
        main_panel.add(photo_1_panel, "foto 0");
        main_panel.add(photo_2_panel, "foto 1");
        main_panel.add(photo_3_panel, "foto 2");

        add(main_panel, BorderLayout.PAGE_START);
    }

    public void changeCard()
    {
        c_n++;
        if (c_n > 3)
            c_n = 0;
        card_layout.show(main_panel, "foto " + c_n);
    }

    public static void main(String args[])
    {
        final CardLayoutDemo window = new CardLayoutDemo();

        // imposto le dimensioni della finestra
        window.setSize(800, 600);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);

        // temporizzatore
        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            public void run()
            {
                window.changeCard();
            }
        }, 0, 3000);
    }
}
