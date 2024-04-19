package com.pellegrinoprincipe;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import java.net.URL;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class JMenuBarDemo extends JFrame
{
    // componenti dei menu
    private JMenuBar menu_bar;
    private JMenu file_menu;
    private JMenu edit_menu;
    private JMenu help_menu;
    private JMenu color_menu;
    private JMenu font_menu;

    public JMenuBarDemo()
    {
        super("Menu bar DEMO");

        // datasource per il menu File
        // Testo item, icona, mnemonico, acceleratore, separatore
        Object items_data_for_file[][] =
        {
            {"Nuovo file...", "new16.png", KeyEvent.VK_N,
              KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)
            },
            {"Apri file...", "open16.png", KeyEvent.VK_A,
              KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)
            },
            {"separator" /* item separatore */ },
            {"Esci", "exit16.png", null, null}
        };

        // datasource per il menu Edit
        // Testo item, icona, mnemonico, acceleratore, separatore
        Object items_data_for_edit[][] =
        {
            {"Copia", null, KeyEvent.VK_C,
              KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK)
            },
            {"Taglia", null, KeyEvent.VK_T,
              KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK)
            },
            {"Incolla", null, KeyEvent.VK_I,
              KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK)
            }
        };

        // datasource per il menu Help
        // Testo item, icona, mnemonico, acceleratore, separatore
        Object items_data_for_help[][] =
        {
            {"Help", null, KeyEvent.VK_H,
              KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.SHIFT_MASK)
            },
            {"separator" /* item separatore */ },
            {"About", null, KeyEvent.VK_A,
              KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK)
            }
        };

        // datasource per il menu Colori
        // Testo item, icona, mnemonico, acceleratore, separatore
        Object items_data_for_colori[][] =
        {
            {"Rosso", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK)
            },
            {"Verde", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK)
            },
            {"Blu", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK)
            }
        };

        // datasource per il menu Font
        // Testo item, icona, mnemonico, acceleratore, separatore
        Object items_data_for_font[][] =
        {
            {"Monospaced", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK)
            },
            {"Italic", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK)
            },
            {"Bold", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK)
            }
        };
        
        menu_bar = new JMenuBar(); // creo la menu bar

        // creo i menu
        file_menu = menuCreation("File", KeyEvent.VK_F);
        edit_menu = menuCreation("Modifica", KeyEvent.VK_M);
        help_menu = menuCreation("Help", KeyEvent.VK_H);

        // submenu
        color_menu = menuCreation("Colori", null);
        font_menu = menuCreation("Font", null);

        // creo gli items per i menu
        menuItemCreation(file_menu, items_data_for_file, "normal");
        menuItemCreation(edit_menu, items_data_for_edit, "normal");
        menuItemCreation(help_menu, items_data_for_help, "normal");
        menuItemCreation(color_menu, items_data_for_colori, "radio");
        menuItemCreation(font_menu, items_data_for_font, "check");

        // aggiungo i submenu come items
        edit_menu.addSeparator();
        edit_menu.add(color_menu, -1);

        font_menu.setMnemonic(KeyEvent.VK_F);
        edit_menu.add(font_menu, -1);

        // aggiungo alla menu bar i menu
        menu_bar.add(file_menu);
        menu_bar.add(edit_menu);
        menu_bar.add(help_menu);
        
        setJMenuBar(menu_bar); // aggiungo al JFrame la barra dei menu
    }

    private JMenu menuCreation(String text, Integer mnemonic)
    {
        JMenu menu = new JMenu(text);
        if (mnemonic != null)
            menu.setMnemonic(mnemonic);
   
        return menu;
    }

    // creo gli item da associare ad un menu
    private void menuItemCreation(JMenu menu, Object data[][], String type)
    {
        ButtonGroup bg = new ButtonGroup();
        JMenuItem item = null;

        for (int i = 0; i < data.length; i++)
        {
            // determina il tipo di item menu
            switch (type)
            {
                case "normal":
                    item = new JMenuItem();
                    break;
                case "radio":
                    item = new JRadioButtonMenuItem();
                    bg.add(item);
                    break;
                case "check":
                    item = new JCheckBoxMenuItem();
                    break;
                default:
                    break;
            }

            // verifica subito se devi inserire un separatore, un item o un submenu
            String data0 = (String) data[i][0];
            if (data0.equals("separator"))
                menu.addSeparator();
            else
            {
                // proprietà dell'item
                String text = data0;
                ImageIcon icon = data[i][1] != null ? getIcon((String) data[i][1]) : null;
                Integer mnemonic = data[i][2] != null ? (Integer) data[i][2] : KeyEvent.VK_UNDEFINED;
                KeyStroke accelerator = data[i][3] != null ? (KeyStroke) data[i][3] : null;

                // imposto le proprietà all'item
                item.setText(text);
                item.setIcon(icon);
                item.setMnemonic(mnemonic);
                item.setAccelerator(accelerator);

                menu.add(item);
            }
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
        JMenuBarDemo window = new JMenuBarDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 400);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
