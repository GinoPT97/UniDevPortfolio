package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import java.net.URL;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class JPopupMenuDemo extends JFrame
{
    // popup menu
    private JPopupMenu text_edit_menu;
    private JMenu color_menu;
    private JMenu font_menu;    
    private JTextArea text_area = new JTextArea(10, 50); // textarea

    public JPopupMenuDemo()
    {
        super("Popup Menu DEMO");

        // datasource per il menu della text area
        // Testo item, icona, mnemonico, acceleratore
        Object items_data_for_textedit[][] =
        {
            {"Seleziona tutto", null, KeyEvent.VK_S,
              KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK)
            },
            {"Paragrafo", "paragrafo.png", KeyEvent.VK_P,
              KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.SHIFT_MASK)
            },
            {"Carattere", "carattere.png", KeyEvent.VK_C,
              KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.SHIFT_MASK)
            }
        };

        // datasource per il menu Colori
        // Testo item, icona, mnemonico, acceleratore
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
        // Testo item, icona, mnemonico, acceleratore
        Object items_data_for_font[][] =
        {
            {"Monospaced", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK)
            },
            {"Italic", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK)
            },
            {"Bold", null, null,
              KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK)
            }
        };
        
        text_edit_menu = new JPopupMenu(); // creo il menu popup

        // submenu
        color_menu = menuCreation("Colori", KeyEvent.VK_O);
        font_menu = menuCreation("Font", KeyEvent.VK_F);

        // creo gli items per i menu
        menuItemCreation(text_edit_menu, items_data_for_textedit, "normal");
        menuItemCreation(color_menu, items_data_for_colori, "radio");
        menuItemCreation(font_menu, items_data_for_font, "check");
        
        text_edit_menu.addSeparator(); // aggiungo un separatore

        // aggiungo i submenu come items
        text_edit_menu.add(color_menu, -1);
        text_edit_menu.add(font_menu, -1);

        // aggiungo alla text area il popup menu
        text_area.setComponentPopupMenu(text_edit_menu);

        add(text_area, BorderLayout.LINE_START);
    }

    private JMenu menuCreation(String text, Integer mnemonic)
    {
        JMenu menu = new JMenu(text);
        if (mnemonic != null)
            menu.setMnemonic(mnemonic);
  
        return menu;
    }

    // creo gli item da associare ad un menu
    private void menuItemCreation(JComponent menu, Object data[][], String type)
    {
        ButtonGroup bg = new ButtonGroup();
        JMenuItem item = null;

        for (int i = 0; i < data.length; i++)
        {            
            switch (type) // determina il tipo di item menu
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
        JPopupMenuDemo window = new JPopupMenuDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 200);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
