package com.pellegrinoprincipe;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DesktopIntegrationDemo extends JFrame
{
    private JButton btn_browser;
    private JButton btn_file;
    private JTextField url;
    private JTextField file;
    private JLabel lbl_url;
    private JLabel lbl_file;
    private JLabel lbl_action;
    private JFileChooser fc;
    private JComboBox cb;
    private Desktop desk;

    public DesktopIntegrationDemo()
    {
        super("Desktop Integration DEMO");
        
        setLayout(null); // non imposto layout managers

        // labels
        lbl_url = new JLabel("URL");
        lbl_file = new JLabel("File");
        lbl_action = new JLabel("Azione");

        // text box
        url = new JTextField();
        file = new JTextField();

        // vari pulsanti...
        btn_browser = new JButton("...lancia il browser...");
        btn_file = new JButton("...scegli il tipo di file...");

        // file chooser
        fc = new JFileChooser();

        // combo box
        cb = new JComboBox(new String[] {"Open", "Edit", "Print"});

        // aggiungo i componenti al container
        add(lbl_url);
        add(btn_browser);
        add(lbl_file);
        add(btn_file);
        add(lbl_action);
        add(cb);
        add(url);
        add(file);

        file.setEditable(false);

        // imposto posizione e dimensione dei componenti
        lbl_url.setBounds(10, 10, 50, 20);
        url.setBounds(60, 10, 250, 20);
        btn_browser.setBounds(320, 10, 150, 20);
        
        // se il desktop non è supportato allora disattiva i pulsanti
        if (!Desktop.isDesktopSupported())
        {
            btn_browser.setEnabled(false);
            btn_file.setEnabled(false);
        }
        else // oggetto desktop
            desk = Desktop.getDesktop();
    
        
        btn_browser.addActionListener(new ActionListener() // evento
        {
            public void actionPerformed(ActionEvent e)
            {
                String address = url.getText().length() == 0 ? "http://www.google.it" : url.getText();
                try
                {
                    desk.browse(new URI(address));
                }
                catch (URISyntaxException ex) {}
                catch (IOException ex) {}
            }
        });

        btn_file.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                
                int res = fc.showOpenDialog(null); // dialogo di tipo "Open"               
                if (res == JFileChooser.APPROVE_OPTION) // controllo la scelta
                {                    
                    File f = fc.getSelectedFile(); // ottengo il file scelto
                    file.setText(f.getPath());

                    try
                    {
                        switch ((String) cb.getSelectedItem())
                        {
                            case "Open":
                                desk.open(f);
                                break;
                            case "Edit":
                                desk.edit(f);
                                break;
                            case "Print":
                                desk.print(f);
                                break;
                        }
                    }
                    catch (IOException ex)
                    {
                        JOptionPane.showMessageDialog(null, "Nessuna applicazione associata!");
                    }
                }
            }
        });

        lbl_action.setBounds(10, 50, 50, 20);
        cb.setBounds(60, 50, 150, 20);

        lbl_file.setBounds(10, 90, 50, 20);
        file.setBounds(60, 90, 250, 20);
        btn_file.setBounds(320, 90, 180, 20);
    }

    public static void main(String args[])
    {
        // creo la finestra
        DesktopIntegrationDemo window = new DesktopIntegrationDemo();

        window.setSize(600, 200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
