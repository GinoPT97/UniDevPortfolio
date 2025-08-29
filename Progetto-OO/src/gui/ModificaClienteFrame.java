package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.Controller;

public class ModificaClienteFrame extends JFrame {
    private JPanel contentPane;
    private String cod;
    private final String[] labels = {"Nome:", "Cognome:", "Codice Fiscale:", "Email:", "Indirizzo:", "Telefono: +39"};
    private final JTextField[] fields = new JTextField[labels.length];
    private JButton backbutton;
    private JButton clearbutton;
    private JButton addbutton;

    public ModificaClienteFrame(String title, Controller c) {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(107, 142, 35));
        JLabel titlelabel = new JLabel("Modifica Cliente");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.WHITE);
        titlepanel.add(titlelabel);
        contentPane.add(titlepanel, BorderLayout.NORTH);

        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addbutton = creaButton("Inserisci", new Color(34, 139, 34));
        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(addbutton);
        buttonpanel.add(clearbutton);
        buttonpanel.add(backbutton);
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        JPanel elempanel = new JPanel(new GridBagLayout());
        elempanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        contentPane.add(elempanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int labelWidth = 120;
        Font labelFont = new Font("Tahoma", Font.BOLD, 16);
        for (int i = 0; i < labels.length; i++) {
            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Tahoma", Font.PLAIN, 15));
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
            fields[i].setBackground(new Color(250, 250, 250));
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
            JLabel label = new JLabel(labels[i]);
            label.setPreferredSize(new Dimension(labelWidth, 30));
            label.setFont(labelFont);
            gbc.gridx = 0;
            gbc.gridy = i;
            elempanel.add(label, gbc);
            gbc.gridx = 1;
            elempanel.add(fields[i], gbc);
        }
        // Forza il ridisegno del layout dopo aver aggiunto tutti i componenti
        contentPane.revalidate();
        contentPane.repaint();
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
        if (!tel.isEmpty() && !tel.matches("^\\d{6,15}$")) {
            fields[5].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valid = false;
            if (firstError == -1) firstError = 5;
        }
        if (!valid && firstError != -1)
			fields[firstError].requestFocus();
        return valid;
    }

    public void viewct(String codCl, String nome, String cognome, String codFis, String indirizzo, String email, String telefono) {
        cod = codCl;
        fields[0].setText(nome);
        fields[1].setText(cognome);
        fields[2].setText(codFis);
        fields[3].setText(email);
        fields[4].setText(indirizzo);
        fields[5].setText(telefono);
        // Forza il ridisegno del layout dopo il caricamento dati
        if (contentPane != null) {
            contentPane.revalidate();
            contentPane.repaint();
        }
    }

    private void azioni(Controller c) {
        backbutton.addActionListener(e -> { clean(); c.visAndElem(3, 3); });
        addbutton.addActionListener(e -> {
            if (!validateFields()) {
                JOptionPane.showMessageDialog(null, "Compila correttamente tutti i campi obbligatori.\nControlla email e telefono.", "Errore di validazione", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                c.upcliente(
                        cod,
                        fields[0].getText(),
                        fields[1].getText(),
                        fields[2].getText(),
                        fields[3].getText(),
                        fields[4].getText(),
                        fields[5].getText()
                );
                clean();
                c.visAndElem(3, 3);
                JOptionPane.showMessageDialog(null, "Cliente modificato");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage());
            }
        });
        clearbutton.addActionListener(e -> clean());
    }
}

