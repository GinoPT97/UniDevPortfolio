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
import javax.swing.SwingConstants;
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
	private JPanel buttonpanel;
	private JPanel buttonContainer;

	public void elementi() {
	    // Impostazioni di base del frame
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 700, 450);
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    setContentPane(contentPane);
	    contentPane.setLayout(new BorderLayout(0, 0));
	    setLocationRelativeTo(null);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));

	    // Pannello del titolo con sfondo arancione
	    JPanel titlepanel = new JPanel();
	    titlepanel.setBackground(Color.ORANGE);
	    contentPane.add(titlepanel, BorderLayout.WEST);
	    titlepanel.setLayout(new BorderLayout(0, 0)); // Usa BorderLayout per estendere il bottone di logout

	    // Centra verticalmente il titolo
	    titlelab = new JLabel("Area Dipendenti");
	    titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlelab.setHorizontalAlignment(SwingConstants.CENTER); // Allinea il titolo orizzontalmente al centro
	    titlepanel.add(titlelab, BorderLayout.CENTER);

	    // Bottone di logout esteso orizzontalmente
	    logoututton = new JButton("Logout");
	    logoututton.setBackground(Color.RED);
	    logoututton.setForeground(Color.WHITE); // Testo bianco per contrasto
	    titlepanel.add(logoututton, BorderLayout.SOUTH); // Posizionato in basso ed esteso orizzontalmente

	    // Pannello per i bottoni centrati
	    buttonpanel = new JPanel();
	    contentPane.add(buttonpanel, BorderLayout.CENTER);
	    buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS)); // Layout verticale per centrare i bottoni

	    // Pannello contenitore per i bottoni
	    buttonContainer = new JPanel();
	    buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Layout per centrare i bottoni
	    buttonpanel.add(Box.createVerticalGlue()); // Aggiunge spazio sopra i bottoni
	    buttonpanel.add(buttonContainer); // Aggiunge il contenitore dei bottoni
	    buttonpanel.add(Box.createVerticalGlue()); // Aggiunge spazio sotto i bottoni

	    // Creazione e aggiunta dei bottoni al contenitore
	    clientebutton = new JButton("Clienti");
	    buttonContainer.add(clientebutton);

	    searchbutton = new JButton("Ricerca Clienti");
	    buttonContainer.add(searchbutton);

	    tesserabutton = new JButton("Tessera Punti");
	    buttonContainer.add(tesserabutton);

	    ordineutton = new JButton("Ordine");
	    buttonContainer.add(ordineutton);
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
			}
		});
	}

	public DipendenteFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
