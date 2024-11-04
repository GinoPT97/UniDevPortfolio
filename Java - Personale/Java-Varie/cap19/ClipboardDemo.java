package com.pellegrinoprincipe;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class ClipboardDemo extends JFrame
{
    // data flavor per un oggetto JLabel
    private DataFlavor labelFlavor;
    private JLabel cutcopy_label;
    private JLabel paste_label;
    private JLabel image_label;
    private JTextArea area;
    private JTextArea area_paste;
    private JPanel panel_paste;
    private JPopupMenu clipmenu;
    private JMenuItem[] items;
    private ImageIcon an_image;
    private Clipboard cb;

    // ritorna un oggetto di tipo ImageIcon
    private ImageIcon getIcon(String name)
    {
        // path delle icone
        URL _url = getClass().getResource(name);

        // immagine da visualizzare
        return new ImageIcon(_url);
    }

    // implementazione di Transferable
    private class MyTransferable implements Transferable
    {
        // proprietà da trasferire
        private String text;
        private JLabel label;        
        private DataFlavor[] flavors = new DataFlavor[3]; // tipo di contenuto da trasferire

        public MyTransferable(String text, JLabel label)
        {
            // creo i flavors
            flavors[0] = text != null ? DataFlavor.stringFlavor : null;
            flavors[1] = label != null ? labelFlavor : null;

            this.text = text;
            this.label = label;
        }

        // ritorno i flavors
        public DataFlavor[] getTransferDataFlavors()
        {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {            
            for (DataFlavor df : flavors) // il data flavor è supportato?
            {
                if (df != null && df.equals(flavor))
                    return true;
            }
            return false;
        }

        // ottiene il dato da gestire
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
        {
            if (isDataFlavorSupported(flavor))
            {
                if (flavor.equals(flavors[0]))
                    return text;
                else if (flavor.equals(flavors[1]))
                    return label;
                else
                    return null;
            }
            else { throw new UnsupportedFlavorException(flavor); }
        }
    };

    public ClipboardDemo()
    {
        super("Clipboard Demo");
        setLayout(null);
        
        MouseAdapter ma = new MouseAdapter() // mouse adapter
        {
            public void mousePressed(MouseEvent e)
            {
                JComponent c = (JComponent) e.getSource();
                int btn = e.getButton();
               
                Point p = e.getPoint(); // coordinate x,y quando mouse premuto all'ìnterno del componente

                if (btn == MouseEvent.BUTTON3)
                {
                    if (c instanceof JTextArea)
                    {
                        JTextArea ta = (JTextArea) c;
                        String st = ta.getSelectedText();

                        // se c'è del testo selezionato e ho premuto il pulsante destro
                        // fai apparire il menu contestuale
                        if (st != null && ta.getName().equals("source"))
                            clipmenu.show(ta, p.x, p.y);
                        else if (ta.getName().equals("dest"))
                            clipmenu.show(ta, p.x, p.y);
                    }
                    else
                        clipmenu.show(c, p.x, p.y);
                }
            }
        };
        
        ActionListener al = new ActionListener() // action listener
        {
            public void actionPerformed(ActionEvent e)
            {
                String item = e.getActionCommand();
                Component c = clipmenu.getInvoker(); // su quale componente è stato invocato il menu

                JTextArea ta = null;
                JPanel pan = null;
                JLabel lab = null;

                if (c instanceof JTextArea)
                    ta = (JTextArea) c;
                else if (c instanceof JPanel)
                    pan = (JPanel) c;
                else if (c instanceof JLabel)
                    lab = (JLabel) c;

                switch (item)
                {
                    case "Cut":
                        if (ta != null)
                        {                            
                            int ss = ta.getSelectionStart(); // taglio il testo
                            int se = ta.getSelectionEnd();
                            String left_text = "", right_text = "";
                            String ta_text = ta.getText();
                            cb.setContents(new MyTransferable(ta.getSelectedText(), null), null);

                            try
                            {
                                left_text = ta.getText(0, ss);
                                right_text = ta.getText(se, ta_text.length() - se);
                                ta.setText(left_text + right_text);
                            }
                            catch (BadLocationException ex) {}
                        }
                        else if (lab != null)
                        {
                            cb.setContents(new MyTransferable(null, lab), null);

                            // aggiornamento del content pane
                            getContentPane().remove(lab);
                            getContentPane().repaint();
                        }
                        break;
                    case "Copy":
                        if (lab != null)
                        {
                            // creo una copia della label da trasferire importando
                            // solo le  proprietà di interesse
                            JLabel label_copy = new JLabel();
                            label_copy.setIcon(lab.getIcon());     
                            cb.setContents(new MyTransferable(null, label_copy), null);
                        }
                        else if (ta != null)
                        {
                            String st = ta.getSelectedText();
                            cb.setContents(new MyTransferable(st, null), null);
                        }
                        break;
                    case "Paste":
                        Transferable t = cb.getContents(null);

                        if (pan != null)
                        {
                            JPanel panel = (JPanel) c;
                            try
                            {
                                if (cb.isDataFlavorAvailable(labelFlavor))
                                {
                                    JLabel label = (JLabel) cb.getData(labelFlavor);
                                    panel.add(label);                                    
                                    panel.getRootPane().revalidate(); // aggiorna il panel
                                }
                            }
                            catch (UnsupportedFlavorException ex) {}
                            catch (IOException ex) {}
                        }
                        else if (ta != null)
                        {
                            if (t.isDataFlavorSupported(DataFlavor.stringFlavor))
                            {
                                try
                                {
                                    ta.setText((String) t.getTransferData(DataFlavor.stringFlavor));
                                }
                                catch (UnsupportedFlavorException ex) {}
                                catch (IOException ex) {}
                            }
                        }
                        break;
                }
            }
        };
        
        cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        try
        {
            labelFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=javax.swing.JLabel");
        }
        catch (ClassNotFoundException ex) {}

        // creo il popup menu
        clipmenu = new JPopupMenu();
        items = new JMenuItem[3];
        items[0] = new JMenuItem("Cut");
        items[1] = new JMenuItem("Copy");
        items[2] = new JMenuItem("Paste");
        clipmenu.add(items[0]);
        clipmenu.add(items[1]);
        clipmenu.add(items[2]);
        items[0].addActionListener(al);
        items[1].addActionListener(al);
        items[2].addActionListener(al);
        
        cutcopy_label = new JLabel("Cut/Copy sui seguenti componenti:"); // labels

        cutcopy_label.setBounds(10, 20, 200, 20);
        an_image = getIcon("open.png");
        image_label = new JLabel(an_image);

        image_label.setBounds(10, 150, 48, 48);
        paste_label = new JLabel("Paste sui seguenti componenti:");

        paste_label.setBounds(290, 20, 200, 20);
        image_label.addMouseListener(ma);

        // panel
        panel_paste = new JPanel(new FlowLayout());
        panel_paste.setBounds(290, 150, 100, 100);
        panel_paste.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        panel_paste.addMouseListener(ma);

        // area
        area = new JTextArea("Seleziona un po' di testo... \ned esegui qualche operazione\ndi cut o copy...");

        area.setName("source");

        area.setBounds(10, 50, 200, 90);
        area_paste = new JTextArea();
        area.addMouseListener(ma);

        area_paste.setName("dest");
        area_paste.setBounds(290, 50, 200, 90);
        area_paste.addMouseListener(ma);

        // add dei componenti
        add(cutcopy_label);
        add(image_label);
        add(area);
        add(area_paste);
        add(paste_label);
        add(panel_paste);
    }

    public static void main(String args[])
    {
        ClipboardDemo window = new ClipboardDemo();
        window.setSize(550, 300);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
