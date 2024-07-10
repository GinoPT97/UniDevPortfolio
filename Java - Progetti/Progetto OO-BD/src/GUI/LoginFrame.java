package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/*
public class LoginFrame extends JFrame {
    private Controller c;
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
		JPanel buttonpanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonpanel.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);

		JPanel infopanel = new JPanel();
		infopanel.setBorder(new EmptyBorder(150, 100, 100, 100));

		JPanel titlepanel = new JPanel();
		titlepanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		titlepanel.setBackground(new Color(0, 128, 0));
		titlepanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel titlelabel = new JLabel("");
		titlelabel.setHorizontalAlignment(SwingConstants.CENTER);
		titlelabel.setVerticalAlignment(SwingConstants.TOP);
		titlepanel.add(titlelabel);
		titlelabel.setText("Ortofrutta 2000");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		logbutt = new JButton("Login");
		buttonpanel.add(logbutt);
		logbutt.setVerticalAlignment(SwingConstants.TOP);
		logbutt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(c.verifyid(idtf.getText())) {
						 c.iddip = idtf.getText();
						 c.logtoutente(2);
						 idtf.setText("");
						 JOptionPane.showMessageDialog(contentPane, "Accesso Dipendente");
					   } else if(idtf.getText().equals("00000")) {
						   c.logtoutente(1);
						   idtf.setText("");
						   JOptionPane.showMessageDialog(contentPane, "Accesso Admin");
					   } else {
						   JOptionPane.showMessageDialog(contentPane, "Id errato!");
						   idtf.setText("");
					   }
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
				}
			}
		});
		logbutt.setBackground(Color.GREEN);

		clearbutt = new JButton("Clear");
		buttonpanel.add(clearbutt);
		clearbutt.setVerticalAlignment(SwingConstants.BOTTOM);
		clearbutt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				idtf.setText("");
			}
		});
		contentPane.add(infopanel, BorderLayout.CENTER);
		infopanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel idlab = new JLabel("ID :");
		infopanel.add(idlab);
		idtf = new JTextField();
		idtf.setText("00000");
		infopanel.add(idtf);
		idtf.setHorizontalAlignment(SwingConstants.CENTER);
		idtf.setColumns(10);
		contentPane.add(titlepanel, BorderLayout.NORTH);
    }

	public LoginFrame(String title, Controller c) throws SQLException{
		super(title);
		c.connect();
		this.elementi(c);
	}
}*/

public class LoginFrame extends JFrame {
    private Controller c;
    private JPanel contentPane;
    private JButton logbutt;
    private JButton clearbutt;
    private JTextField idtf;
    private Image backgroundImage;

    public void elementi(Controller c) {
        setBounds(100, 100, 700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));
        
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Immagini/Imm2.png"));
            if (backgroundImage == null) {
                throw new IOException("Image not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Image not found: Immagini/Imm2.png", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JLayeredPane layeredPane = new JLayeredPane();
        contentPane.add(layeredPane, BorderLayout.CENTER);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setBounds(0, 0, 700, 450);
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBounds(0, 0, 700, 450);
        layeredPane.add(mainPanel, JLayeredPane.PALETTE_LAYER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(150, 100, 100, 100));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel titleLabel = new JLabel("Ortofrutta 2000");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlePanel.add(titleLabel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        logbutt = new JButton("Login");
        buttonPanel.add(logbutt);
        logbutt.setVerticalAlignment(SwingConstants.TOP);
        logbutt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(c.verifyid(idtf.getText())) {
                        c.iddip = idtf.getText();
                        c.logtoutente(2);
                        idtf.setText("");
                        JOptionPane.showMessageDialog(contentPane, "Accesso Dipendente");
                    } else if(idtf.getText().equals("00000")) {
                        c.logtoutente(1);
                        idtf.setText("");
                        JOptionPane.showMessageDialog(contentPane, "Accesso Admin");
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Id errato!");
                        idtf.setText("");
                    }
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
                }
            }
        });
        logbutt.setBackground(Color.GREEN);

        clearbutt = new JButton("Clear");
        buttonPanel.add(clearbutt);
        clearbutt.setVerticalAlignment(SwingConstants.BOTTOM);
        clearbutt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idtf.setText("");
            }
        });

        mainPanel.add(infoPanel, BorderLayout.CENTER);
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel idLab = new JLabel("ID :");
        infoPanel.add(idLab);
        idtf = new JTextField();
        idtf.setText("00000");
        infoPanel.add(idtf);
        idtf.setHorizontalAlignment(SwingConstants.CENTER);
        idtf.setColumns(10);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
    }

    public LoginFrame(String title, Controller c) throws SQLException {
        super(title);
        c.connect();
        this.elementi(c);
    }

    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}

