package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;

public class JProgressBarDemo extends JFrame
{
    // JProgressBar
    private JProgressBar pb;
    private JProgressBar pb_i;    
    private ProgressMonitor pm; // ProgressMonitor    
    private JLabel[] labels = new JLabel[2];

    public void showProgress()
    {
        for (int i = 0; i <= 100; i++)
        {
            try
            {
                Thread.sleep(100);
                pb.setValue(i);
                
                if (pb.getValue() == 50) // se il progresso è alla metà creiamo un progress monitor
                {
                    pm = new ProgressMonitor(this, "...sto computando...", null, 0, 5);

                    for (int x = 0; x <= 5; x++)
                    {
                        Thread.sleep(1000);
                        pm.setProgress(x);
                    }

                    // crea un progress monitor per la lettura di dati da un file
                    File a_f = new File(getClass().getResource("SomeText.txt").getPath());
                    try(FileInputStream fileIn = new FileInputStream(a_f);
                        ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(this, "Reading SomeText.txt", fileIn);
                        Scanner in = new Scanner(pmis))
                        {
                            while (in.hasNextLine())
                            {
                                String line = in.nextLine();
                                Thread.sleep(10);
                            }
                        }
                        catch(IOException ioe)
                        {
                            JOptionPane.showMessageDialog(this, ioe.toString());            
                        }
                  }
            }
            catch (InterruptedException ie)
            {
                JOptionPane.showMessageDialog(this, ie.toString());
            }
        }
    }

    public JProgressBarDemo()
    {
        super("JProgressBar DEMO");

        setLayout(new FlowLayout(FlowLayout.LEADING));

        labels[0] = new JLabel("Progress Bar in Determinate Mode");

        // creo una progress bar in determined mode
        pb = new JProgressBar();

        // imposto i valori minimi e massimi
        pb.setMinimum(0);
        pb.setMaximum(100);

        // valore di partenza
        pb.setValue(0);

        // visualizza in percentuale nella progress bar l'indice di completamento
        pb.setStringPainted(true);

        labels[1] = new JLabel("Progress Bar in Indeterminate Mode");

        // progress bar in indeterminate mode
        pb_i = new JProgressBar();
        pb_i.setIndeterminate(true);

        // aggiungo i controlli...
        add(labels[0]);
        add(pb);
        add(labels[1]);
        add(pb_i);
    }

    public static void main(String args[])
    {
        JProgressBarDemo window = new JProgressBarDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 120);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);

        // avvio l'indicazione del progresso
        window.showProgress();
    }
}
