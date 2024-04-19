package com.pellegrinoprincipe;

import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

public class JSpinnerDemo extends JFrame
{    
    private JSpinner[] spinners = new JSpinner[3]; // JSpinner
    private JLabel[] labels = new JLabel[3]; // JLabel

    public JSpinnerDemo()
    {
        super("JSpinner DEMO");
        
        setLayout(new GridLayout(3, 2, 5, 5)); // imposto il layout

        // creo uno spinner con un ListModel
        String[] some_obj = {"Cane", "Gatto", "Topo", "Pesce", "Anatra"};
        SpinnerListModel lm = new SpinnerListModel(some_obj);
        spinners[0] = new JSpinner(lm);
        labels[0] = new JLabel("Animali: ");

        // creo uno spinner con un NumberModel
        SpinnerNumberModel nm = new SpinnerNumberModel(50, 0, 100, 10);
        spinners[1] = new JSpinner(nm);
        labels[1] = new JLabel("Percentuali: ");

        // creo uno spinner con un DateModel
        GregorianCalendar calendar = new GregorianCalendar();
        Date _start = new Date();
        calendar.add(Calendar.MONTH, -3); // min 3 mesi dal mese attuale incluso
        Date _min = calendar.getTime();
        calendar.add(Calendar.MONTH, 10); // max 10 mesi dal mese attuale incluso
        Date _max = calendar.getTime();
        SpinnerDateModel dm = new SpinnerDateModel(_start, _min, _max, Calendar.MONTH);
        spinners[2] = new JSpinner(dm);
        labels[2] = new JLabel("Date: ");

        // aggiungo i controlli...
        add(labels[0]);
        add(spinners[0]);
        add(labels[1]);
        add(spinners[1]);
        add(labels[2]);
        add(spinners[2]);
    }

    public static void main(String args[])
    {
        JSpinnerDemo window = new JSpinnerDemo();

        // imposto le dimensioni della finestra
        window.setSize(300, 120);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
