package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.MaskFormatter;

public class JTextFieldFormatterDemo extends JFrame
{
    // array di text fields
    private JFormattedTextField[] text_fields = new JFormattedTextField[4];
    private JLabel labels[] = new JLabel[4];

    public JTextFieldFormatterDemo()
    {
        super("JFormattedTextField DEMO");
        
        setLayout(new FlowLayout(FlowLayout.CENTER)); // imposto il layout

        // campo di testo con un formattatore che accetta solo valori percentuali
        labels[0] = new JLabel("Digita un valore percentuale:");
        text_fields[0] = new JFormattedTextField(NumberFormat.getPercentInstance());
        text_fields[0].setColumns(20);

        // campo di testo con un formattatore che accetta solo valori di tipo data
        labels[1] = new JLabel("Digita un valore di tipo time:");
        text_fields[1] = new JFormattedTextField(DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US));
        text_fields[1].setColumns(20);

        // campo di testo con un formattatore che accetta solo il pattern specificato
        labels[2] = new JLabel("Digita un valore come da pattern (Es. 11--11---ee):");
        try
        {
            MaskFormatter mf = new MaskFormatter("##--##---??");
            text_fields[2] = new JFormattedTextField(mf);
            text_fields[2].setColumns(20);
        }
        catch (ParseException pe) {}

        // campo di testo che accetta un formattatore arbitrario
        labels[3] = new JLabel("Digita un valore come da pattern (Es. a#11#3):");
        text_fields[3] = new JFormattedTextField(new DefaultFormatter()
        {
            // l'utente deve digitare dei caratteri separati dal simbolo
            // # ed ogni campo può avere 1 o 2 caratteri e ci devono essere
            // massimo 3 campi
            // Es. a#11#3
            public String valueToString(Object value) throws ParseException
            {
                StringBuilder builder = new StringBuilder();

                String _v = (String) value;

                if (_v != null)
                {
                    // se non c'è neppure un cancelletto allora genera subito un'eccezione
                    if (_v.indexOf("#") == -1)
                        throw new ParseException("Maschera non corretta...", 0);
          
                    String _values[] = _v.split("#");

                    // se ci sono più di tre cancelletti genera un'eccezione
                    if (_values.length > 3)
                        throw new ParseException("Massimo 3 campi...", 0);
          
                    for (int i = 0; i < _values.length; i++)
                    {
                        String a_value = _values[i];

                        // se un campo non contiene il nr. di caratteri previsti
                        // genera un'eccezione
                        if (a_value.length() < 1 || a_value.length() > 2)
                            throw new ParseException("1 o 2 caratteri ammessi...", 0);
          
                        builder.append(a_value + (i == _values.length - 1 ? "" : "#"));
                    }
                }

                return builder.toString();
            }

            // ritorna semplicemente il testo inserito
            public Object stringToValue(String text) throws ParseException
            {
                return text;
            }
        });

        text_fields[3].setColumns(20);

        // creo i text fields e li aggiungo al container
        for (int i = 0; i < text_fields.length; i++)
        {
            add(labels[i]);
            add(text_fields[i]);
        }
    }

    public static void main(String args[])
    {
        JTextFieldFormatterDemo window = new JTextFieldFormatterDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 260);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
