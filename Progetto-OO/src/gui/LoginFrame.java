package gui;

import controller.Controller;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private JPanel contentPane;
    private JButton logbutt;
    private JButton clearbutt;
    private JTextField idtf;

    public LoginFrame(String title, Controller c) throws SQLException {
        super(title);
        c.connect();
        JPanel titlePanel = new ImagePanel(new ImageIcon(ImagePanel.class.getResource("/Immagini/ImmLog.jpg")).getImage());
        SwingUtilities.invokeLater(() -> {
            elementi(titlePanel);
            azioni(c);
        });
    }

    private void elementi(JPanel titlePanel) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 450);
        setLocationRelativeTo(null);

        // Utilizza un pannello contenitore con BorderLayout
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Creazione del pannello sinistro con l'immagine e il titolo
        JLabel titleLabel = new JLabel("Dalla terra al banco!", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        titleLabel.setForeground(new Color(39, 54, 24));
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

        logbutt = new JButton("Login", gui.IconUtils.getIconForText("Login", Color.GREEN));
        logbutt.setBackground(new Color(46, 204, 113));
        logbutt.setForeground(Color.WHITE);
        logbutt.setFocusPainted(false);
        logbutt.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.add(logbutt);

        buttonPanel.add(Box.createVerticalStrut(10));

        clearbutt = new JButton("Clear", gui.IconUtils.getIconForText("Clear", Color.RED));
        clearbutt.setBackground(new Color(231, 76, 60));
        clearbutt.setForeground(Color.WHITE);
        clearbutt.setFocusPainted(false);
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
                if ("00000".equals(id) || "0".equals(id)) {
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
}