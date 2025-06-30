package gui;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class ModificaClienteFrame extends JFrame {
    private JPanel contentPane;
    private String cod;
    private JPanel buttonpanel;
    private JButton backbutton;
    private JButton clearbutton;
    private JButton addbutton;
    private JPanel elempanel;
    private JTextField nometf;
    private JTextField cognometf;
    private JTextField codfisctf;
    private JTextField emailtf;
    private JTextField indirizzotf;
    private JTextField telefonotf;
    private JPanel titlepanel;
    private JLabel titlelabel;

    public ModificaClienteFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        // Impostazioni di base del frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaClienteFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Pannello principale
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Pannello del titolo
        titlepanel = new JPanel();
        titlepanel.setBackground(new Color(107, 142, 35));
        titlelabel = new JLabel("Modifica Cliente");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE); // Colore del testo bianco per contrasto
        titlepanel.add(titlelabel);
        contentPane.add(titlepanel, BorderLayout.NORTH);

        // Pannello per i bottoni
        buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Layout centrato con spaziatura
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
        elempanel.setBorder(new EmptyBorder(20, 50, 20, 50)); // Padding migliorato
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS)); // Layout verticale per gli input
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

    public void viewct(String codCl, String nome, String cognome, String codFis, String indirizzo, String email, String telefono) {
        cod = codCl;
        nometf.setText(nome);
        cognometf.setText(cognome);
        codfisctf.setText(codFis);
        indirizzotf.setText(indirizzo);
        emailtf.setText(email);
        telefonotf.setText(telefono);
    }

    private void azioni(Controller c) {
        backbutton.addActionListener(e -> {
            clean();
            c.visAndElem(3, 3); // Torna alla vista con indice 3
        });

        addbutton.addActionListener(e -> {
            try {
                // Aggiorna il cliente nel database utilizzando il metodo refactorizzato
                c.upcliente(
                        cod, // codCliente
                        nometf.getText(), // nome
                        cognometf.getText(), // cognome
                        codfisctf.getText(), // codFis
                        emailtf.getText(), // email
                        indirizzotf.getText(), // indirizzo
                        telefonotf.getText() // telefono
                );

                // Aggiorna la riga corrispondente nel modello
                for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
                    if (c.clienteModel.getValueAt(i, 0).equals(cod)) { // Usa direttamente cod
                        c.clienteModel.setValueAt(nometf.getText(), i, 1);
                        c.clienteModel.setValueAt(cognometf.getText(), i, 2);
                        c.clienteModel.setValueAt(codfisctf.getText(), i, 3);
                        c.clienteModel.setValueAt(emailtf.getText(), i, 4);
                        c.clienteModel.setValueAt(indirizzotf.getText(), i, 5);
                        c.clienteModel.setValueAt(telefonotf.getText(), i, 6);
                        break; // Esci dal ciclo dopo aver trovato e aggiornato la riga
                    }
                }

                clean(); // Pulisce i campi dopo l'aggiornamento
                c.visAndElem(3, 3); // Torna alla vista con indice 3
                JOptionPane.showMessageDialog(null, "Cliente modificato");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage());
            }
        });

        clearbutton.addActionListener(e -> clean());
    }
}

