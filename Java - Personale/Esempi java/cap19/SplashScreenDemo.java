package com.pellegrinoprincipe;

import java.awt.*;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SplashScreenDemo extends JFrame
{
    void _doRender(Graphics2D g, int perc)
    {
        // disegna sull'immagine di overlay dello splash screen
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(380, 220, 200, 40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", Font.BOLD, 14));
        g.drawString("Loading " + perc + "%", 380, 250);
    }

    public SplashScreenDemo()
    {
        super("ndUI - A Natural Docs Frontend");
        final SplashScreen splash = SplashScreen.getSplashScreen();
        URL r = splash.getImageURL();        
        
        if (splash == null)
        {
            System.out.println("problemi nella creazione dello splash screen...forse il path dell'immagine è errato?");
            return;
        }
        Graphics2D g = splash.createGraphics();
        if (g == null)
        {
            System.out.println("problemi per la creazione del contesto grafico...");
            return;
        }
        for (int i = 0; i < 100; i++)
        {
            _doRender(g, i);
            splash.update();
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {}
        }
        
        splash.close();

        // finestra dell'applicazione
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        toFront();
    }

    public static void main(String args[])
    {
        new SplashScreenDemo();
    }
}
