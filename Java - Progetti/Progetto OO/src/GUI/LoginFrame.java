package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame {
    private JPanel contentPane;
    private JButton logbutt, clearbutt;
    private JTextField idtf;

    private void elementi(JPanel titlePanel) {
        setTitle("Green Market Point");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrame.class.getResource("/Immagini/ImmIcon.png")));
        setBounds(100, 100, 700, 450);
        setLocationRelativeTo(null);

        // Utilizza un pannello contenitore con BorderLayout
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

            // Creazione del pannello sinistro con l'immagine e il titolo
            JLabel titleLabel = new JLabel("Dalla terra al banco!", SwingConstants.LEFT);
            titleLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
            titleLabel.setForeground(new Color(39, 54, 24)); // Verde militare molto scuro
            titleLabel.setBorder(null);
            titlePanel.add(titleLabel, BorderLayout.NORTH);

        // Creazione del pannello destro con i controlli di login
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(150, 100, 100, 100));

        JLabel idLab = new JLabel("ID :");
        idLab.setAlignmentX(CENTER_ALIGNMENT);
        infoPanel.add(idLab);

        idtf = new JTextField("00000", 10);
        idtf.setHorizontalAlignment(SwingConstants.CENTER);
        idtf.setMaximumSize(new Dimension(200, 30));
        infoPanel.add(idtf);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        logbutt = new JButton("Login");
        logbutt.setBackground(Color.GREEN);
        logbutt.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(logbutt);

        buttonPanel.add(Box.createVerticalStrut(10));

        clearbutt = new JButton("Clear");
        clearbutt.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(clearbutt);

        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(buttonPanel);

        // Utilizza JSplitPane per dividere dinamicamente lo spazio tra i pannelli
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, titlePanel, infoPanel);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(null);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    private void azioni(Controller c) {
        logbutt.addActionListener(e -> {
            try {
                String id = idtf.getText();
                if ("00000".equals(id)) {
                    c.iddip = id;
                    c.logtoutente(1);
                    showMessage("Accesso Admin");
                } else if (c.verifyid(id)) {
                    c.iddip = id;
                    c.logtoutente(2);
                    showMessage("Accesso Dipendente");
                } else {
                    showMessage("Id errato!");
                }
                idtf.setText("");
            } catch (SQLException e1) {
                showMessage("Errore!\nTipo di errore : " + e1);
            }
        });

        clearbutt.addActionListener(e -> idtf.setText(""));

        idtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    logbutt.doClick();
                }
            }
        });
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(contentPane, message);
    }

    public LoginFrame(String title, Controller c) throws SQLException {
        super(title);
        c.connect();
        JPanel titlePanel = c.createImagePanel("/Immagini/ImmLog-2.jpg");
        SwingUtilities.invokeLater(() -> {
            elementi(titlePanel);
            azioni(c);
        });
    }
}