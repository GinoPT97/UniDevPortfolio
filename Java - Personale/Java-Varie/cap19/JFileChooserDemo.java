package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class JFileChooserDemo extends JFrame
{
    private JFileChooser fc; // JFileChooser

    public JFileChooserDemo()
    {
        super("JFileChooser DEMO");
        
        fc = new JFileChooser(); // file chooser di tipo open        
        fc.setFileHidingEnabled(false); // fai vedere anche i file hidden        
        fc.showSaveDialog(this); // dialogo di tipo "Save"

        // creo un filtro che consente di aprire solo file immagini .jpg o .png
        fc.addChoosableFileFilter(new FileFilter()
        {
            private final String jpeg = "jpg";
            private final String png = "png";

            public boolean accept(File f)
            {
                if (f.isDirectory())                    
                    return true;
         
                String s = f.getName();
                String ext = s.split("\\.")[1];

                return ext.contains(jpeg) || ext.contains(png);
            }

            public String getDescription() { return "Solo file .jpg oppure .png"; }
        });
                
        int res = fc.showOpenDialog(this); // dialogo di tipo "Open"
        
        if (res == JFileChooser.APPROVE_OPTION) // controllo la scelta
        {            
            File f = fc.getSelectedFile(); /// ottengo il file scelto
            JOptionPane.showMessageDialog(this, "Il file scelto è: " + f.getName());
        }
        else if (res == JFileChooser.CANCEL_OPTION) {} // altro se ho cancellato la scelta        
        add(fc, BorderLayout.CENTER); // altro dialogo aggiunto direttamente al container
    }

    public static void main(String args[])
    {
        JFileChooserDemo window = new JFileChooserDemo();

        // imposto le dimensioni della finestra
        window.setSize(650, 350);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
