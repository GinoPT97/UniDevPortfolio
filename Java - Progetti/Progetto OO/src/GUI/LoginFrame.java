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
import javax.swing.border.EmptyBorder;

import Model.ImagePanel;

import javax.swing.ImageIcon;

public class LoginFrame extends JFrame {
    private JPanel contentPane;
    private JButton logbutt, clearbutt;
    private JTextField idtf;
    private ImagePanel imagePanel;

    private void elementi() {
        setBounds(100, 100, 700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(238, 238, 238));
        setContentPane(contentPane);

        JPanel titlepanel = new JPanel();
        titlepanel.setBackground(new Color(0, 128, 0));
        titlepanel.setLayout(new BorderLayout());
        JLabel titlelabel = new JLabel("Ortofrutta 2.0", SwingConstants.CENTER);
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlepanel.add(titlelabel, BorderLayout.CENTER);

        imagePanel = new ImagePanel("/Immagini/ImmLog-1.png");
        titlepanel.add(imagePanel, BorderLayout.WEST);

        JPanel infopanel = new JPanel();
        infopanel.setLayout(new BoxLayout(infopanel, BoxLayout.Y_AXIS));
        infopanel.setBorder(new EmptyBorder(150, 100, 100, 100));

        JLabel idlab = new JLabel("ID :");
        idlab.setAlignmentX(CENTER_ALIGNMENT);
        infopanel.add(idlab);

        idtf = new JTextField("00000", 10);
        idtf.setHorizontalAlignment(SwingConstants.CENTER);
        idtf.setMaximumSize(new Dimension(200, 30));
        infopanel.add(idtf);

        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS));

        logbutt = new JButton("Login");
        logbutt.setBackground(Color.GREEN);
        logbutt.setAlignmentX(CENTER_ALIGNMENT);
        buttonpanel.add(logbutt);

        buttonpanel.add(Box.createVerticalStrut(10)); // Spazio tra i bottoni

        clearbutt = new JButton("Clear");
        clearbutt.setAlignmentX(CENTER_ALIGNMENT);
        buttonpanel.add(clearbutt);

        infopanel.add(Box.createVerticalStrut(20)); // Spazio tra il campo ID e i bottoni
        infopanel.add(buttonpanel);

        contentPane.add(titlepanel, BorderLayout.NORTH);
        contentPane.add(infopanel, BorderLayout.CENTER);
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
        this.elementi();
        this.azioni(c);
    }
}






