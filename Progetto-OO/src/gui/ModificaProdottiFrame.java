package gui;

import controller.Controller;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ModificaProdottiFrame extends JFrame {
    // Costanti per le categorie
    private static final String FRUTTA = "FRUTTA";
    private static final String VERDURA = "VERDURA";
    private static final String FARINACEI = "FARINACEI";
    private static final String LATTICINI = "LATTICINI";
    private static final String UOVA = "UOVA";
    private static final String CONFEZIONATI = "CONFEZIONATI";
    
    private String cod;
    private JTextField nometf;
    private JTextField provtf;
    private JTextField prezzotf;
    private JTextField racctf;
    private JTextField mungtf;
    private JTextField prodtf;
    private JTextField scadtf;
    private JTextField scortatf;
    private JTextArea descta;
    private JCheckBox glutcb;
    private JComboBox<String> categoriacb;
    private JButton backbutton;
    private JButton updatebutton;
    private JButton clearbutton;

    public ModificaProdottiFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        // Impostazioni base della finestra
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 500); // Aumenta l'altezza della finestra
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0)); // Rimuovi margini
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Pannello centrale con layout BoxLayout
        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(0, 0, 0, 0)); // Rimuovi margini
        contentPane.add(elempanel, BorderLayout.CENTER);
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));

        // Inizializza i componenti prima di usarli
        nometf = new JTextField(10);
        descta = new JTextArea(1, 10);
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

        // Pannello per categorie
        JPanel categoriapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        categoriacb = new JComboBox<>(new String[]{FRUTTA, VERDURA, FARINACEI, LATTICINI, UOVA, CONFEZIONATI});
        categoriapanel.add(categoriacb);
        JButton selbutton = creaButton("Seleziona", new Color(46, 139, 87));
        categoriapanel.add(selbutton);
        elempanel.add(categoriapanel);

        // Aggiungi l'azione per il bottone "Seleziona"
        selbutton.addActionListener(event -> {
            // Abilita o disabilita i campi in base alla categoria selezionata
            int type = categoriacb.getSelectedIndex();
            racctf.setEditable(type == 0);
            mungtf.setEditable(type == 2);
            scadtf.setEditable(type == 1);
            glutcb.setEnabled(type == 3);
        });

        // Pannello dei bottoni
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updatebutton = creaButton("Inserisci", new Color(34, 139, 34));
        buttonpanel.add(updatebutton);
        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        buttonpanel.add(clearbutton);
        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(backbutton);
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        // Pannello del titolo
        JPanel panel = new JPanel();
        panel.setBackground(new Color(178, 34, 34));
        contentPane.add(panel, BorderLayout.NORTH);
        JLabel titlelabel = new JLabel("Modifica Prodotto");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        panel.add(titlelabel);
    }

    private JPanel createInputPanel(String labelText, JComponent inputComponent) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        boolean isTextField = inputComponent instanceof JTextField;
        if (isTextField) {
            ((JTextField) inputComponent).setEditable(true);
        }
        panel.add(inputComponent);
        return panel;
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
        JButton button = new JButton(text, gui.IconUtils.getIconForText(text, color));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    public void viewprod(String codProdotto, String nome, String descrizione, String luogoProvenienza, 
                        double prezzo, int scorta, boolean glutine, String categoria) {
        cod = codProdotto;
        nometf.setText(nome);
        descta.setText(descrizione);
        provtf.setText(luogoProvenienza);
        prezzotf.setText(String.valueOf(prezzo));
        scortatf.setText(String.valueOf(scorta));
        glutcb.setSelected(glutine);

        switch (categoria) {
            case FRUTTA -> categoriacb.setSelectedIndex(0);
            case VERDURA -> categoriacb.setSelectedIndex(1);
            case FARINACEI -> categoriacb.setSelectedIndex(2);
            case LATTICINI -> categoriacb.setSelectedIndex(3);
            case UOVA -> categoriacb.setSelectedIndex(4);
            case CONFEZIONATI -> categoriacb.setSelectedIndex(5);
            default -> categoriacb.setSelectedIndex(-1);
        }
    }


    public void clean() {
        JTextField[] textFields = {nometf, provtf, prezzotf, racctf, mungtf, prodtf, scadtf, scortatf};
        for (JTextField tf : textFields) {
            tf.setText("");
            tf.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }
        descta.setText("");
        descta.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextArea.border"));
        glutcb.setSelected(false);
    }

    private boolean validateFields() {
        boolean valid = true;
        int firstError = -1;
        // Campi obbligatori: nome, descrizione, provenienza, prezzo, scorta
        JTextField[] obbligatori = {nometf, provtf, prezzotf, scortatf};
        for (int i = 0; i < obbligatori.length; i++) {
            JTextField tf = obbligatori[i];
            String text = tf.getText().trim();
            tf.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            if (text.isEmpty()) {
                tf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                valid = false;
                if (firstError == -1) firstError = i;
            }
        }
        descta.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextArea.border"));
        if (descta.getText().trim().isEmpty()) {
            descta.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valid = false;
            if (firstError == -1) firstError = obbligatori.length;
        }
        // Prezzo numerico
        String prezzo = prezzotf.getText().trim();
        if (!prezzo.isEmpty()) {
            try {
                Double.parseDouble(prezzo);
            } catch (NumberFormatException ex) {
                prezzotf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                valid = false;
                if (firstError == -1) firstError = 2;
            }
        }
        // Scorta numerica
        String scorta = scortatf.getText().trim();
        if (!scorta.isEmpty() && !scorta.matches("^\\d+$")) {
            scortatf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valid = false;
            if (firstError == -1) firstError = 3;
        }
        // Date: formato yyyy-MM-dd se non vuote
        JTextField[] dateFields = {racctf, mungtf, prodtf, scadtf};
        for (int i = 0; i < dateFields.length; i++) {
            JTextField tf = dateFields[i];
            String text = tf.getText().trim();
            if (!text.isEmpty() && !text.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                tf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                valid = false;
                if (firstError == -1) firstError = obbligatori.length + 1 + i;
            }
        }
        // Focus sul primo errore
        if (!valid && firstError != -1) {
            if (firstError < obbligatori.length) obbligatori[firstError].requestFocus();
            else if (firstError == obbligatori.length) descta.requestFocus();
            else dateFields[firstError - obbligatori.length - 1].requestFocus();
        }
        return valid;
    }

    private void azioni(Controller c) {
        // Gestione del pulsante "Indietro"
        backbutton.addActionListener(e -> {
            clean(); // Pulisce i campi
            c.visAndElem(4, 3); // Torna alla schermata precedente
        });

        // Gestione del pulsante "Pulisci"
        clearbutton.addActionListener(e -> clean()); // Pulisce i campi

        // Gestione della selezione della categoria tramite JComboBox
        categoriacb.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedCategory = (String) categoriacb.getSelectedItem();

                // Disabilita tutti i campi relativi alle categorie
                racctf.setEnabled(false);
                mungtf.setEnabled(false);
                prodtf.setEnabled(false);
                scadtf.setEnabled(false);
                glutcb.setEnabled(false);

                // Abilita solo il campo pertinente alla categoria selezionata
                switch (selectedCategory) {
                    case FRUTTA, VERDURA -> racctf.setEnabled(true);
                    case FARINACEI -> glutcb.setEnabled(true);
                    case LATTICINI -> {
                        mungtf.setEnabled(true);
                        prodtf.setEnabled(true);
                        scadtf.setEnabled(true);
                    }
                    case UOVA, CONFEZIONATI -> scadtf.setEnabled(true);
                    default -> {
                        // Caso default per valori non previsti
                    }
                }
            }
        });

        // Gestione del pulsante "Inserisci/Modifica"
        updatebutton.addActionListener(e -> {
            if (!validateFields()) {
                JOptionPane.showMessageDialog(this, "Compila correttamente tutti i campi obbligatori.\nControlla i valori numerici e le date.", "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String categoria = categoriacb.getSelectedItem().toString();
                java.sql.Date dataRaccolta = null;
                java.sql.Date dataMungitura = null;
                java.sql.Date dataScadenza = null;
                if ((FRUTTA.equals(categoria) || VERDURA.equals(categoria)) && !racctf.getText().trim().isEmpty()) {
                    dataRaccolta = new java.sql.Date(dateFormat.parse(racctf.getText()).getTime());
                }
                if (LATTICINI.equals(categoria) && !mungtf.getText().trim().isEmpty()) {
                    dataMungitura = new java.sql.Date(dateFormat.parse(mungtf.getText()).getTime());
                }
                if ((UOVA.equals(categoria) || CONFEZIONATI.equals(categoria) || LATTICINI.equals(categoria)) && !scadtf.getText().trim().isEmpty()) {
                    dataScadenza = new java.sql.Date(dateFormat.parse(scadtf.getText()).getTime());
                }
                c.upprod(
                        cod,
                        nometf.getText(),
                        descta.getText(),
                        Double.parseDouble(prezzotf.getText()),
                        provtf.getText(),
                        dataRaccolta,
                        dataMungitura,
                        glutcb.isSelected(),
                        dataScadenza,
                        categoria,
                        Integer.parseInt(scortatf.getText())
                );
                JOptionPane.showMessageDialog(this, "Prodotto modificato", "Successo", JOptionPane.INFORMATION_MESSAGE);
                clean();
                c.visAndElem(4, 3);
            } catch (NumberFormatException | ParseException | java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}