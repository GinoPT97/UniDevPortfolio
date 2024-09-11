package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class AdminFrame extends JFrame {
	private JPanel contentPane;
	private JButton logoutbutton;
	private JButton statistichebutton;
	private JButton dipbutton;
	private JButton visordbutt;
	private JButton prodbutton;
	private JPanel titlepanel;
	private JLabel titlelabel;
	private JButton searchbutton;

	public void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setIconImage(Toolkit.getDefaultToolkit().getImage(AdminFrame.class.getResource("/Immagini/ImmIcon.png")));
		setLocationRelativeTo(null);

		JPanel buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.CENTER); // Modificato per centrare i bottoni
		buttonpanel.setLayout(new GridBagLayout()); // Usa GridBagLayout per centrare i bottoni
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(10, 0, 10, 0); // Spaziatura tra i bottoni
		gbc.anchor = GridBagConstraints.CENTER;

		dipbutton = new JButton("Dipendenti");
		buttonpanel.add(dipbutton, gbc);

		statistichebutton = new JButton("Statistiche");
		buttonpanel.add(statistichebutton, gbc);

		searchbutton = new JButton("Ricerca Clienti");
		buttonpanel.add(searchbutton, gbc);

		prodbutton = new JButton("Prodotti");
		buttonpanel.add(prodbutton, gbc);

		visordbutt = new JButton("Ordini");
		buttonpanel.add(visordbutt, gbc);

		titlepanel = new JPanel();
		titlepanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		titlepanel.setBackground(new Color(30, 144, 255));
		contentPane.add(titlepanel, BorderLayout.WEST);
		titlepanel.setLayout(new BorderLayout(0, 0));

		titlelabel = new JLabel("Admin Area");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelabel);

		logoutbutton = new JButton("Logout");
		titlepanel.add(logoutbutton, BorderLayout.SOUTH);
		logoutbutton.setVerticalAlignment(SwingConstants.BOTTOM);
		logoutbutton.setBackground(Color.RED);
	}

	public void azioni(Controller c) {
		logoutbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.logout(1);
			}
		});

		dipbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.adminAndElem(1);
			}
		});

		prodbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.adminAndElem(2);
			}
		});

		statistichebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.adminAndElem(3);
			}
		});

		visordbutt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.adminAndElem(4);
			}
		});

		searchbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.searchAndElem(1);
			}
		});
	}

	public AdminFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}



