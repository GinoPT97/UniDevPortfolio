package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
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

    public AdminFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
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

	public void elementi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage(AdminFrame.class.getResource("/Immagini/ImmIcon.png")));
        setLocationRelativeTo(null);

        // Pannello per i bottoni
        JPanel buttonpanel = new JPanel();
        contentPane.add(buttonpanel, BorderLayout.CENTER);
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS)); // Disposizione verticale

        buttonpanel.add(Box.createVerticalGlue()); // Spazio prima dei bottoni

        dipbutton = new JButton("Dipendenti");
        dipbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(dipbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        statistichebutton = new JButton("Statistiche");
        statistichebutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(statistichebutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        searchbutton = new JButton("Ricerca Clienti");
        searchbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(searchbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        prodbutton = new JButton("Prodotti");
        prodbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(prodbutton);
        buttonpanel.add(Box.createVerticalStrut(10));

        visordbutt = new JButton("Ordini");
        visordbutt.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonpanel.add(visordbutt);

        buttonpanel.add(Box.createVerticalGlue()); // Spazio dopo i bottoni

        // Pannello del titolo
        titlepanel = new JPanel();
        titlepanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        titlepanel.setBackground(new Color(30, 144, 255));
        contentPane.add(titlepanel, BorderLayout.WEST);
        titlepanel.setLayout(new BorderLayout(0, 0));

        // Etichetta del titolo
        titlelabel = new JLabel("Admin Area");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlelabel.setForeground(Color.BLACK);
        titlelabel.setHorizontalAlignment(SwingConstants.CENTER); // Allineamento orizzontale al centro
        titlepanel.add(titlelabel, BorderLayout.CENTER);

        // Bottone di logout che occupa l'intera larghezza del pannello
        logoutbutton = new JButton("Logout");
        logoutbutton.setBackground(Color.RED);
        logoutbutton.setForeground(Color.WHITE);
        titlepanel.add(logoutbutton, BorderLayout.SOUTH); // Estende il bottone di logout a tutta la larghezza
    }
}



