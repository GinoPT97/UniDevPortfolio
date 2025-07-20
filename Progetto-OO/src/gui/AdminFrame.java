package gui;

import controller.Controller;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminFrame extends JFrame {
    private static final String DIPENDENTI = "Dipendenti";
    private static final String STATISTICHE = "Statistiche";
    private static final String PRODOTTI = "Prodotti";
    private static final String ORDINI = "Ordini";
    private static final String LOGOUT = "Logout";
    private JButton logoutbutton;
    private JButton statistichebutton;
    private JButton dipbutton;
    private JButton visordbutt;
    private JButton prodbutton;
    private JButton searchbutton;

    public AdminFrame(String title, Controller c) {
        super(title);
        JPanel titlePanel = new ImagePanel(new ImageIcon(ImagePanel.class.getResource("/Immagini/ImmAdmin.png")).getImage());
        this.elementi(titlePanel);
        this.azioni(c);
    }

    private void elementi(JPanel titlePanel) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);

        // Pannello per i bottoni
        JPanel buttonpanel = new JPanel();
        contentPane.add(buttonpanel, BorderLayout.CENTER);
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS));

        buttonpanel.add(Box.createVerticalGlue());

        dipbutton = new JButton(DIPENDENTI, gui.IconUtils.getIconForText(DIPENDENTI, new Color(52, 152, 219)));
        dipbutton = new JButton(DIPENDENTI, gui.IconUtils.getIconForText(DIPENDENTI, new Color(52, 152, 219)));
        dipbutton.setBackground(new Color(52, 152, 219));
        dipbutton.setForeground(Color.WHITE);
        dipbutton.setFocusPainted(false);
        dipbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(dipbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        statistichebutton = new JButton(STATISTICHE, gui.IconUtils.getIconForText(STATISTICHE, new Color(39, 174, 96)));
        statistichebutton = new JButton(STATISTICHE, gui.IconUtils.getIconForText(STATISTICHE, new Color(39, 174, 96)));
        statistichebutton.setBackground(new Color(39, 174, 96));
        statistichebutton.setForeground(Color.WHITE);
        statistichebutton.setFocusPainted(false);
        statistichebutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(statistichebutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        searchbutton = new JButton("Ricerca Clienti", gui.IconUtils.getIconForText("Ricerca", new Color(241, 196, 15)));
        searchbutton = new JButton("Ricerca Clienti", gui.IconUtils.getIconForText("Ricerca", new Color(241, 196, 15)));
        searchbutton.setBackground(new Color(241, 196, 15));
        searchbutton.setForeground(Color.WHITE);
        searchbutton.setFocusPainted(false);
        searchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(searchbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        prodbutton = new JButton(PRODOTTI, gui.IconUtils.getIconForText(PRODOTTI, new Color(155, 89, 182)));
        prodbutton = new JButton(PRODOTTI, gui.IconUtils.getIconForText(PRODOTTI, new Color(155, 89, 182)));
        prodbutton.setBackground(new Color(155, 89, 182));
        prodbutton.setForeground(Color.WHITE);
        prodbutton.setFocusPainted(false);
        prodbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(prodbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        visordbutt = new JButton(ORDINI, gui.IconUtils.getIconForText(ORDINI, new Color(230, 126, 34)));
        visordbutt = new JButton(ORDINI, gui.IconUtils.getIconForText(ORDINI, new Color(230, 126, 34)));
        visordbutt.setBackground(new Color(230, 126, 34));
        visordbutt.setForeground(Color.WHITE);
        visordbutt.setFocusPainted(false);
        visordbutt.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(visordbutt);

        buttonpanel.add(Box.createVerticalGlue());

        // Etichetta del titolo
        JLabel titlelabel = new JLabel("Admin Point", SwingConstants.CENTER);
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlePanel.add(titlelabel, BorderLayout.NORTH);

        // Bottone di logout che occupa l'intera larghezza del pannello
        logoutbutton = new JButton(LOGOUT);
        logoutbutton = new JButton(LOGOUT, gui.IconUtils.getIconForText(LOGOUT, Color.RED));
        logoutbutton.setBackground(Color.RED);
        logoutbutton.setForeground(Color.WHITE);
        logoutbutton.setFocusPainted(false);
        titlePanel.add(logoutbutton, BorderLayout.SOUTH);

        // Aggiungi il pannello dell'immagine
        contentPane.add(titlePanel, BorderLayout.WEST);
    }

    private void azioni(Controller c) {
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
}



