package com.pellegrinoprincipe;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

public class JToolTipDemo extends JFrame
{    
    JButton btn; // button    
    JTextField txt; // text field

    public JToolTipDemo()
    {
        super("JToolTip DEMO");

        setLayout(new FlowLayout(FlowLayout.LEADING));
        
        btn = new JButton("OK...") // creo il pulsante con il tooltip personalizzato
        {            
            public JToolTip createToolTip() // override da JComponent di tooltip
            {
                JToolTip tip = new JToolTip()
                {
                    public String getTipText()
                    {
                        return "premi per salvare...";
                    }
                };

                tip.setBackground(Color.WHITE);
                tip.setForeground(Color.BLACK);
                return tip;
            }
        };
        btn.setToolTipText("");
        
        txt = new JTextField(20); // creo il text box
        txt.setToolTipText("digita i dati nel formato corretto...");

        // impostiamo alcune proprietà che varranno per i tooltip
        ToolTipManager.sharedInstance().setInitialDelay(20);
        ToolTipManager.sharedInstance().setDismissDelay(5000);

        // aggiungo i controlli...
        add(btn);
        add(txt);
    }

    public static void main(String args[])
    {
        JToolTipDemo window = new JToolTipDemo();

        // imposto le dimensioni della finestra
        window.setSize(280, 110);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
