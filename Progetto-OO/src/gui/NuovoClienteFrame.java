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
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

        contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        // Pannello per i pulsanti
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentPane.add(buttonpanel, BorderLayout.SOUTH);
        addbutton = creaButton("Aggiungi", new Color(34, 139, 34));
        clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
        backbutton = creaButton("Indietro", new Color(178, 34, 34));
        buttonpanel.add(addbutton);
        buttonpanel.add(clearbutton);
        buttonpanel.add(backbutton);

        // Pannello principale per i campi di inserimento
        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        elempanel.setLayout(new GridLayout(labels.length + 1, 1, 10, 10));
        contentPane.add(elempanel, BorderLayout.CENTER);

        for (int i = 0; i < labels.length; i++) {
            fields[i] = new JTextField(20);
            elempanel.add(createInputPanel(labels[i], fields[i]));
        }
        elempanel.add(new JLabel("La relativa tessera verrà creata in automatico"));

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
        JButton button = new JButton(text, IconUtils.getIconForText(text, color));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Tahoma", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    public void clean() {
        for (JTextField field : fields) field.setText("");
    }

    private void azioni(Controller c) {
        backbutton.addActionListener(e -> { clean(); c.visAndElem(3, 3); });
        addbutton.addActionListener(e -> {
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
