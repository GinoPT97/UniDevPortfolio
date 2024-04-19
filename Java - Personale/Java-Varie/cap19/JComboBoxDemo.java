package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JComboBoxDemo extends JFrame
{
    // array di combo boxes
    private JComboBox[] combo_boxes = new JComboBox[3];
    // array di labels
    JLabel labels[] = new JLabel[3];

    public JComboBoxDemo()
    {
        super("JComboBox DEMO");

        // array di voci di tipo stringhe
        String color_items[] = {"Red", "Blue", "Yellow", "Green", "Black", "White"};

        // array di voci di tipo immagine
        ImageIcon images_items[] =
        {
            getIcon("exit.png"),
            getIcon("contact.png"),
            getIcon("open.png"),
            getIcon("trash.png")
        };

        // array di voci di tipo stringhe
        String names_items[] = {"Carlo", "Bruno", "Claudio", "Maria", "Francesca", "Pietro"};

        // creo le labels
        labels[0] = new JLabel("Scegli un colore: ");
        labels[1] = new JLabel("Scegli un'immagine: ");
        labels[2] = new JLabel("Scegli un nome: ");
        
        setLayout(new FlowLayout(FlowLayout.CENTER)); // imposto il layout

        // creazione di un combo box  editabile
        combo_boxes[0] = new JComboBox(color_items);
        combo_boxes[0].setEditable(true);

        combo_boxes[0].setSelectedItem("Maria");

        // creazione di un combo box non editabile
        combo_boxes[1] = new JComboBox(images_items);

        // creazione di un combo box con un limitato nr di elementi visualizzabili
        // e con il secondo elemento visualizzato di default
        combo_boxes[2] = new JComboBox(names_items);
        combo_boxes[2].setMaximumRowCount(4);
        combo_boxes[2].setSelectedIndex(1);

        // aggiungo i componenti al container
        for (int i = 0; i < combo_boxes.length; i++)
        {
            add(labels[i]);
            add(combo_boxes[i]);
        }
    }

    // ritorna un oggetto di tipo ImageIcon
    private ImageIcon getIcon(String name)
    {
        // path delle icone
        URL _url = getClass().getResource(name);

        // immagine da visualizzare
        return new ImageIcon(_url);
    }

    public static void main(String args[])
    {
        JComboBoxDemo window = new JComboBoxDemo();

        // imposto le dimensioni della finestra
        window.setSize(300, 250);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
