package gui;

import controller.Controller;
import java.awt.*;
import java.awt.event.ItemEvent;
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
        setBounds(100, 100, 1000, 765); // Stesse dimensioni di NuovoProdottoFrame
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        setLocationRelativeTo(null);

        // Pannello centrale migliorato
        JPanel elempanel = new JPanel();
        elempanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        elempanel.setBackground(new Color(245, 245, 250));
        contentPane.add(elempanel, BorderLayout.CENTER);
        elempanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Inizializza i componenti prima di usarli
        nometf = new JTextField(24);
        descta = new JTextArea();
        descta.setRows(5);
        descta.setColumns(28);
        descta.setLineWrap(true);
        descta.setWrapStyleWord(true);
        descta.setForeground(Color.GRAY);
        descta.setText("Inserisci la descrizione");
        // Placeholder coerente con NuovoProdottoFrame
        descta.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (descta.getText().equals("Inserisci la descrizione")) {
                    descta.setText("");
                    descta.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (descta.getText().isEmpty()) {
                    descta.setForeground(Color.GRAY);
                    descta.setText("Inserisci la descrizione");
                }
            }
        });
        provtf = new JTextField(24);
        prezzotf = new JTextField(14);
        racctf = new JTextField(16);
        mungtf = new JTextField(16);
        prodtf = new JTextField(16);
        glutcb = new JCheckBox("Contiene glutine");
        scadtf = new JTextField(16);
        scortatf = new JTextField(12);

        Font labelFont = new Font("Tahoma", Font.BOLD, 16);
        Font fieldFont = new Font("Tahoma", Font.PLAIN, 15);

        int row = 0;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel nomeLabel = new JLabel("Nome prodotto:");
        nomeLabel.setFont(labelFont);
        elempanel.add(nomeLabel, gbc);
        gbc.gridx = 1;
        nometf.setFont(fieldFont);
        elempanel.add(nometf, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel descLabel = new JLabel("Descrizione:");
        descLabel.setFont(labelFont);
        elempanel.add(descLabel, gbc);
        gbc.gridx = 1;
        Dimension areaDim = new Dimension(280, 80);
        JScrollPane descScroll = new JScrollPane(descta);
        descScroll.setMinimumSize(areaDim);
        descScroll.setPreferredSize(areaDim);
        descScroll.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(descScroll, BorderLayout.CENTER);
        JLabel charCountLabel = new JLabel("0/500 caratteri");
        charCountLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
        charCountLabel.setForeground(Color.DARK_GRAY);
        descPanel.add(charCountLabel, BorderLayout.SOUTH);
        descta.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                String text = descta.getText();
                if (text.equals("Inserisci la descrizione")) text = "";
                if (text.length() >= 500 && descta.getSelectedText() == null) {
                    e.consume();
                }
            }
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String text = descta.getText();
                if (text.equals("Inserisci la descrizione")) text = "";
                charCountLabel.setText(text.length() + "/500 caratteri");
            }
        });
        elempanel.add(descPanel, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel provLabel = new JLabel("Provenienza:");
        provLabel.setFont(labelFont);
        elempanel.add(provLabel, gbc);
        gbc.gridx = 1;
        provtf.setFont(fieldFont);
        elempanel.add(provtf, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel prezzoLabel = new JLabel("Prezzo (€):");
        prezzoLabel.setFont(labelFont);
        elempanel.add(prezzoLabel, gbc);
        gbc.gridx = 1;
        prezzotf.setFont(fieldFont);
        elempanel.add(prezzotf, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel raccLabel = new JLabel("Data Raccolta:");
        raccLabel.setFont(labelFont);
        elempanel.add(raccLabel, gbc);
        gbc.gridx = 1;
        racctf.setFont(fieldFont);
        elempanel.add(racctf, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel mungLabel = new JLabel("Data Mungitura:");
        mungLabel.setFont(labelFont);
        elempanel.add(mungLabel, gbc);
        gbc.gridx = 1;
        mungtf.setFont(fieldFont);
        elempanel.add(mungtf, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel prodLabel = new JLabel("Data Produzione:");
        prodLabel.setFont(labelFont);
        elempanel.add(prodLabel, gbc);
        gbc.gridx = 1;
        prodtf.setFont(fieldFont);
        elempanel.add(prodtf, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel glutLabel = new JLabel("Glutine:");
        glutLabel.setFont(labelFont);
        elempanel.add(glutLabel, gbc);
        gbc.gridx = 1;
        glutcb.setFont(fieldFont);
        elempanel.add(glutcb, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel scadLabel = new JLabel("Data Scadenza:");
        scadLabel.setFont(labelFont);
        elempanel.add(scadLabel, gbc);
        gbc.gridx = 1;
        scadtf.setFont(fieldFont);
        elempanel.add(scadtf, gbc);

        row++;
        gbc.gridy = row; gbc.gridx = 0;
        JLabel scortaLabel = new JLabel("Scorta:");
        scortaLabel.setFont(labelFont);
        elempanel.add(scortaLabel, gbc);
        gbc.gridx = 1;
        scortatf.setFont(fieldFont);
        elempanel.add(scortatf, gbc);

        // Categoria e pulsanti
        row++;
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel categoriapanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        categoriapanel.setOpaque(false);
        categoriacb = new JComboBox<>(new String[]{FRUTTA, VERDURA, FARINACEI, LATTICINI, UOVA, CONFEZIONATI});
        categoriacb.setFont(fieldFont);
        categoriapanel.add(categoriacb);
        // Listener centralizzato solo qui
        categoriacb.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                aggiornaCampiCategoria((String) categoriacb.getSelectedItem());
            }
        });

        // Pannello dei bottoni separato e distanziato
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonpanel.setBorder(new EmptyBorder(28, 0, 28, 0));
        buttonpanel.setBackground(new Color(245, 245, 250));
        updatebutton = creaButton("Inserisci", new Color(34, 139, 34));
        updatebutton.setFont(new Font("Tahoma", Font.BOLD, 18));
        buttonpanel.add(updatebutton);
        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        clearbutton.setFont(new Font("Tahoma", Font.BOLD, 18));
        buttonpanel.add(clearbutton);
        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        backbutton.setFont(new Font("Tahoma", Font.BOLD, 18));
        buttonpanel.add(backbutton);
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        // Pannello del titolo
        JPanel panel = new JPanel();
        panel.setBackground(new Color(178, 34, 34));
        contentPane.add(panel, BorderLayout.NORTH);
        JLabel titlelabel = new JLabel("Modifica Prodotto");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 32));
        titlelabel.setForeground(Color.WHITE);
        panel.add(titlelabel);

        // Migliora visibilità campi disabilitati
        UIManager.put("TextField.inactiveBackground", new Color(230, 230, 230));
        UIManager.put("TextArea.inactiveBackground", new Color(230, 230, 230));
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
        descta.setForeground(Color.GRAY);
        descta.setText("Inserisci la descrizione");
        descta.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextArea.border"));
        glutcb.setSelected(false);
        aggiornaCampiCategoria((String) categoriacb.getSelectedItem());
    }
    // Metodo per abilitare/disabilitare i campi in base alla categoria
    private void aggiornaCampiCategoria(String categoria) {
        racctf.setEnabled(false);
        mungtf.setEnabled(false);
        prodtf.setEnabled(false);
        scadtf.setEnabled(false);
        glutcb.setEnabled(false);

        if (categoria == null) return;
        if (categoria.equals(FRUTTA) || categoria.equals(VERDURA)) {
            racctf.setEnabled(true);
        } else if (categoria.equals(FARINACEI)) {
            glutcb.setEnabled(true);
        } else if (categoria.equals(LATTICINI)) {
            mungtf.setEnabled(true);
            prodtf.setEnabled(true);
            scadtf.setEnabled(true);
        } else if (categoria.equals(UOVA) || categoria.equals(CONFEZIONATI)) {
            scadtf.setEnabled(true);
        }
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
            try {
                String categoria = categoriacb.getSelectedItem().toString();
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
                java.time.LocalDate dataRaccolta = null;
                java.time.LocalDate dataMungitura = null;
                java.time.LocalDate dataScadenza = null;
                if ((FRUTTA.equals(categoria) || VERDURA.equals(categoria)) && !racctf.getText().trim().isEmpty()) {
                    dataRaccolta = java.time.LocalDate.parse(racctf.getText().trim(), formatter);
                }
                if (LATTICINI.equals(categoria) && !mungtf.getText().trim().isEmpty()) {
                    dataMungitura = java.time.LocalDate.parse(mungtf.getText().trim(), formatter);
                }
                if ((UOVA.equals(categoria) || CONFEZIONATI.equals(categoria) || LATTICINI.equals(categoria)) && !scadtf.getText().trim().isEmpty()) {
                    dataScadenza = java.time.LocalDate.parse(scadtf.getText().trim(), formatter);
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
            } catch (NumberFormatException | java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}