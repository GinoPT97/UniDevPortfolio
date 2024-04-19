package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class JEditorPaneDemo extends JFrame
{    
    private JEditorPane epane; // JEditorPane    
    private URL html_page; // oggetto URL    
    private JScrollPane scroll_pane; // scroll pane

    public JEditorPaneDemo()
    {
        super("JEditorPane DEMO");
        
        epane = new JEditorPane(); // istanzio in editor pane        
        epane.setEditable(false); // lo rendo non editabile
       
        try // creo un oggetto URL
        {
            html_page = getClass().getResource("for_editor_pane.htm");            
            epane.setPage(html_page); // carico la pagina HTML nel editor pane
        }
        catch (IOException exc)
        {
            JOptionPane.showMessageDialog(this, exc.toString());
        }

        scroll_pane = new JScrollPane(epane);

        // aggiungo allo scroll pane l'editor pane
        scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_pane.setPreferredSize(new Dimension(800, 600));

        // aggiungo al container lo scroll pane
        add(scroll_pane, BorderLayout.CENTER);
    }

    public static void main(String args[])
    {
        JEditorPaneDemo window = new JEditorPaneDemo();

        // imposto le dimensioni della finestra
        window.setSize(800, 600);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
