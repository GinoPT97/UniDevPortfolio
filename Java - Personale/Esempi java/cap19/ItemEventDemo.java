package com.pellegrinoprincipe;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class ItemEventDemo extends JFrame
{
    private JMenuBar menu_bar;
    private JMenu operate_menu;
    private JCheckBoxMenuItem menu_item;

    public ItemEventDemo()
    {
        super("ItemEvent Demo");
        
        menu_bar = new JMenuBar(); // componenti

        operate_menu = new JMenu("Operazioni");
        menu_item = new JCheckBoxMenuItem("Cambia il colore di sfondo...");

        operate_menu.add(menu_item);
        menu_bar.add(operate_menu);

        setJMenuBar(menu_bar);

        menu_item.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {               
                if (e.getStateChange() == ItemEvent.SELECTED)
                    getContentPane().setBackground(Color.WHITE);
                else
                    getContentPane().setBackground(new Color(238, 238, 238));
            }
        });
    }

    public static void main(String args[])
    {
        // creo la finestra
        ItemEventDemo window = new ItemEventDemo();

        window.setSize(310, 150);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
