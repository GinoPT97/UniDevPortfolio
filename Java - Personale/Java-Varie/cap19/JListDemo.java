package com.pellegrinoprincipe;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class JListDemo extends JFrame
{
    // array di JList
    private JList[] lists = new JList[3];
    // array di labels
    JLabel labels[] = new JLabel[3];

    public JListDemo()
    {
        super("JList DEMO");

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
        labels[0] = new JLabel("Lista a selezione singola: ");
        labels[1] = new JLabel("Lista a selezione d'intervallo singolo: ");
        labels[2] = new JLabel("Lista a selezione d'intervallo multiplo:  ");
        
        setLayout(new FlowLayout(FlowLayout.CENTER)); // imposto il layout

        // JList single selection
        lists[0] = new JList(color_items);
        lists[0].setPreferredSize(new Dimension(100, 130));
        lists[0].setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // JList single int. selection
        lists[1] = new JList(images_items);
        lists[1].setPreferredSize(new Dimension(300, 130));
        lists[1].setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        lists[1].setVisibleRowCount(-1);

        lists[1].setLayoutOrientation(JList.HORIZONTAL_WRAP);

        // JList multiple int. selection
        lists[2] = new JList(names_items);
        lists[2].setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // aggiungo i componenti al container
        for (int i = 0; i < lists.length; i++)
        {
            add(labels[i]);
            add(lists[i]);
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
        JListDemo window = new JListDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 480);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
