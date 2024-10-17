package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private JPanel contentPane;
    private JButton logbutt;
    private JButton clearbutt;
    private JTextField idtf;

    public void elementi(Controller c) {
        setBounds(100, 100, 700, 450);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Creazione del pannello di sfondo con l'immagine desiderata
        JPanel titlepanel = c.createBackgroundPanel("/Immagini/ImmLog.jpg");
        titlepanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        titlepanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel titlelabel = new JLabel("Ortofrutta 2.0");
        titlelabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlepanel.add(titlelabel);

        // Pannello info per i campi ID
        JPanel infopanel = new JPanel();
        infopanel.setBorder(new EmptyBorder(150, 100, 100, 100));
        infopanel.setLayout(new BoxLayout(infopanel, BoxLayout.Y_AXIS));

        JLabel idlab = new JLabel("ID :");
        idlab.setAlignmentX(CENTER_ALIGNMENT);
        infopanel.add(idlab);

        idtf = new JTextField();
        idtf.setText("00000");
        idtf.setHorizontalAlignment(SwingConstants.CENTER);
        idtf.setColumns(10);
        idtf.setMaximumSize(new Dimension(200, 30));  // Imposta una dimensione massima per evitare l'allungamento
        infopanel.add(idtf);

        // Pannello per i bottoni
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS)); // Imposta i bottoni uno sotto l'altro

        logbutt = new JButton("Login");
        logbutt.setAlignmentX(CENTER_ALIGNMENT);
        logbutt.setBackground(Color.GREEN);
        buttonpanel.add(logbutt);
        buttonpanel.add(Box.createVerticalStrut(10)); // Spazio tra i bottoni

        clearbutt = new JButton("Clear");
        clearbutt.setAlignmentX(CENTER_ALIGNMENT);
        buttonpanel.add(clearbutt);

        // Aggiungi il buttonpanel sotto il campo ID
        infopanel.add(Box.createVerticalStrut(20));  // Spazio tra il campo ID e i bottoni
        infopanel.add(buttonpanel);

        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.add(infopanel, BorderLayout.CENTER);

        // Aggiungi il titlepanel come pannello superiore
        contentPane.add(titlepanel, BorderLayout.NORTH);
    }

    public void azioni(Controller c) {
        logbutt.addActionListener(e -> {
            try {
                if (idtf.getText().equals("00000")) {
                    c.iddip = idtf.getText();
                    c.loginUtente(1);
                    idtf.setText("");
                    JOptionPane.showMessageDialog(contentPane, "Accesso Admin");
                } else if (c.verifyid(idtf.getText())) {
                    c.iddip = idtf.getText();
                    c.loginUtente(2);
                    idtf.setText("");
                    JOptionPane.showMessageDialog(contentPane, "Accesso Dipendente");
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Id errato!");
                    idtf.setText("");
                }
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
            }
        });

        clearbutt.addActionListener(e -> idtf.setText(""));

        idtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    logbutt.doClick();  // Simula il click del bottone "Login" quando si preme Invio
                }
            }
        });
    }

    public LoginFrame(String title, Controller c) throws SQLException {
        super(title);
        c.connect();
        this.elementi(c);
        this.azioni(c);
    }
}
