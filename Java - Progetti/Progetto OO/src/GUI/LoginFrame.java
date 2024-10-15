package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

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

	public void elementi() {
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
		logbutt.setBackground(Color.GREEN);

		clearbutt = new JButton("Clear");
		buttonpanel.add(clearbutt);
		clearbutt.setVerticalAlignment(SwingConstants.BOTTOM);

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

	// Metodo azioni per gestire tutti gli eventi dei bottoni
	public void azioni(Controller c) {
		// Azione per il bottone "Login"
		logbutt.addActionListener(e -> {
			try {
				if (c.verifyid(idtf.getText())) {
					c.iddip = idtf.getText();
					c.logtoutente(2);
					idtf.setText("");
					JOptionPane.showMessageDialog(contentPane, "Accesso Dipendente");
				} else if (idtf.getText().equals("00000")) {
					c.iddip = idtf.getText();
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
		});

		// Azione per il bottone "Clear"
		clearbutt.addActionListener(e -> idtf.setText(""));

		// Aggiunta del KeyListener per rilevare il tasto Invio sulla JTextField
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
		this.elementi();
		this.azioni(c);
	}
}
