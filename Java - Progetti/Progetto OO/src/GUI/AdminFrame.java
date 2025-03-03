package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class AdminFrame extends JFrame {
    private JPanel contentPane;
    private JButton logoutbutton;
    private JButton statistichebutton;
    private JButton dipbutton;
    private JButton visordbutt;
    private JButton prodbutton;
    private JLabel titlelabel;
    private JButton searchbutton;

    public void elementi(JPanel titlePanel) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage(AdminFrame.class.getResource("/Immagini/ImmIcon.png")));
        setLocationRelativeTo(null);

        // Pannello per i bottoni
        JPanel buttonpanel = new JPanel();
        contentPane.add(buttonpanel, BorderLayout.CENTER);
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS));

        buttonpanel.add(Box.createVerticalGlue());

        dipbutton = new JButton("Dipendenti");
        dipbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(dipbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        statistichebutton = new JButton("Statistiche");
        statistichebutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(statistichebutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        searchbutton = new JButton("Ricerca Clienti");
        searchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(searchbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        prodbutton = new JButton("Prodotti");
        prodbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(prodbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        visordbutt = new JButton("Ordini");
        visordbutt.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(visordbutt);

        buttonpanel.add(Box.createVerticalGlue());

        // Etichetta del titolo
        titlelabel = new JLabel("Admin Point", SwingConstants.CENTER);
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlePanel.add(titlelabel, BorderLayout.NORTH);

        // Bottone di logout che occupa l'intera larghezza del pannello
        logoutbutton = new JButton("Logout");
        logoutbutton.setBackground(Color.RED);
        logoutbutton.setForeground(Color.WHITE);
        titlePanel.add(logoutbutton, BorderLayout.SOUTH); 

        // Aggiungi il pannello dell'immagine
        contentPane.add(titlePanel, BorderLayout.WEST);
    }

    public void azioni(Controller c) {
        // Listener per il bottone di logout che richiama il metodo logout nel Controller
        logoutbutton.addActionListener(e -> c.logout(1));

        // Listener per il bottone dipendenti che richiama il metodo adminAndElem con parametro 1 per dipendenti
        dipbutton.addActionListener(e -> c.adminAndElem(1)); // Usa adminAndElem per amministratori

        // Listener per il bottone prodotti che richiama il metodo adminAndElem con parametro 2
        prodbutton.addActionListener(e -> c.adminAndElem(2)); // Usa adminAndElem per amministratori

        // Listener per il bottone statistiche che richiama il metodo adminAndElem con parametro 3
        statistichebutton.addActionListener(e -> c.adminAndElem(3)); // Usa adminAndElem per amministratori

        // Listener per il bottone visione ordini che richiama il metodo adminAndElem con parametro 4
        visordbutt.addActionListener(e -> c.adminAndElem(4)); // Usa adminAndElem per amministratori

        // Listener per il bottone ricerca che richiama il metodo searchAndElem con parametro 1
        searchbutton.addActionListener(e -> c.adminAndElem(6));
    }

    public AdminFrame(String title, Controller c) {
        super(title);
        JPanel titlePanel = c.createImagePanel("/Immagini/ImmAdmin.png");
        this.elementi(titlePanel);
        this.azioni(c);
    }
}



