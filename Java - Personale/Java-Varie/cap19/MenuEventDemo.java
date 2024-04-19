package com.pellegrinoprincipe;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;

public class MenuEventDemo extends JFrame
{
    // componenti dei menu
    private JMenuBar menu_bar;
    private JMenu file_menu;
    private JTextField tf;
    // variabili per il drag
    public int start_menubar_offset_x;
    public int start_offset_mouse_x;

    public MenuEventDemo()
    {
        super("MenuEvent DEMO");

        setLayout(new FlowLayout());
        
        menu_bar = new JMenuBar(); // creo la menu bar

        // creo i menu
        file_menu = new JMenu("File");
        file_menu.setMnemonic(KeyEvent.VK_F);

        // creo il text field
        tf = new JTextField("Dopo aver aperto il menu premi un tasto...");
        tf.setEditable(false);
        
        file_menu.addMenuListener(new MenuListener() // listener per il menu
        {
            public void menuSelected(MenuEvent e)
            {
                file_menu.getItem(1).setEnabled(false);
            }

            public void menuDeselected(MenuEvent e)
            {
            }

            public void menuCanceled(MenuEvent e)
            {
            }
        });

        // aggiunta degli items al menu
        file_menu.add(new JMenuItem("Apri", KeyEvent.VK_A));
        file_menu.add(new JMenuItem("Modifica", KeyEvent.VK_M));
        file_menu.add(new JMenuItem("Esci", KeyEvent.VK_E));

        file_menu.addMenuKeyListener(new MenuKeyListener() // key listener
        {
            public void menuKeyTyped(MenuKeyEvent e) {}

            public void menuKeyPressed(MenuKeyEvent e)
            {
                tf.setText("Il tasto digitato e' il: " + e.getKeyText(e.getKeyCode()));
            }

            public void menuKeyReleased(MenuKeyEvent e) {}
        });

        // drag listener
        file_menu.addMenuDragMouseListener(new MenuDragMouseListener()
        {
            public void menuDragMouseEntered(MenuDragMouseEvent e) {}

            public void menuDragMouseExited(MenuDragMouseEvent e) {}

            public void menuDragMouseDragged(MenuDragMouseEvent e)
            {
                int x = e.getXOnScreen();
                int disp = x - start_offset_mouse_x;
                menu_bar.setLocation(start_menubar_offset_x + disp, menu_bar.getY());
            }

            public void menuDragMouseReleased(MenuDragMouseEvent e) {}
        });
        
        file_menu.addMouseListener(new MouseAdapter() // mouse listener
        {
            public void mousePressed(MouseEvent e)
            {
                start_offset_mouse_x = e.getXOnScreen();
            }
        });
        
        menu_bar.add(file_menu); // aggiungo alla menu bar i menu        
        setJMenuBar(menu_bar); // aggiungo al JFrame la barra dei menu
        start_menubar_offset_x = getJMenuBar().getX();
        add(tf);
    }

    public static void main(String args[])
    {
        // creo la finestra
        MenuEventDemo window = new MenuEventDemo();

        window.setSize(270, 120);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
