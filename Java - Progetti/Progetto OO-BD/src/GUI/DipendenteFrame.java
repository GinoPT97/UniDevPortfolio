package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class DipendenteFrame extends JFrame {
	private Controller c;
	private JPanel contentPane;
	private JButton logoututton;
	private JButton clientebutton;
	private JButton ordineutton;
	private JButton tesserabutton;
	private JLabel titlelab;
	private JButton searchbutton;

	public void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));

		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(Color.ORANGE);
		contentPane.add(titlepanel, BorderLayout.WEST);
		titlepanel.setLayout(new BorderLayout(0, 0));

		titlelab = new JLabel("Area Dipendenti");
		titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelab);

		logoututton = new JButton("Logout");
		titlepanel.add(logoututton, BorderLayout.SOUTH);
		logoututton.setBackground(Color.RED);

		JPanel buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.EAST);
		buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 180));

		clientebutton = new JButton("Clienti");
		buttonpanel.add(clientebutton);

		searchbutton = new JButton("Ricerca Clienti");
		buttonpanel.add(searchbutton);

		tesserabutton = new JButton("Tessera Punti");
		buttonpanel.add(tesserabutton);

		ordineutton = new JButton("Ordine");
		buttonpanel.add(ordineutton);
	}

	public void azioni(Controller c) {
		logoututton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.logout(2);
			}
		});

		clientebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.dipAndElem(1);
			}
		});

		tesserabutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.dipAndElem(2);
			}
		});

		ordineutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.dipAndElem(3);
			}
		});

		searchbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.searchAndElem(1);
				c.searchf.x = 3;
			}
		});
	}

	public DipendenteFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
