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
import javax.swing.BorderFactory;

import Model.Dipendente;

public class ModificaDipendenteFrame extends JFrame {
    private JPanel contentPane;
    private String cod;
    private JPanel buttonpanel;
    private JPanel elempanel;
    private JButton backbutton;
    private JButton clearbutton;
    private JButton addbutton;
    private JTextField nometf;
    private JTextField cognometf;
    private JTextField codfisctf;
    private JTextField emailtf;
    private JTextField indirizzotf;
    private JTextField telefonotf;
    private JPanel titlepanel;
    private JLabel titlelabel;

    public void elementi() {
        // Impostazioni di base del frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaDipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Pannello principale
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Pannello del titolo
        titlepanel = new JPanel();
        titlepanel.setBackground(Color.ORANGE);
        titlelabel = new JLabel("Modifica Dipendente");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);
        contentPane.add(titlepanel, BorderLayout.NORTH);

        // Pannello per i bottoni
        buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        // Bottone Inserisci
        addbutton = creaButton("Inserisci", new Color(34, 139, 34));
        buttonpanel.add(addbutton);

        // Bottone Pulisci
        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        buttonpanel.add(clearbutton);

        // Bottone Indietro
        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(backbutton);

        // Pannello per gli elementi di input
        elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
        contentPane.add(elempanel, BorderLayout.CENTER);

        // Metodo per creare i pannelli di input
        elempanel.add(createInputPanel("Nome :", nometf = new JTextField(10)));
        elempanel.add(createInputPanel("Cognome :", cognometf = new JTextField(10)));
        elempanel.add(createInputPanel("Codice Fiscale :", codfisctf = new JTextField(10)));
        elempanel.add(createInputPanel("Email :", emailtf = new JTextField(10)));
        elempanel.add(createInputPanel("Indirizzo :", indirizzotf = new JTextField(10)));
        elempanel.add(createInputPanel("Telefono : +39", telefonotf = new JTextField(10)));
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

    public void viewdip(Dipendente de) {
        cod = de.getCodDIP();
        nometf.setText(de.getNome());
        cognometf.setText(de.getCognome());
        codfisctf.setText(de.getCodFis());
        indirizzotf.setText(de.getInd());
        emailtf.setText(de.getEmail());
        telefonotf.setText(de.getTel());
    }

    public void azioni(Controller c) {
        backbutton.addActionListener(e -> {
            clean();
            c.visAndElem(2, 3);
        });

        addbutton.addActionListener(e -> {
            try {
                // Crea un nuovo oggetto Dipendente utilizzando i valori dai JTextField
                Dipendente dipendente = new Dipendente(
                    cod,
                    nometf.getText(),
                    cognometf.getText(),
                    codfisctf.getText(),
                    emailtf.getText(),
                    indirizzotf.getText(),
                    telefonotf.getText()
                );
                // Aggiorna il dipendente nel database
                c.updip(dipendente);

                // Aggiorna il modello della tabella dei dipendenti
                for (int i = 0; i < c.dipModel.getRowCount(); i++) {
                    if (c.dipModel.getValueAt(i, 0).equals(dipendente.getCodDIP())) { // Assumendo che il codice dipendente sia il primo elemento
                        c.dipModel.setValueAt(dipendente.getNome(), i, 1);
                        c.dipModel.setValueAt(dipendente.getCognome(), i, 2);
                        c.dipModel.setValueAt(dipendente.getCodFis(), i, 3);
                        c.dipModel.setValueAt(dipendente.getEmail(), i, 4);
                        c.dipModel.setValueAt(dipendente.getInd(), i, 5);
                        c.dipModel.setValueAt(dipendente.getTel(), i, 6);
                        break; // Esci dal ciclo dopo aver trovato e aggiornato la riga
                    }
                }

                clean(); // Pulisce i campi di input
                c.visAndElem(2, 3); // Torna alla vista con indice 3
                JOptionPane.showMessageDialog(this, "Dipendente modificato", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "Errore!" + "\n" + "Tipo di errore: " + e1, "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearbutton.addActionListener(e -> clean());
    }

    public ModificaDipendenteFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }
}