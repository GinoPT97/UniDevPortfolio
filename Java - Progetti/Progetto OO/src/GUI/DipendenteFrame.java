package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DipendenteFrame extends JFrame {
    private JPanel contentPane;
    private JButton logoututton;
    private JButton clientebutton;
    private JButton ordineutton;
    private JLabel titlelab;
    private JButton searchbutton;
    private JPanel buttonpanel;
    private JPanel buttonContainer;

    public void elementi() {
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
        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(Color.ORANGE);
        contentPane.add(titlepanel, BorderLayout.WEST);
        titlepanel.setLayout(new BorderLayout(0, 0)); // Usa BorderLayout per estendere il bottone di logout

        // Centra verticalmente il titolo
        titlelab = new JLabel("Area Dipendenti");
        titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelab.setHorizontalAlignment(SwingConstants.CENTER); // Allinea il titolo orizzontalmente al centro
        titlepanel.add(titlelab, BorderLayout.CENTER);

        // Bottone di logout esteso orizzontalmente
        logoututton = new JButton("Logout");
        logoututton.setBackground(Color.RED);
        logoututton.setForeground(Color.WHITE); // Testo bianco per contrasto
        titlepanel.add(logoututton, BorderLayout.SOUTH); // Posizionato in basso ed esteso orizzontalmente

        // Pannello per i bottoni centrati
        buttonpanel = new JPanel();
        contentPane.add(buttonpanel, BorderLayout.CENTER);
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS)); // Layout verticale per centrare i bottoni

        // Pannello contenitore per i bottoni
        buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Layout per centrare i bottoni
        buttonpanel.add(Box.createVerticalGlue()); // Aggiunge spazio sopra i bottoni
        buttonpanel.add(buttonContainer); // Aggiunge il contenitore dei bottoni
        buttonpanel.add(Box.createVerticalGlue()); // Aggiunge spazio sotto i bottoni

        // Creazione e aggiunta dei bottoni al contenitore
        clientebutton = new JButton("Clienti");
        buttonContainer.add(clientebutton);

        searchbutton = new JButton("Ricerca Clienti");
        buttonContainer.add(searchbutton);

        ordineutton = new JButton("Ordine");
        buttonContainer.add(ordineutton);
    }

    public void azioni(Controller c) {
        // Listener per il pulsante di logout, chiama il metodo logout del Controller con parametro 2
        logoututton.addActionListener(e -> c.logout(2));

        // Listener per il pulsante cliente, chiama il metodo dipAndElem del Controller con parametro 1
        clientebutton.addActionListener(e -> c.dipAndElem(1));

        // Listener per il pulsante ordine, chiama il metodo dipAndElem del Controller con parametro 3
        ordineutton.addActionListener(e -> c.dipAndElem(3));

        // Listener per il pulsante di ricerca, chiama il metodo searchAndElem del Controller con parametro 1
        searchbutton.addActionListener(e -> c.searchAndElem(1));
    }

    public DipendenteFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }
}

