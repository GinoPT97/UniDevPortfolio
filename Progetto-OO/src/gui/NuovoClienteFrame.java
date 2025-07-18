package gui;

import controller.Controller;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NuovoClienteFrame extends JFrame {
    private JPanel contentPane;
    private final String[] labels = {"Nome:", "Cognome:", "Codice Fiscale:", "Email:", "Indirizzo:", "Telefono: +39"};
    private final JTextField[] fields = new JTextField[labels.length];
    private JButton addbutton;
    private JButton clearbutton;
    private JButton backbutton;

    public NuovoClienteFrame(String title, Controller c) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(NuovoClienteFrame.class.getResource("/Immagini/ImmIcon.png")));
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 520);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

        contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        // Pannello del titolo
        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(85, 107, 47));
        contentPane.add(titlepanel, BorderLayout.NORTH);
        JLabel titlelabel = new JLabel("Inserimento Nuovo Utente");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 32));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.setBorder(new EmptyBorder(18, 0, 18, 0));
        titlepanel.add(titlelabel);

        // Pannello principale per i campi di inserimento
        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(30, 80, 20, 80));
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
        contentPane.add(elempanel, BorderLayout.CENTER);

        for (int i = 0; i < labels.length; i++) {
            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Tahoma", Font.PLAIN, 15));
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            fields[i].setBackground(new Color(250, 250, 250));
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
            JPanel fieldPanel = createInputPanel(labels[i], fields[i]);
            fieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            elempanel.add(fieldPanel);
            elempanel.add(Box.createVerticalStrut(8));
        }
        
        JLabel tesseraLabel = new JLabel("La relativa tessera verrà creata in automatico");
        tesseraLabel.setFont(new Font("Tahoma", Font.ITALIC, 14));
        tesseraLabel.setForeground(new Color(85, 107, 47));
        tesseraLabel.setHorizontalAlignment(SwingConstants.CENTER);
        elempanel.add(tesseraLabel);

        // Pannello per i pulsanti
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonpanel.setBorder(new EmptyBorder(10, 0, 18, 0));
        contentPane.add(buttonpanel, BorderLayout.SOUTH);
        addbutton = creaButton("Aggiungi", new Color(34, 139, 34));
        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(addbutton);
        buttonpanel.add(clearbutton);
        buttonpanel.add(backbutton);
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Tahoma", Font.BOLD, 16));
        label.setPreferredSize(new Dimension(160, 32));
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        return panel;
    }

    private JButton creaButton(String text, Color color) {
        JButton button = new JButton(text, IconUtils.getIconForText(text, color));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Tahoma", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2, true),
                BorderFactory.createEmptyBorder(8, 28, 8, 28)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Effetto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }


    public void clean() {
        for (JTextField field : fields) {
            field.setText("");
            field.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }
    }

    private boolean validateFields() {
        boolean valid = true;
        int firstError = -1;
        for (int i = 0; i < fields.length; i++) {
            String text = fields[i].getText().trim();
            fields[i].setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            // Campo obbligatorio
            if (text.isEmpty()) {
                fields[i].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                valid = false;
                if (firstError == -1) firstError = i;
            }
        }
        // Email semplice
        String email = fields[3].getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            fields[3].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valid = false;
            if (firstError == -1) firstError = 3;
        }
        // Telefono numerico
        String tel = fields[5].getText().trim();
        if (!tel.isEmpty() && !tel.matches("^[0-9]{6,15}$")) {
            fields[5].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valid = false;
            if (firstError == -1) firstError = 5;
        }
        if (!valid && firstError != -1) {
            fields[firstError].requestFocus();
        }
        return valid;
    }

    private void azioni(Controller c) {
        backbutton.addActionListener(e -> { clean(); c.visAndElem(3, 3); });
        addbutton.addActionListener(e -> {
            if (!validateFields()) {
                JOptionPane.showMessageDialog(null, "Compila correttamente tutti i campi obbligatori.\nControlla email e telefono.", "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                c.newclt("",
                        fields[0].getText(), // nome
                        fields[1].getText(), // cognome
                        fields[2].getText(), // codFis
                        fields[3].getText(), // email
                        fields[4].getText(), // indirizzo
                        fields[5].getText()  // telefono
                );
                clean();
                JOptionPane.showMessageDialog(null, "Cliente e relativa tessera aggiunti");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage());
            }
        });
        clearbutton.addActionListener(e -> clean());
    }
}
