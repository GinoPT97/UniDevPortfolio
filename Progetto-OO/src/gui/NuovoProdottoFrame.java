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

    private final String[] labels = {"Nome:", "Descrizione:", "Provenienza:", "Prezzo:", "Data Raccolta (YYYY-MM-DD):", "Data Mungitura (YYYY-MM-DD):", "Data Produzione (YYYY-MM-DD):", "Glutine:", "Data Scadenza (YYYY-MM-DD):", "Scorta:"};
    private final JComponent[] fields = new JComponent[labels.length];
    private JButton backbutton;
    private JButton clearbutton;
    private JButton insertbutton;
    private JButton selbutton;
    private JComboBox<String> categoriacb;

    public NuovoProdottoFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(NuovoProdottoFrame.class.getResource("/Immagini/ImmIcon.png")));

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
        contentPane.add(elempanel, BorderLayout.CENTER);

        // Inizializza i componenti e li inserisce nell'array
        fields[0] = new JTextField(10); // Nome
        fields[1] = new JTextArea(2, 10); // Descrizione
        fields[2] = new JTextField(10); // Provenienza
        fields[3] = new JTextField(10); // Prezzo
        fields[4] = new JTextField(10); // Data Raccolta
        fields[5] = new JTextField(10); // Data Mungitura
        fields[6] = new JTextField(10); // Data Produzione
        fields[7] = new JCheckBox("Si"); // Glutine
        fields[8] = new JTextField(10); // Data Scadenza
        fields[9] = new JTextField(10); // Scorta

        for (int i = 0; i < labels.length; i++) {
            if (i == 1) {
                elempanel.add(createInputPanel(labels[i], fields[i]));
            } else if (i == 7) {
                elempanel.add(createInputPanel(labels[i], fields[i], false));
            } else if (i >= 4 && i <= 6 || i == 8) {
                elempanel.add(createInputPanel(labels[i], fields[i], false));
            } else {
                elempanel.add(createInputPanel(labels[i], fields[i]));
            }
        }

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
        JButton button = new JButton(text, gui.IconUtils.getIconForText(text, color));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    public void clean() {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof JTextField tf) tf.setText("");
            if (fields[i] instanceof JTextArea ta) ta.setText("");
            if (fields[i] instanceof JCheckBox cb) cb.setSelected(false);
        }
    }

    private void azioni(Controller c) {
        clearbutton.addActionListener(e -> clean());
        backbutton.addActionListener(e -> { clean(); c.visAndElem(4, 3); });
        insertbutton.addActionListener(e -> {
            DateFormat data = new SimpleDateFormat("yyyy-MM-dd");
            try {
                // Verifica che tutti i campi obbligatori siano compilati
                if (((JTextField)fields[0]).getText().isEmpty() || ((JTextArea)fields[1]).getText().isEmpty() ||
                        ((JTextField)fields[3]).getText().isEmpty() || ((JTextField)fields[2]).getText().isEmpty() ||
                        ((JTextField)fields[9]).getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci tutti i componenti");
                    return;
                }
                String categoria = categoriacb.getSelectedItem().toString();
                c.newprod(
                        "",
                        ((JTextField)fields[0]).getText(),
                        ((JTextArea)fields[1]).getText(),
                        Double.parseDouble(((JTextField)fields[3]).getText()),
                        ((JTextField)fields[2]).getText(),
                        (categoria.equals(FRUTTA) || categoria.equals(VERDURA)) ? new java.sql.Date(data.parse(((JTextField)fields[4]).getText()).getTime()) : null,
                        categoria.equals(LATTICINI) ? new java.sql.Date(data.parse(((JTextField)fields[5]).getText()).getTime()) : null,
                        ((JCheckBox)fields[7]).isSelected(),
                        (categoria.equals(UOVA) || categoria.equals(CONFEZIONATI) || categoria.equals(LATTICINI)) ? new java.sql.Date(data.parse(((JTextField)fields[8]).getText()).getTime()) : null,
                        categoria.equals(LATTICINI) ? new java.sql.Date(data.parse(((JTextField)fields[6]).getText()).getTime()) : null,
                        categoria,
                        Integer.parseInt(((JTextField)fields[9]).getText())
                );
                clean();
                JOptionPane.showMessageDialog(null, "Aggiunta effettuata");
            } catch (NumberFormatException | SQLException | ParseException e1) {
                JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore : " + e1);
            }
        });
        selbutton.addActionListener(event -> {
            int type = categoriacb.getSelectedIndex();
            ((JTextField)fields[4]).setEditable(false);
            ((JTextField)fields[5]).setEditable(false);
            ((JTextField)fields[6]).setEditable(false);
            ((JTextField)fields[8]).setEditable(false);
            ((JCheckBox)fields[7]).setEnabled(false);
            switch (type) {
                case 0, 1 -> ((JTextField)fields[4]).setEditable(true);
                case 2 -> ((JCheckBox)fields[7]).setEnabled(true);
                case 3 -> {
                    ((JTextField)fields[5]).setEditable(true);
                    ((JTextField)fields[6]).setEditable(true);
                    ((JTextField)fields[8]).setEditable(true);
                }
                case 4, 5 -> ((JTextField)fields[8]).setEditable(true);
            }
        });
        ((JTextArea)fields[1]).addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (((JTextArea)fields[1]).getText().length() >= 500) {
                    e.consume();
                }
            }
        });
    }
}
