package gui;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

public class ModificaProdottiFrame extends JFrame {
    private String cod;
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
    private JButton backbutton;
    private JButton updatebutton;
    private JButton clearbutton;

    public ModificaProdottiFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    public void elementi() {
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

        // Metodo per creare i pannelli di input
        elempanel.add(createInputPanel("Nome :", nometf = new JTextField(10)));
        elempanel.add(createInputPanel("Descrizione :", descta = new JTextArea(1, 10)));
        elempanel.add(createInputPanel("Provenienza :", provtf = new JTextField(10)));
        elempanel.add(createInputPanel("Prezzo :", prezzotf = new JTextField(10)));
        elempanel.add(createInputPanel("Data Raccolta (YYYY-MM-DD) :", racctf = new JTextField(10), false));
        elempanel.add(createInputPanel("Data Mungitura (YYYY-MM-DD) :", mungtf = new JTextField(10), false));
        elempanel.add(createInputPanel("Glutine :", glutcb = new JCheckBox("Si"), false));
        elempanel.add(createInputPanel("Data Scadenza (YYYY-MM-DD) :", scadtf = new JTextField(10), false));
        elempanel.add(createInputPanel("Scorta :", scortatf = new JTextField(10)));

        // Pannello per categorie
        JPanel categoriapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        categoriacb = new JComboBox<>(new String[]{"Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei"});
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
            case "Ortofrutticoli":
                categoriacb.setSelectedIndex(0);
                break;
            case "Inscatolati":
                categoriacb.setSelectedIndex(1);
                break;
            case "Latticini":
                categoriacb.setSelectedIndex(2);
                break;
            case "Farinacei":
                categoriacb.setSelectedIndex(3);
                break;
            default:
                categoriacb.setSelectedIndex(-1);
                break;
        }
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

                // Mappa campi legati alla categoria
                Map<String, JTextField> categoryFields = Map.of(
                        "Ortofrutticoli", racctf,
                        "Latticini", mungtf,
                        "Inscatolati", scadtf
                );

                // Disabilita tutti i campi relativi alle categorie
                categoryFields.values().forEach(field -> field.setEnabled(false));

                // Abilita solo il campo pertinente alla categoria selezionata
                JTextField fieldToEnable = categoryFields.getOrDefault(selectedCategory, null);
                if (fieldToEnable != null) {
                    fieldToEnable.setEnabled(true);
                }
            }
        });

        // Gestione del pulsante "Inserisci/Modifica"
        updatebutton.addActionListener(e -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                // Verifica se i campi obbligatori sono compilati
                List<JTextComponent> mandatoryFields = List.of(nometf, descta, prezzotf, provtf, scortatf);
                if (mandatoryFields.stream().anyMatch(field -> field.getText().isEmpty())) {
                    JOptionPane.showMessageDialog(this, "Inserisci tutti i campi obbligatori", "Errore", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Aggiorna il prodotto nel database utilizzando il metodo refactorizzato
                String categoria = categoriacb.getSelectedItem().toString();
                c.upprod(
                        cod,
                        nometf.getText(),
                        descta.getText(),
                        Double.parseDouble(prezzotf.getText()),
                        provtf.getText(),
                        "Ortofrutticoli".equals(categoria) ? new java.sql.Date(dateFormat.parse(racctf.getText()).getTime()) : null,
                        "Latticini".equals(categoria) ? new java.sql.Date(dateFormat.parse(mungtf.getText()).getTime()) : null,
                        glutcb.isSelected(),
                        "Inscatolati".equals(categoria) ? new java.sql.Date(dateFormat.parse(scadtf.getText()).getTime()) : null,
                        categoria,
                        Integer.parseInt(scortatf.getText())
                );

                // Cerca la riga corrispondente nel modello
                int rowIndex = IntStream.range(0, c.prodModel.getRowCount())
                        .filter(i -> c.prodModel.getValueAt(i, 0).equals(cod))
                        .findFirst().orElse(-1);

                if (rowIndex != -1) {
                    // Aggiorna direttamente con i valori dai campi
                    c.prodModel.setValueAt(nometf.getText(), rowIndex, 1);
                    c.prodModel.setValueAt(descta.getText(), rowIndex, 2);
                    c.prodModel.setValueAt(Double.parseDouble(prezzotf.getText()), rowIndex, 3);
                    c.prodModel.setValueAt(provtf.getText(), rowIndex, 4);
                    c.prodModel.setValueAt(categoria, rowIndex, 9);
                    c.prodModel.setValueAt(Integer.parseInt(scortatf.getText()), rowIndex, 10);

                    JOptionPane.showMessageDialog(this, "Prodotto modificato", "Successo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Prodotto non trovato!", "Errore", JOptionPane.ERROR_MESSAGE);
                }

                clean();
                c.visAndElem(4, 3);
            } catch (NumberFormatException | ParseException | java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}