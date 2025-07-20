package gui;

import controller.Controller;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DipendenteFrame extends JFrame {
    private static final String LOGOUT = "Logout";
    private static final String CLIENTI = "Clienti";
    private static final String ORDINE = "Ordine";
    private static final String PRODOTTI = "Prodotti";
    private JButton logoututton;
    private JButton clientebutton;
    private JButton ordineutton;
    private JButton searchbutton;
    private JButton prodButton;

    public DipendenteFrame(String title, Controller c) {
        super(title);
        JPanel titlePanel = new ImagePanel(new ImageIcon(ImagePanel.class.getResource("/Immagini/ImmDipendenti.jpg")).getImage());
        this.elementi(titlePanel);
        this.azioni(c);
    }

    private void elementi(JPanel titlePanel) {
        // Impostazioni di base del frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 450);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);

        // Pannello del titolo con sfondo arancione
        JLabel titlelab = new JLabel("Dipendenti Point", SwingConstants.CENTER); // Declared locally
        titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelab.setForeground(Color.WHITE);
        titlePanel.add(titlelab, BorderLayout.CENTER);

        // Bottone di logout esteso orizzontalmente
        logoututton = new JButton(LOGOUT);
        logoututton = new JButton(LOGOUT, gui.IconUtils.getIconForText(LOGOUT, Color.RED));
        logoututton.setBackground(Color.RED);
        logoututton.setForeground(Color.WHITE);
        logoututton.setFocusPainted(false);
        titlePanel.add(logoututton, BorderLayout.SOUTH);

        // Pannello per i bottoni centrati
        JPanel buttonpanel = new JPanel(); // Declared locally
        contentPane.add(buttonpanel, BorderLayout.CENTER);
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS));

        // Creazione e aggiunta dei bottoni al pannello
        buttonpanel.add(Box.createVerticalGlue());

        clientebutton = new JButton(CLIENTI);
        clientebutton = new JButton(CLIENTI, gui.IconUtils.getIconForText(CLIENTI, new Color(52, 152, 219)));
        clientebutton.setBackground(new Color(52, 152, 219));
        clientebutton.setForeground(Color.WHITE);
        clientebutton.setFocusPainted(false);
        clientebutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(clientebutton);

        buttonpanel.add(Box.createRigidArea(new Dimension(0, 20)));

        searchbutton = new JButton("Ricerca Clienti");
        searchbutton = new JButton("Ricerca Clienti", gui.IconUtils.getIconForText("Ricerca", new Color(241, 196, 15)));
        searchbutton.setBackground(new Color(241, 196, 15));
        searchbutton.setForeground(Color.WHITE);
        searchbutton.setFocusPainted(false);
        searchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(searchbutton);

        buttonpanel.add(Box.createRigidArea(new Dimension(0, 20)));

        ordineutton = new JButton(ORDINE);
        ordineutton = new JButton(ORDINE, gui.IconUtils.getIconForText(ORDINE, new Color(230, 126, 34)));
        ordineutton.setBackground(new Color(230, 126, 34));
        ordineutton.setForeground(Color.WHITE);
        ordineutton.setFocusPainted(false);
        ordineutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(ordineutton);

        buttonpanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bottone per la gestione dei prodotti
        prodButton = new JButton(PRODOTTI);
        prodButton = new JButton(PRODOTTI, gui.IconUtils.getIconForText(PRODOTTI, new Color(155, 89, 182)));
        prodButton.setBackground(new Color(155, 89, 182));
        prodButton.setForeground(Color.WHITE);
        prodButton.setFocusPainted(false);
        prodButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(prodButton);

        buttonpanel.add(Box.createVerticalGlue());
        // Aggiungi il pannello dell'immagine
        contentPane.add(titlePanel, BorderLayout.WEST);
    }

    private void azioni(Controller c) {
        // Listener per il pulsante di logout, chiama il metodo logout del Controller con parametro 2
        logoututton.addActionListener(e -> c.logout(2));

        // Listener per il pulsante cliente, chiama il metodo dipAndElem del Controller con parametro 1 per i dipendenti
        clientebutton.addActionListener(e -> c.dipAndElem(1));

        // Listener per il pulsante ordine, chiama il metodo dipAndElem del Controller con parametro 3
        ordineutton.addActionListener(e -> c.dipAndElem(3));

        // Listener per il pulsante di ricerca, chiama il metodo dipAndElem del Controller con parametro 5
        searchbutton.addActionListener(e -> c.dipAndElem(5));

        // Listener per il pulsante prodotti, chiama il metodo dipAndElem del Controller con parametro 2
        prodButton.addActionListener(e -> c.dipAndElem(2));
    }
}

