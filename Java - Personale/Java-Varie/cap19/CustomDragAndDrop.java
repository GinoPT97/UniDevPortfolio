package com.pellegrinoprincipe;

import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.text.BadLocationException;

public class CustomDragAndDrop extends JFrame
{
    private JList dest_list;
    private DefaultListModel<String> dlm;
    private JTextField source_text;
    private JTextField dest_text;
    private JLabel source_label;
    private JLabel source_img_label;
    private JLabel dest_label;
    private JButton dest_btn;
    private DataFlavor colorFlavor; // data flavor per un oggetto Color

    // ritorna un oggetto di tipo ImageIcon
    private ImageIcon getIcon(String name)
    {
        // path delle icone
        URL _url = getClass().getResource(name);

        // immagine da visualizzare
        return new ImageIcon(_url);
    }
    
    private class MyTransferable implements Transferable // implementazione di Transferable
    {
        // proprietà da trasferire
        private String text;
        private Color color;
        private ImageIcon image;
        // tipo di contenuto da trasferire
        private DataFlavor[] flavors = new DataFlavor[3];

        public MyTransferable(String text, Color color, ImageIcon image)
        {
            // creo i flavors
            flavors[0] = text != null ? DataFlavor.stringFlavor : null;
            try
            {
                colorFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.awt.Color");
                flavors[1] = color != null ? colorFlavor : null;
            }
            catch (ClassNotFoundException ex) {}

            flavors[2] = image != null ? DataFlavor.imageFlavor : null;

            this.text = text;
            this.color = color;
            this.image = image;

        }
        
        public DataFlavor[] getTransferDataFlavors() // ritorno i flavors
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
                    return color;
                else if (flavor.equals(flavors[2]))
                    return image;
                else
                    return null;
            }
            else
                throw new UnsupportedFlavorException(flavor);
        }
    };
    
    private class MyTransferHandler extends TransferHandler // implementazione di TransferHandler
    {
        // METODI PER  L'ESPORTAZIONE
        // azione del trasferimento
        public int getSourceActions(JComponent c)
        {            
            return TransferHandler.COPY_OR_MOVE; // posso copiare o muovere un dato
        }
        
        protected Transferable createTransferable(JComponent c) // crea un oggetto Tranferable
        {
            String text = null;
            ImageIcon image = null;
            Color color = null;

            if (c instanceof JLabel)
            {
                text = ((JLabel) c).getText();
                image = (ImageIcon) ((JLabel) c).getIcon();
            }
            else if (c instanceof JTextField)
            {
                text = ((JTextField) c).getSelectedText();
                color = c.getForeground();
            }
            return new MyTransferable(text, color, image);
        }

        // cosa faccio al termine dell'esportazione?
        protected void exportDone(JComponent source, Transferable data, int action)
        {
            if (action == TransferHandler.MOVE)
            {
                try
                {                    
                    if (source instanceof JTextField) // estraggo la parte di testo selezionata...
                    {
                        JTextField tf = (JTextField) source;
                        String d = (String) data.getTransferData(DataFlavor.stringFlavor);

                        String old_txt = tf.getText(0, tf.getText().length() - d.length());
                        ((JTextField) source).setText(old_txt);
                    }
                }
                catch (BadLocationException e) {}
                catch (UnsupportedFlavorException e) {}
                catch (IOException e) {}
            }
        }

        // METODI PER L'IMPORTAZIONE
        // posso importare il dato?
        public boolean canImport(TransferSupport support)
        {            
            Component c = support.getComponent(); // determina il componente destinatario del drop

            // verifica che l'import possa avvenire solo sui seguenti tipi di componenti
            return c instanceof JList || c instanceof JLabel || c instanceof JTextField;
        }

        public boolean importData(TransferSupport support)
        {   
            // oggetto Transferable associato a questo handler
            Transferable transferable = support.getTransferable(); // oggetto Transferable associato a questo handler

            // flavors supportati
            DataFlavor flavors[] = transferable.getTransferDataFlavors();
            List<DataFlavor> flavor_as_list = Arrays.asList(flavors);
            
            Component component = support.getComponent(); // target component

            try
            {          
                if (component instanceof JLabel) // sei una label?
                {
                    JLabel label = (JLabel) component;

                    if (flavor_as_list.contains(DataFlavor.stringFlavor))
                        label.setText((String) transferable.getTransferData(DataFlavor.stringFlavor));
                    else if (flavor_as_list.contains(DataFlavor.imageFlavor) && support.getUserDropAction() == TransferHandler.COPY)
                        label.setIcon((ImageIcon) transferable.getTransferData(DataFlavor.imageFlavor));
           
                    return true;
                }
                else if (component instanceof JTextField)
                {
                    JTextField text = (JTextField) component;

                    // solo l'altro text field sorgente contiene tutti e due i flavors...
                    if (flavor_as_list.contains(DataFlavor.stringFlavor) && flavor_as_list.contains(colorFlavor))
                    {
                        text.setText((String) transferable.getTransferData(DataFlavor.stringFlavor));
                        text.setForeground((Color) transferable.getTransferData(colorFlavor));
                    }

                    return true;
                }
                else if (component instanceof JList)
                {
                    JList list = (JList) component;

                    if (flavor_as_list.contains(DataFlavor.stringFlavor))
                    {
                        JList.DropLocation location = (JList.DropLocation) support.getDropLocation();

                        int index = location.getIndex();
                        String text = (String) transferable.getTransferData(DataFlavor.stringFlavor);

                        if (location.isInsert())
                            ((DefaultListModel) list.getModel()).add(index, text);
                        else
                            ((DefaultListModel) list.getModel()).set(index, text);
                    }
                    return true;
                }
            }
            catch (UnsupportedFlavorException ex) {}
            catch (IOException ex) {}
            return false;
        }
    }

    public CustomDragAndDrop()
    {
        super("Custom drag and drop");
        setLayout(null);
        
        MyTransferHandler mth = new MyTransferHandler(); // handler per il trasferimento
        
        MouseAdapter ma = new MouseAdapter() // mouse adapter
        {
            public void mousePressed(MouseEvent e)
            {
                JComponent c = (JComponent) e.getSource();
                TransferHandler th = c.getTransferHandler();
                th.exportAsDrag(c, e, TransferHandler.COPY);
            }
        };

        // source text field
        source_text = new JTextField("Magenta");
        source_text.setForeground(Color.MAGENTA);
        source_text.setBounds(10, 50, 200, 20);
        add(source_text);
        source_text.setTransferHandler(mth);
        source_text.setDragEnabled(true);

        // source label
        source_label = new JLabel("Questo testo può essere trasferito...");
        source_label.setBounds(10, 80, 250, 20);
        add(source_label);
        source_label.setTransferHandler(mth);
        source_label.addMouseListener(ma);

        // source image label
        ImageIcon ic = getIcon("exit.png");
        source_img_label = new JLabel(ic);
        source_img_label.setBounds(10, 120, 48, 48);
        add(source_img_label);
        source_img_label.setTransferHandler(mth);
        source_img_label.addMouseListener(ma);

        // label informative
        JLabel src_out = new JLabel("Componenti sorgenti del drag:");
        src_out.setBounds(10, 10, 200, 20);
        add(src_out);

        JLabel target_out = new JLabel("Componenti destinatari del drop:");
        target_out.setBounds(300, 10, 200, 20);
        add(target_out);

        // modello per la lista
        DefaultListModel<String> dlm = new DefaultListModel<>();
        dlm.addElement("Rosso");
        dlm.addElement("Verde");
        dlm.addElement("Giallo");
        dlm.addElement("Blu");

        dest_list = new JList(dlm);
        dest_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dest_list.setBounds(300, 50, 200, 120);
        add(dest_list);
        dest_list.setTransferHandler(mth);
        dest_list.setDropMode(DropMode.INSERT);

        // dest text
        dest_text = new JTextField();
        dest_text.setBounds(300, 200, 200, 20);
        add(dest_text);
        dest_text.setTransferHandler(mth);

        dest_label = new JLabel("Questo testo può subire un drop!!!!");
        dest_label.setBounds(300, 230, 300, 48);
        add(dest_label);
        dest_label.setTransferHandler(mth);

        // button che non può subire drop
        dest_btn = new JButton("Premi per accedere...");
        dest_btn.setBounds(300, 280, 200, 20);
        dest_btn.setTransferHandler(mth);
        add(dest_btn);
    }

    public static void main(String args[])
    {
        CustomDragAndDrop window = new CustomDragAndDrop();
        window.setSize(750, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
    }
}
