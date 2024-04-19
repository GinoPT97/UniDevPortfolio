package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class JSliderDemo extends JFrame
{    
    private JSlider perc_slider; // JSlider
    private JLabel lbl = new JLabel("Valori percentuali");

    public JSliderDemo()
    {
        super("JSlider DEMO");
        
        setLayout(new FlowLayout(FlowLayout.CENTER)); // imposto il layout

        int min = 0, max = 100, start_to = 50;

        // istanza di un JSlider
        perc_slider = new JSlider(JSlider.HORIZONTAL, min, max, start_to);

        // imposto i valori per i ticks
        perc_slider.setMajorTickSpacing(10);
        perc_slider.setMinorTickSpacing(5);
        perc_slider.setPaintTicks(true);
        perc_slider.setPaintLabels(true);
        
        perc_slider.setSnapToTicks(true); // forza la levetta a spostarsi solo sui marcatori

        // aggiungo i controlli al container
        add(lbl);
        add(perc_slider);
    }

    public static void main(String args[])
    {
        JSliderDemo window = new JSliderDemo();

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
