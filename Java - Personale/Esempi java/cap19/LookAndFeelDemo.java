package com.pellegrinoprincipe;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultMutableTreeNode;
import java.net.URL;
import javax.swing.SwingUtilities;

public class LookAndFeelDemo extends JFrame
{
    private JLabel label;
    private JButton button;
    private JCheckBox check_box;
    private JRadioButton radio_button;
    private JTextField text_field;
    private JTextArea text_area;
    private JComboBox combo_box;
    private JList list;
    private JSlider slider;
    private JSpinner spinner;
    private JTree tree;
    private JTable table;
    private JProgressBar progress_bar;
    private JMenuBar menu_bar;

    public LookAndFeelDemo()
    {
        super("Look And Feel Demo");
        
        setLayout(null); // layout manager

        final UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();

        // componenti per cambiare il LAF
        JLabel lable_laf = new JLabel("Scegli il L&F da cambiare:");
        final JComboBox cb_laf = new JComboBox();

        for (int i = 0, n = plaf.length; i < n; i++)
            cb_laf.addItem(plaf[i].getName());
            
        cb_laf.addItemListener(new ItemListener() // cambia il L&F
        {
            public void itemStateChanged(ItemEvent e)
            {
                int ix = cb_laf.getSelectedIndex();
                try
                {
                    UIManager.setLookAndFeel(plaf[ix].getClassName());
                    SwingUtilities.updateComponentTreeUI(LookAndFeelDemo.this);
                }
                catch (Exception ex) {}
            }
        });

        add(lable_laf);
        add(cb_laf);
        lable_laf.setBounds(10, 10, 150, 25);
        cb_laf.setBounds(10, 35, 150, 25);
        createComponent(); // crea tutti i componenti
    }

    public void createComponent()
    {
        JLabel label;
        JButton button;
        JCheckBox check_box;
        JRadioButton radio_button;
        JTextField text_field;
        JTextArea text_area;
        JComboBox combo_box;
        JList list;
        JSlider slider;
        JSpinner spinner;
        JTree tree;
        JTable table;
        JProgressBar progress_bar;
        JMenuBar menu_bar;

        label = new JLabel("sono una label!!!!");
        add(label);
        label.setBounds(300, 10, 150, 35);

        button = new JButton("sono un plusante!!!!");
        add(button);
        button.setBounds(300, 45, 150, 35);

        check_box = new JCheckBox("check box");
        add(check_box);
        check_box.setBounds(300, 80, 150, 35);

        radio_button = new JRadioButton("radio button");
        add(radio_button);
        radio_button.setBounds(300, 115, 150, 35);

        text_field = new JTextField("sono un text field");
        add(text_field);
        text_field.setBounds(300, 155, 150, 25);

        text_area = new JTextArea("sono\nuna\ntext\narea");
        JScrollPane scroll = new JScrollPane(text_area,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scroll);
        scroll.setBounds(300, 190, 150, 55);

        String color_items[] =
        {
            "sono", "una", "lista"
        };
        list = new JList(color_items);
        add(list);
        list.setBounds(300, 260, 150, 55);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
        slider.setMajorTickSpacing(5);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        add(slider);
        slider.setBounds(300, 330, 150, 55);

        SpinnerNumberModel nm = new SpinnerNumberModel(50, 0, 100, 10);
        spinner = new JSpinner(nm);
        add(spinner);
        spinner.setBounds(300, 400, 50, 25);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Opt");
        DefaultMutableTreeNode graphic = new DefaultMutableTreeNode("Graphic");
        DefaultMutableTreeNode gimp = new DefaultMutableTreeNode("Gimp");
        DefaultMutableTreeNode fspot = new DefaultMutableTreeNode("FSpot");
        graphic.add(gimp);
        graphic.add(fspot);
        DefaultMutableTreeNode language = new DefaultMutableTreeNode("Language");
        DefaultMutableTreeNode cpp = new DefaultMutableTreeNode("Gnu-cpp");
        DefaultMutableTreeNode java = new DefaultMutableTreeNode("Java");
        language.add(cpp);
        language.add(java);
        DefaultMutableTreeNode ide = new DefaultMutableTreeNode("Ide");
        DefaultMutableTreeNode netb = new DefaultMutableTreeNode("Netbeans");
        DefaultMutableTreeNode ecl = new DefaultMutableTreeNode("Eclipse");
        ide.add(netb);
        ide.add(ecl);
        root.add(graphic);
        root.add(language);
        root.add(ide);
        tree = new JTree(root);
        JScrollPane scroll_tree = new JScrollPane(tree,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scroll_tree);
        scroll_tree.setBounds(300, 430, 150, 80);

        JButton bcc = new JButton("Scegli un colore...");
        bcc.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JColorChooser.showDialog(null, null, Color.yellow);
            }
        });
        add(bcc);
        bcc.setBounds(300, 520, 150, 35);

        JButton bcf = new JButton("Scegli un file...");
        bcf.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                new JFileChooser().showOpenDialog(null);
            }
        });
        add(bcf);
        bcf.setBounds(300, 560, 150, 35);

        String columns_headers[] = {"Nome", "Cognome", "Eta'", "Data di nascita", "Citta'"};

        Object[][] data =
        {
            {"Pellegrino", "Principe", 38, "23/06/1972", "Napoli"},
            {"Mario", "Rossi", 58, "11/07/1966", "Roma"},
            {"Biagio", "Bianchi", 27, "16/01/1980", "Milano"}
        };

        // tabella con il table model di default
        table = new JTable(data, columns_headers);
        JTableHeader th = table.getTableHeader();
        add(th);
        add(table);
        th.setBounds(500, 80, 450, 25);
        table.setBounds(500, 110, 350, 50);

        progress_bar = new JProgressBar();
        progress_bar.setMinimum(0);
        progress_bar.setValue(0);
        progress_bar.setIndeterminate(true);
        add(progress_bar);
        progress_bar.setBounds(500, 200, 350, 35);

        Object items_data_for_file[][] =
        {
            {
                "Nuovo file...",
                "new16.png",
                KeyEvent.VK_N,
                KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)
            },
            {
                "Apri file...",
                "open16.png",
                KeyEvent.VK_A,
                KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)
            },
            {
                "separator" // item separatore
            },
            {
                "Esci",
                "exit16.png",
                null,
                null
            }
        };

        menu_bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu_bar.add(menu);
        menuItemCreation(menu, items_data_for_file, "normal");
        setJMenuBar(menu_bar);
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
            {
                menu.addSeparator();
            }
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
        // creo la finestra
        LookAndFeelDemo window = new LookAndFeelDemo();

        window.setSize(1000, 700);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
