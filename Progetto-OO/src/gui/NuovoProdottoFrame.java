package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Prodotto;

import javax.swing.JComponent;
import javax.swing.BorderFactory;

public class NuovoProdottoFrame extends JFrame {
    private JPanel contentPane;
    private JPanel buttonpanel;
    private JButton backbutton;
    private JButton clearbutton;
    private JButton insertbutton;
    private JButton selbutton;
    private JTextField nometf;
    private JTextField provtf;
    private JTextField prezzotf;
    private JTextField racctf;
    private JTextField mungtf;
    private JTextField scadtf;
    private JTextField scortatf;
    private JTextArea descta;
    private JCheckBox glutcb;
    private JComboBox<String> categoriacb;

    public NuovoProdottoFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    public void elementi() {
        // Configurazione della finestra
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 500); // Aumenta l'altezza della finestra
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(NuovoProdottoFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Pannello principale
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0)); // Rimuovi margini
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Pannello per il contenuto centrale
        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // Rimuovi margini
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
        contentPane.add(elempanel, BorderLayout.CENTER);

        // Metodo per creare i pannelli di input
        elempanel.add(createInputPanel("Nome :", nometf = new JTextField(10)));
        elempanel.add(createInputPanel("Descrizione :", descta = new JTextArea(2, 10)));
        elempanel.add(createInputPanel("Provenienza :", provtf = new JTextField(10)));
        elempanel.add(createInputPanel("Prezzo :", prezzotf = new JTextField(10)));
        elempanel.add(createInputPanel("Data Raccolta (YYYY-MM-DD) :", racctf = new JTextField(10), false));
        elempanel.add(createInputPanel("Data Mungitura (YYYY-MM-DD) :", mungtf = new JTextField(10), false));
        elempanel.add(createInputPanel("Glutine :", glutcb = new JCheckBox("Si"), false));
        elempanel.add(createInputPanel("Data Scadenza (YYYY-MM-DD) :", scadtf = new JTextField(10), false));
        elempanel.add(createInputPanel("Scorta :", scortatf = new JTextField(10)));

        // Categoria e pulsante di selezione
        JPanel categoriapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        categoriacb = new JComboBox<>(new String[]{"Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"});
        categoriapanel.add(categoriacb);
        selbutton = creaButton("Selezione", new Color(46, 139, 87));
        categoriapanel.add(selbutton);
        elempanel.add(categoriapanel);

        // Pannello per i pulsanti di azione
        buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        insertbutton = creaButton("Inserisci", new Color(34, 139, 34));
        buttonpanel.add(insertbutton);
        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        buttonpanel.add(clearbutton);
        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(backbutton);
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        // Pannello per il titolo
        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(139, 0, 0));
        JLabel titlelabel = new JLabel("Inserimento nuovo prodotto");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);
        contentPane.add(titlepanel, BorderLayout.NORTH);
    }

    private JPanel createInputPanel(String labelText, JComponent inputComponent) {
        return createInputPanel(labelText, inputComponent, true);
    }

    private JPanel createInputPanel(String labelText, JComponent inputComponent, boolean editable) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        if (inputComponent instanceof JTextField) {
            ((JTextField) inputComponent).setEditable(editable);
        } else if (inputComponent instanceof JCheckBox) {
            ((JCheckBox) inputComponent).setEnabled(editable);
        }
        panel.add(inputComponent);
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
        descta.setText("");
        prezzotf.setText("");
        provtf.setText("");
        scortatf.setText("");
        racctf.setText("");
        mungtf.setText("");
        scadtf.setText("");
        glutcb.setSelected(false);
    }

    public void azioni(Controller c) {
        clearbutton.addActionListener(e -> clean());

        backbutton.addActionListener(e -> {
            clean();
            c.visAndElem(4, 3);
        });

        insertbutton.addActionListener(e -> {
            DateFormat data = new SimpleDateFormat("yyyy-MM-dd");
            try {
                // Verifica che tutti i campi obbligatori siano compilati
                if (nometf.getText().isEmpty() || descta.getText().isEmpty() ||
                        prezzotf.getText().isEmpty() || provtf.getText().isEmpty() ||
                        scortatf.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci tutti i componenti");
                    return;
                }

                // Prepara i dati da salvare in base alla categoria selezionata
                String categoria = categoriacb.getSelectedItem().toString();
                Prodotto prodotto = new Prodotto(
                        "", nometf.getText(), descta.getText(),
                        Double.parseDouble(prezzotf.getText()), provtf.getText(),
                        categoria.equals("Ortofrutticoli") ? data.parse(racctf.getText()) : null,
                        categoria.equals("Latticini") ? data.parse(mungtf.getText()) : null,
                        glutcb.isSelected(),
                        categoria.equals("Inscatolati") ? data.parse(scadtf.getText()) : null,
                        categoria, Integer.parseInt(scortatf.getText())
                );

                // Salva il prodotto nel database
                c.newprod(prodotto);

                // Aggiungi il prodotto anche al modello della tabella
                c.prodModel.addRow(new Object[]{
                        prodotto.getNome(),
                        prodotto.getDescrizione(),
                        prodotto.getPrezzo(),
                        prodotto.getLuogoProv(),
                        prodotto.getCategoria(),
                        prodotto.getScorta()
                });

                // Pulisci i campi e mostra un messaggio di successo
                clean();
                JOptionPane.showMessageDialog(null, "Aggiunta effettuata");

            } catch (NumberFormatException | SQLException | ParseException e1) {
                JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
            }
        });

        selbutton.addActionListener(event -> {
            // Abilita o disabilita i campi in base alla categoria selezionata
            int type = categoriacb.getSelectedIndex();
            racctf.setEditable(type == 0);
            mungtf.setEditable(type == 2);
            scadtf.setEditable(type == 1);
            glutcb.setEnabled(type == 3);
        });

        descta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (descta.getText().length() >= 500) {
                    e.consume();
                }
            }
        });
    }
}
