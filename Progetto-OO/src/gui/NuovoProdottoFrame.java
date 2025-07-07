package gui;

import controller.Controller;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NuovoProdottoFrame extends JFrame {
    // Costanti per le categorie
    private static final String FRUTTA = "FRUTTA";
    private static final String VERDURA = "VERDURA";
    private static final String FARINACEI = "FARINACEI";
    private static final String LATTICINI = "LATTICINI";
    private static final String UOVA = "UOVA";
    private static final String CONFEZIONATI = "CONFEZIONATI";
    
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
    private JTextField prodtf;
    private JTextArea descta;
    private JCheckBox glutcb;
    private JComboBox<String> categoriacb;

    public NuovoProdottoFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        // Configurazione della finestra
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 500); // Aumenta l'altezza della finestra
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(NuovoProdottoFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Pannello principale
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0)); // Rimuovi margini
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Pannello per il contenuto centrale
        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // Rimuovi margini
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
        contentPane.add(elempanel, BorderLayout.CENTER);

        // Inizializza i componenti prima di usarli
        nometf = new JTextField(10);
        descta = new JTextArea(2, 10);
        provtf = new JTextField(10);
        prezzotf = new JTextField(10);
        racctf = new JTextField(10);
        mungtf = new JTextField(10);
        prodtf = new JTextField(10);
        glutcb = new JCheckBox("Si");
        scadtf = new JTextField(10);
        scortatf = new JTextField(10);
        
        // Crea i pannelli di input
        elempanel.add(createInputPanel("Nome :", nometf));
        elempanel.add(createInputPanel("Descrizione :", descta));
        elempanel.add(createInputPanel("Provenienza :", provtf));
        elempanel.add(createInputPanel("Prezzo :", prezzotf));
        elempanel.add(createInputPanel("Data Raccolta (YYYY-MM-DD) :", racctf, false));
        elempanel.add(createInputPanel("Data Mungitura (YYYY-MM-DD) :", mungtf, false));
        elempanel.add(createInputPanel("Data Produzione (YYYY-MM-DD) :", prodtf, false));
        elempanel.add(createInputPanel("Glutine :", glutcb, false));
        elempanel.add(createInputPanel("Data Scadenza (YYYY-MM-DD) :", scadtf, false));
        elempanel.add(createInputPanel("Scorta :", scortatf));

        // Categoria e pulsante di selezione
        JPanel categoriapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        categoriacb = new JComboBox<>(new String[]{FRUTTA, VERDURA, FARINACEI, LATTICINI, UOVA, CONFEZIONATI});
        categoriapanel.add(categoriacb);
        selbutton = creaButton("Selezione", new Color(46, 139, 87));
        categoriapanel.add(selbutton);
        elempanel.add(categoriapanel);

        // Pannello per i pulsanti di azione
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
        if (inputComponent instanceof JTextField textField) {
            textField.setEditable(editable);
        } else if (inputComponent instanceof JCheckBox checkBox) {
            checkBox.setEnabled(editable);
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
        prodtf.setText("");
        scadtf.setText("");
        glutcb.setSelected(false);
    }

    private void azioni(Controller c) {
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
                
                // Salva il prodotto nel database utilizzando il metodo refactorizzato
                c.newprod(
                        "", // codProdotto
                        nometf.getText(), // nome
                        descta.getText(), // descrizione
                        Double.parseDouble(prezzotf.getText()), // prezzo
                        provtf.getText(), // luogoProvenienza
                        (categoria.equals(FRUTTA) || categoria.equals(VERDURA)) ? new java.sql.Date(data.parse(racctf.getText()).getTime()) : null, // dataRaccolta
                        categoria.equals(LATTICINI) ? new java.sql.Date(data.parse(mungtf.getText()).getTime()) : null, // dataMungitura
                        glutcb.isSelected(), // glutine
                        (categoria.equals(UOVA) || categoria.equals(CONFEZIONATI) || categoria.equals(LATTICINI)) ? new java.sql.Date(data.parse(scadtf.getText()).getTime()) : null, // dataScadenza
                        categoria.equals(LATTICINI) ? new java.sql.Date(data.parse(prodtf.getText()).getTime()) : null, // dataproduzione
                        categoria, // categoria
                        Integer.parseInt(scortatf.getText()) // scorta
                );

                // Pulisci i campi e mostra un messaggio di successo
                clean();
                JOptionPane.showMessageDialog(null, "Aggiunta effettuata");

            } catch (NumberFormatException | SQLException | ParseException e1) {
                JOptionPane.showMessageDialog(null, """
                        Errore!
                        Tipo di errore : """ + e1);
            }
        });

        selbutton.addActionListener(event -> {
            // Abilita o disabilita i campi in base alla categoria selezionata
            int type = categoriacb.getSelectedIndex();
            
            // Reset di tutti i campi
            racctf.setEditable(false);
            mungtf.setEditable(false);
            prodtf.setEditable(false);
            scadtf.setEditable(false);
            glutcb.setEnabled(false);
            
            // Abilita i campi specifici per categoria
            switch (type) {
                case 0, 1 -> // FRUTTA, VERDURA
                    racctf.setEditable(true);
                case 2 -> // FARINACEI
                    glutcb.setEnabled(true);
                case 3 -> { // LATTICINI
                    mungtf.setEditable(true);
                    prodtf.setEditable(true);
                    scadtf.setEditable(true);
                }
                case 4, 5 -> // UOVA, CONFEZIONATI
                    scadtf.setEditable(true);
                default -> {
                    // Caso default per valori non previsti
                }
            }
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
