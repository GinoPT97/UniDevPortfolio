package com.pellegrinoprincipe;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class SystemTrayDemo
{
    private SystemTray tray;
    private TrayIcon tray_icon;

    public SystemTrayDemo()
    {
        if (!SystemTray.isSupported())
        {
            JOptionPane.showMessageDialog(null, "System tray non supportato!");
            return;
        }
        else
           tray = SystemTray.getSystemTray();
        
        final PopupMenu popup = new PopupMenu(); // creo il menu

        // create menu item for the default action
        MenuItem open_doc = new MenuItem("Open a doc file...");
        MenuItem open_pdf = new MenuItem("Open a pdf file...");
        MenuItem open_gif = new MenuItem("Open a gif file...");
        MenuItem exit = new MenuItem("Exit");
        
        ActionListener al_for_items = new ActionListener() // evento per gli item
        {
            public void actionPerformed(ActionEvent e)
            {
                MenuItem item = (MenuItem)e.getSource();
                tray_icon.displayMessage(item.getLabel(), "nessun file selezionato!", TrayIcon.MessageType.INFO);
            }
        };

        open_doc.addActionListener(al_for_items);
        open_gif.addActionListener(al_for_items);
        open_pdf.addActionListener(al_for_items);

        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (tray != null)
                {                    
                    tray.remove(tray_icon); // rimuovi l'icona ed esci dall'applicazione
                    System.exit(0);
                }
            }
        });

        popup.add(open_doc);
        popup.add(open_pdf);
        popup.add(open_gif);
        popup.addSeparator();
        popup.add(exit);

        // crea l'icona per la system tray
        tray_icon = new TrayIcon(getIcon("new.png"), "Open documents...", popup);
        tray_icon.setImageAutoSize(true);
        
        tray_icon.addActionListener(new ActionListener() // doppio click sulla tray icon...
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "Apro l'applicazione...");
            }
        });

        try
        {            
            tray.add(tray_icon); // aggiungi l'icona
        }
        catch (AWTException ex) {}
    }

    private Image getIcon(String name)
    {
        // path delle icone
        URL _url = getClass().getResource(name);

        // immagine da visualizzare
        return new ImageIcon(_url).getImage();
    }

    public static void main(String args[])
    {
        new SystemTrayDemo();
    }
}
