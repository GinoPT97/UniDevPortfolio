package com.pellegrinoprincipe;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.tree.DefaultMutableTreeNode;

public class BuiltinDragAndDrop extends JFrame
{
    private JTable table;
    private JColorChooser color_chooser;
    private JFileChooser file_chooser;
    private JList list;
    private JTree tree;
    private JTextArea area;
    private JTextField text;
    private JComboBox source;
    private JComboBox target;
    private JLabel label_source;
    private JLabel label_target;
    private String combo_name;

    private void removePrevWidgets(String comp_name)
    {
        Component c = getContentPane().getComponentAt(comp_name == "LEFT" ? 2 : 620, 90);

        if (!(c instanceof JPanel))
        {
            remove(c);            
            if (c instanceof JTable) // se è una tabella rimuovo anche l'header
                remove(getContentPane().getComponentAt(comp_name == "LEFT" ? 2 : 620, 80));
        }
    }
   
    private void doUpdate()  // aggiorniamo il contenuto della finestra
    {
        getContentPane().validate();
        repaint();
    }

    public BuiltinDragAndDrop()
    {
        super("Builtin drag and drop");

        setLayout(null);

        // source data
        String data[] = 
        {
            "Seleziona...", "JTable", "JColorChooser", "JFileChooser", "JList", "JTree", "JTextArea", "JTextField"
        };

        // creo i combo box ed assegno un nome alla freccia in giù che visualizza il menu
        source = new JComboBox(data);
        source.getComponent(0).setName("LEFT");

        target = new JComboBox(data);
        target.getComponent(0).setName("RIGHT");
        
        MouseListener ml = new MouseAdapter() // determina quale combo box è stato utilizzato
        {
            public void mousePressed(MouseEvent e)
            {
                JComponent jc = (JComponent) e.getSource();
                combo_name = jc.getName();
            }
        };
        
        ItemListener item_listener = new ItemListener() // eventi per i combo box
        {
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    String item = (String) e.getItem();

                    switch (item)
                    {
                        case "JTable":
                            createJTable(combo_name);
                            break;
                        case "JColorChooser":
                            createJColorChooser(combo_name);
                            break;
                        case "JFileChooser":
                            createJFileChooser(combo_name);
                            break;
                        case "JList":
                            createJList(combo_name);
                            break;
                        case "JTree":
                            createJTree(combo_name);
                            break;
                        case "JTextArea":
                            createJTextArea(combo_name);
                            break;
                        case "JTextField":
                            createJTextField(combo_name);
                            break;
                        default:
                    }
                }
            }
        };

        source.addItemListener(item_listener);
        // gestisce l'interazione con la freccia del combo box
        source.getComponent(0).addMouseListener(ml);

        target.addItemListener(item_listener);
        target.getComponent(0).addMouseListener(ml);

        // le label
        label_source = new JLabel("Sorgente per il drag");
        label_target = new JLabel("Destinazione per il drop");

        // posiziono i componenti
        label_source.setBounds(1, 1, 120, 20);
        source.setBounds(130, 1, 120, 20);
        label_target.setBounds(620, 1, 140, 20);
        target.setBounds(760, 1, 120, 20);

        add(label_source);
        add(source);
        add(label_target);
        add(target);
    }

    public void createJTable(String comp_name)
    {
        removePrevWidgets(comp_name);

        String columns_headers[] = {"Nome", "Cognome", "Eta'"};

        Object[][] data =
        {
            {"Pellegrino", "Principe", 38},
            {"Mario", "Rossi", 58},
            {"Biagio", "Bianchi", 27}
        };
        
        table = new JTable(data, columns_headers); // tabella con il table model di default        
        table.setDragEnabled(true); // attivo il drag

        JTableHeader th = table.getTableHeader();
        th.setBounds(comp_name.equals("LEFT") ? 1 : 620, 70, 225, 20);
        table.setBounds(comp_name.equals("LEFT") ? 1 : 620, 90, 225, 50);

        add(th);
        add(table);
        repaint();
    }

    public void createJColorChooser(String comp_name)
    {
        removePrevWidgets(comp_name);

        color_chooser = new JColorChooser();
        color_chooser.setSize(color_chooser.getPreferredSize());
        color_chooser.setLocation(comp_name.equals("LEFT") ? 1 : 620, 70);
        add(color_chooser);
        color_chooser.setDragEnabled(true);

        doUpdate();
    }

    public void createJFileChooser(String comp_name)
    {
        removePrevWidgets(comp_name);

        file_chooser = new JFileChooser();
        file_chooser.setSize(file_chooser.getPreferredSize());
        file_chooser.setLocation(comp_name.equals("LEFT") ? 1 : 620, 70);
        add(file_chooser);
        file_chooser.setDragEnabled(true);

        doUpdate();
    }

    public void createJList(String comp_name)
    {
        removePrevWidgets(comp_name);

        // array di voci di tipo stringhe
        String color_items[] = {"Red", "Blue", "Yellow", "Green", "Black", "White"};
        
        // JList single selection
        list = new JList(color_items);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBounds(comp_name.equals("LEFT") ? 1 : 620, 70, 225, 70);
        list.setDragEnabled(true);
        add(list);

        doUpdate();
    }

    public void createJTree(String comp_name)
    {
        removePrevWidgets(comp_name);

        // nodo ROOT
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Opt");

        // nodo GRAPHIC e figli
        DefaultMutableTreeNode graphic = new DefaultMutableTreeNode("Graphic");

        DefaultMutableTreeNode gimp = new DefaultMutableTreeNode("Gimp");
        DefaultMutableTreeNode fspot = new DefaultMutableTreeNode("FSpot");
        graphic.add(gimp);
        graphic.add(fspot);

        // nodo LANGUAGE e figli
        DefaultMutableTreeNode language = new DefaultMutableTreeNode("Language");
        DefaultMutableTreeNode cpp = new DefaultMutableTreeNode("Gnu-cpp");
        DefaultMutableTreeNode java = new DefaultMutableTreeNode("Java");
        language.add(cpp);
        language.add(java);

        // nodo IDE e figli
        DefaultMutableTreeNode ide = new DefaultMutableTreeNode("Ide");
        DefaultMutableTreeNode netb = new DefaultMutableTreeNode("Netbeans");
        DefaultMutableTreeNode ecl = new DefaultMutableTreeNode("Eclipse");
        ide.add(netb);
        ide.add(ecl);

        // aggiungo i nodi al ROOT
        root.add(graphic);
        root.add(language);
        root.add(ide);

        // creo il JTree
        tree = new JTree(root);
        tree.setBounds(comp_name.equals("LEFT") ? 1 : 620, 70, 225, 180);
        tree.setDragEnabled(true);
        add(tree);

        doUpdate();
    }

    public void createJTextArea(String comp_name)
    {
        removePrevWidgets(comp_name);

        String text = "[thp@xdevel]$ ls -la | grep java";
        
        area = new JTextArea(text); // controllo text area
        area.setLineWrap(true);
        area.setBounds(comp_name.equals("LEFT") ? 1 : 620, 70, 225, 180);
        area.setDragEnabled(true);
        add(area);

        doUpdate();
    }

    public void createJTextField(String comp_name)
    {
        removePrevWidgets(comp_name);

        JTextField text = new JTextField("Sono un campo di testo");
        text.setBounds(comp_name.equals("LEFT") ? 1 : 620, 70, 225, 25);
        text.setDragEnabled(true);
        add(text);

        doUpdate();
    }

    public static void main(String args[])
    {
        BuiltinDragAndDrop window = new BuiltinDragAndDrop();
        window.setSize(1240, 620);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
