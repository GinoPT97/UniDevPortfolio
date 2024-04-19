package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeDemo extends JFrame
{    
    private JTree tree; // JTree

    public JTreeDemo()
    {
        super("JTree DEMO");

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
        DefaultMutableTreeNode netb = new DefaultMutableTreeNode("NetBeans");
        DefaultMutableTreeNode ecl = new DefaultMutableTreeNode("Eclipse");
        ide.add(netb);
        ide.add(ecl);

        // aggiungo i nodi al ROOT
        root.add(graphic);
        root.add(language);
        root.add(ide);
        
        tree = new JTree(root); // creo il JTree
        add(tree, BorderLayout.CENTER);
    }

    public static void main(String args[])
    {
        JTreeDemo window = new JTreeDemo();

        // imposto le dimensioni della finestra
        window.setSize(250, 250);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
