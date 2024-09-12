package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
        // Impostazioni di base del frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage(AdminFrame.class.getResource("/Immagini/ImmIcon.png")));
        setLocationRelativeTo(null);

        // Pannello per i bottoni centrati
        JPanel buttonpanel = new JPanel();
        contentPane.add(buttonpanel, BorderLayout.CENTER); 
        buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS));

        // Centrare i bottoni utilizzando un pannello per aggiungere spazio
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); 
        buttonpanel.add(Box.createVerticalGlue()); 
        buttonpanel.add(buttonContainer); 
        buttonpanel.add(Box.createVerticalGlue()); 

        // Creazione e aggiunta dei bottoni al contenitore
        dipbutton = new JButton("Dipendenti");
        buttonContainer.add(dipbutton);

        statistichebutton = new JButton("Statistiche");
        buttonContainer.add(statistichebutton);

        searchbutton = new JButton("Ricerca Clienti");
        buttonContainer.add(searchbutton);

        prodbutton = new JButton("Prodotti");
        buttonContainer.add(prodbutton);

        visordbutt = new JButton("Ordini");
        buttonContainer.add(visordbutt);

        // Pannello del titolo a sinistra
        titlepanel = new JPanel();
        titlepanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        titlepanel.setBackground(new Color(30, 144, 255)); // Impostazione del colore di sfondo
        contentPane.add(titlepanel, BorderLayout.WEST);
        titlepanel.setLayout(new BorderLayout(0, 0));

        // Etichetta del titolo
        titlelabel = new JLabel("Admin Area");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlepanel.add(titlelabel, BorderLayout.NORTH);

        // Bottone di logout nella parte inferiore del pannello
        logoutbutton = new JButton("Logout");
        logoutbutton.setBackground(Color.RED);
        logoutbutton.setForeground(Color.WHITE); // Testo in bianco per contrasto
        titlepanel.add(logoutbutton, BorderLayout.SOUTH);
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



