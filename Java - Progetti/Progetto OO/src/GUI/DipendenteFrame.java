package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

public class DipendenteFrame extends JFrame {
    private JPanel contentPane;
    private JButton logoututton;
    private JButton clientebutton;
    private JButton ordineutton;
    private JLabel titlelab;
    private JButton searchbutton;
    private JPanel buttonpanel;
    private JButton prodButton;

    public void elementi(JPanel titlePanel) {
        // Impostazioni di base del frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Pannello del titolo con sfondo arancione
        titlelab = new JLabel("Dipendenti Point", SwingConstants.CENTER);
        titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelab.setForeground(Color.WHITE);
        titlePanel.add(titlelab, BorderLayout.NORTH);

        // Bottone di logout esteso orizzontalmente
        logoututton = new JButton("Logout");
        logoututton.setBackground(Color.RED);
        logoututton.setForeground(Color.WHITE);
        titlePanel.add(logoututton, BorderLayout.SOUTH);

        // Pannello per i bottoni centrati
        buttonpanel = new JPanel();
        contentPane.add(buttonpanel, BorderLayout.CENTER);
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS));

        // Creazione e aggiunta dei bottoni al pannello
        buttonpanel.add(Box.createVerticalGlue()); 

        clientebutton = new JButton("Clienti");
        clientebutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(clientebutton);

        buttonpanel.add(Box.createRigidArea(new Dimension(0, 20)));

        searchbutton = new JButton("Ricerca Clienti");
        searchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(searchbutton);

        buttonpanel.add(Box.createRigidArea(new Dimension(0, 20)));

        ordineutton = new JButton("Ordine");
        ordineutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(ordineutton);

        buttonpanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bottone per la gestione dei prodotti
        prodButton = new JButton("Prodotti");
        prodButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(prodButton);

        buttonpanel.add(Box.createVerticalGlue());
        // Aggiungi il pannello dell'immagine
        contentPane.add(titlePanel, BorderLayout.WEST);
    }

    public void azioni(Controller c) {
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

    public DipendenteFrame(String title, Controller c) {
        super(title);
        JPanel titlePanel = c.createImagePanel("/Immagini/ImmDipendenti.png");
        this.elementi(titlePanel);
        this.azioni(c);
    }
}

