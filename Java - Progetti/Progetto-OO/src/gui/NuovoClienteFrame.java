package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Cliente;

public class NuovoClienteFrame extends JFrame {
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

    public void elementi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Impostazione del contenuto e del layout principale
        contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        // Pannello per i pulsanti
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        addbutton = creaButton("Aggiungi", new Color(34, 139, 34));
        buttonpanel.add(addbutton);

        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        buttonpanel.add(clearbutton);

        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(backbutton);

        // Pannello principale per i campi di inserimento
        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        elempanel.setLayout(new GridLayout(7, 2, 10, 10)); // 7 righe e 2 colonne per i campi di input
        contentPane.add(elempanel, BorderLayout.CENTER);

        // Creazione e aggiunta dei componenti al pannello dei campi di inserimento
        elempanel.add(createInputPanel("Nome:", nometf = new JTextField(20)));
        elempanel.add(createInputPanel("Cognome:", cognometf = new JTextField(20)));
        elempanel.add(createInputPanel("Codice Fiscale:", codfisctf = new JTextField(20)));
        elempanel.add(createInputPanel("Email:", emailtf = new JTextField(20)));
        elempanel.add(createInputPanel("Indirizzo:", indirizzotf = new JTextField(20)));
        elempanel.add(createInputPanel("Telefono: +39", telefonotf = new JTextField(20)));

        JLabel tesseralab = new JLabel("La relativa tessera verrà creata in automatico");
        elempanel.add(tesseralab);

        // Pannello del titolo
        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(85, 107, 47));
        contentPane.add(titlepanel, BorderLayout.NORTH);

        JLabel titlelabel = new JLabel("Inserimento Nuovo Utente");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
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
        button.setFont(new Font("Tahoma", Font.BOLD, 14));
        return button;
    }

    public void clean() {
        // Pulizia dei campi di testo
        nometf.setText("");
        cognometf.setText("");
        codfisctf.setText("");
        indirizzotf.setText("");
        emailtf.setText("");
        telefonotf.setText("");
    }

    public void azioni(Controller c) {
        // Azione per il bottone "Indietro"
        backbutton.addActionListener(e -> {
            clean();
            c.visAndElem(3, 3);
        });

        // Azione per il bottone "Aggiungi"
        addbutton.addActionListener(e -> {
            try {
                // Creazione del nuovo cliente con i dati dai JTextField
                Cliente newCliente = new Cliente(
                        "",
                        nometf.getText(),
                        cognometf.getText(),
                        codfisctf.getText(),
                        emailtf.getText(),
                        indirizzotf.getText(),
                        telefonotf.getText(),
                        null,
                        null
                );

                // Aggiunta del nuovo cliente nel controller
                c.newclt(newCliente);  // Aggiungi cliente nel database

                // Aggiunta della tessera per il nuovo cliente
                c.nuovatessera(nometf.getText(), cognometf.getText(), codfisctf.getText());

                // Aggiunta del cliente al modello
                c.clienteModel.addRow(new Object[]{
                        newCliente.getCodCl(), // Assumendo che tu abbia il codice cliente
                        newCliente.getNome(),
                        newCliente.getCognome(),
                        newCliente.getCodFis(),
                        newCliente.getEmail(),
                        newCliente.getInd(),
                        newCliente.getTel()
                });

                clean(); // Pulisce i campi dopo l'aggiunta
                JOptionPane.showMessageDialog(null, "Cliente e relativa tessera aggiunti");
            } catch (SQLException e1) {
                // Gestione dell'errore
                JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore: " + e1.getMessage());
            }
        });

        // Azione per il bottone "Pulisci"
        clearbutton.addActionListener(e -> clean());
    }

    public NuovoClienteFrame(String title, Controller c) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(NuovoClienteFrame.class.getResource("/Immagini/ImmIcon.png")));
        this.elementi();
        this.azioni(c);
    }
}
