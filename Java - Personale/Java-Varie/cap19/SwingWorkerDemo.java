package com.pellegrinoprincipe;

import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;

class ReadBigFile extends SwingWorker<Integer, String>
{
    private int nr_row = 0;
    private JLabel out_label;
    private File the_file;
    private JTextArea area;
    private int file_rows;
    private JProgressBar pbar;
    private boolean stopped = false;

    public ReadBigFile(File file, JLabel label, JTextArea area_to_app, JProgressBar pb) throws IOException
    {
        out_label = label;
        the_file = file;
        area = area_to_app;
        pbar = pb;

        try(LineNumberReader reader = new LineNumberReader(new FileReader(the_file)))
        {                      
            while ((reader.readLine()) != null) {}
            file_rows = reader.getLineNumber();
        }
    }

    protected Integer doInBackground() throws FileNotFoundException, InterruptedException
    {
        try(Scanner in = new Scanner(the_file))
        {
            while (in.hasNextLine())
            {
                String line = in.nextLine() + "\n";                
                nr_row++;
                
                Thread.currentThread().sleep(5); // attendi un pò...per rendere il processing più fluido                
                publish(line); // invia il dato della linea letta

                // imposta il valore della proprietà progress che va da 0 a 100
                setProgress(100 * nr_row / file_rows);

                if (stopped)
                    return nr_row;
            }
        }
        return nr_row;
    }

    protected void done()
    {
        try
        {
            out_label.setText(out_label.getText() + " " + get());
        }
        catch (InterruptedException ex)
        {
            out_label.setText(out_label.getText() + " " + "Interruzione imprevista...");
        }
        catch (ExecutionException ex)
        {
            out_label.setText(out_label.getText() + " " + "Errore nel processing...");
        }
    }

    protected void process(List<String> lines)
    {
        for (int i = 0; i < lines.size(); i++)
            area.append(lines.get(i));         
        pbar.setString(nr_row + " / " + file_rows); // aggiorna il testo che appare nella progress bar
    }

    public void stopWorker()
    {
        stopped = true;
    }
}

public class SwingWorkerDemo extends JFrame
{
    private JLabel file;
    private JLabel file_2;
    private JLabel output;
    private JLabel out_other_digit;
    private JLabel state_bar;
    private JButton btn_open_file;
    private JButton btn_ok_other;
    private JButton btn_stop_worker;
    private JTextArea area;
    private JScrollPane scroll;
    private JFileChooser fc;
    private File a_file;
    private JTextField text;
    private JProgressBar pbar;
    private ReadBigFile rbf;

    public SwingWorkerDemo()
    {
        super("Swing Worker Demo");

        setLayout(null);

        fc = new JFileChooser();
        file = new JLabel("Scegli un file da processare:");
        add(file);
        file.setBounds(5, 10, 200, 20);
        btn_open_file = new JButton("Apri...");
        add(btn_open_file);
        btn_open_file.setBounds(200, 10, 100, 20);

        // evento per il pulsante che cerca il file da aprire
        btn_open_file.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                int res = fc.showOpenDialog(null);
                pbar.setValue(0);
                
                if (res == JFileChooser.APPROVE_OPTION) // controllo la scelta
                {
                    /// ottengo il file scelto
                    a_file = fc.getSelectedFile();

                    // worker thread che legge le righe di un file molto grande...
                    // e ascolta anche un evento di tipo PropertyChangeEvent
                    try
                    {
                        rbf = new ReadBigFile(a_file, output, area, pbar);                    
                    }
                    catch(IOException ioe){}
                    
                    rbf.addPropertyChangeListener(new PropertyChangeListener()
                    {
                        public void propertyChange(PropertyChangeEvent evt)
                        {
                            // se la proprietà che è cambiata è la progress
                            // aggiorna la progress bar
                            if (evt.getPropertyName().equals("progress"))
                                pbar.setValue((Integer) evt.getNewValue());
                        }
                    });
                    rbf.execute();
                }
            }
        });

        // widgets per l'interazione durante il processing del worker thread 
        file_2 = new JLabel("Digita qualunque cosa...");
        add(file_2);
        file_2.setBounds(5, 40, 200, 20);
        text = new JTextField();
        text.setBounds(200, 40, 150, 20);
        add(text);
        btn_ok_other = new JButton("Ok");
        add(btn_ok_other);
        btn_ok_other.setBounds(360, 40, 100, 20);
        btn_ok_other.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                out_other_digit.setText(text.getText());
            }
        });
        out_other_digit = new JLabel();
        add(out_other_digit);
        out_other_digit.setBounds(470, 40, 200, 20);
        //-----------

        // per la progress bar
        pbar = new JProgressBar();
        pbar.setStringPainted(true);
        pbar.setBounds(300, 280, 500, 20);
        add(pbar);
        state_bar = new JLabel("Stato di avanzamento lettura delle righe...");
        add(state_bar);
        state_bar.setBounds(5, 280, 350, 20);

        // text area per l'output delle linee
        area = new JTextArea(80, 20);
        area.setLineWrap(true);
        scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll);
        scroll.setBounds(5, 70, 800, 200);

        output = new JLabel("Totale righe processate: ");
        output.setBounds(5, 320, 250, 20);
        add(output);

        btn_stop_worker = new JButton("Stop processing...");
        btn_stop_worker.setBounds(650, 310, 150, 60);
        add(btn_stop_worker);
        btn_stop_worker.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (rbf != null)
                    rbf.stopWorker();
            }
        });
    }

    public static void main(String args[])
    {
        // creo la finestra
        SwingWorkerDemo window = new SwingWorkerDemo();

        window.setSize(840, 430);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
