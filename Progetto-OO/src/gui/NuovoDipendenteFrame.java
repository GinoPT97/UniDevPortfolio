package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Dipendente;

import javax.swing.BorderFactory;

public class NuovoDipendenteFrame extends JFrame {
    private JPanel contentPane;
    private JTextField nometf;
    private JTextField cognometf;
    private JTextField codfisctf;
    private JTextField emailtf;
    private JTextField indirizzotf;
    private JTextField telefonotf;
    private JButton addbutton;
    private JButton clearbutton;
    private JButton backbutton;

    public NuovoDipendenteFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    public void elementi() {
        // Imposta le proprietà di base della finestra
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Imposta il pannello principale e il layout BorderLayout
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Pannello dei pulsanti
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        addbutton = creaButton("Aggiungi", new Color(34, 139, 34));
        buttonpanel.add(addbutton);

        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        buttonpanel.add(clearbutton);

        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(backbutton);

        // Pannello per gli elementi
        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(20, 100, 20, 100));
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
        contentPane.add(elempanel, BorderLayout.CENTER);

        // Metodo per creare i pannelli di input
        elempanel.add(createInputPanel("Nome :", nometf = new JTextField(10)));
        elempanel.add(createInputPanel("Cognome :", cognometf = new JTextField(10)));
        elempanel.add(createInputPanel("Codice Fiscale :", codfisctf = new JTextField(10)));
        elempanel.add(createInputPanel("Email :", emailtf = new JTextField(10)));
        elempanel.add(createInputPanel("Indirizzo :", indirizzotf = new JTextField(10)));
        elempanel.add(createInputPanel("Telefono : +39", telefonotf = new JTextField(10)));

        // Pannello del titolo
        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(Color.ORANGE);
        contentPane.add(titlepanel, BorderLayout.NORTH);
        JLabel titlelabel = new JLabel("Inserimento Nuovo Dipendente");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private JButton creaButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    public void clean() {
        nometf.setText("");
        cognometf.setText("");
        codfisctf.setText("");
        indirizzotf.setText("");
        emailtf.setText("");
        telefonotf.setText("");
    }

    public void azioni(Controller c) {
        // Listener per il bottone di aggiunta di un nuovo dipendente
        addbutton.addActionListener(e -> {
            try {
                // Crea un nuovo oggetto Dipendente utilizzando i valori dei campi di testo
                Dipendente newDipendente = new Dipendente(
                        "",
                        nometf.getText(),
                        cognometf.getText(),
                        codfisctf.getText(),
                        emailtf.getText(),
                        indirizzotf.getText(),
                        telefonotf.getText()
                );
                c.newdip(newDipendente); // Aggiungi il dipendente al database

                // Aggiunta del dipendente al modello
                c.dipModel.addRow(new Object[]{
                        newDipendente.getNome(),
                        newDipendente.getCognome(),
                        newDipendente.getCodFis(),
                        newDipendente.getEmail(),
                        newDipendente.getInd(),
                        newDipendente.getTel()
                });

                // Pulisce i campi di input
                clean();
                // Mostra un messaggio di successo
                JOptionPane.showMessageDialog(null, "Dipendente aggiunto");
            } catch (SQLException e1) {
                // Mostra un messaggio di errore in caso di eccezione
                JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1.getMessage());
            }
        });

        // Listener per il bottone di pulizia dei campi di input
        clearbutton.addActionListener(e -> clean());

        // Listener per il bottone di ritorno alla schermata precedente
        backbutton.addActionListener(e -> {
            clean(); // Pulisce i campi di input
            c.visAndElem(2, 3); // Passa alla schermata dipendenti
        });
    }
}
