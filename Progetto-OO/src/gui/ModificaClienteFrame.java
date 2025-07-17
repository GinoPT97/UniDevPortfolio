package gui;

import controller.Controller;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaClienteFrame.class.getResource("/Immagini/ImmIcon.png")));

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

        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
        contentPane.add(elempanel, BorderLayout.CENTER);

        for (int i = 0; i < labels.length; i++) {
            fields[i] = new JTextField(10);
            elempanel.add(createInputPanel(labels[i], fields[i]));
        }
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(textField);
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
        if (!valid && firstError != -1) {
            fields[firstError].requestFocus();
        }
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

