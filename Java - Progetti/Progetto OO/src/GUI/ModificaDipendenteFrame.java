package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import Model.Dipendente;

public class ModificaDipendenteFrame extends JFrame {
	private Controller c;
	private JPanel contentPane;
	private String cod;
	private JPanel buttonpanel;
	private JPanel elempanel;
	private JButton backbutton;
	private JButton clearbutton;
	private JButton addbutton;
	private JPanel nomepanel;
	private JLabel nomelab;
	private JTextField nometf;
	private JPanel cognomepanel;
	private JLabel cognomelab;
	private JTextField cognometf;
	private JPanel codfiscpanel;
	private JTextField codfisctf;
	private JLabel codfisclab;
	private JPanel emailpanel;
	private JLabel emaillab;
	private JTextField emailtf;
	private JPanel indirizzopanel;
	private JLabel indirizzolab;
	private JTextField indirizzotf;
	private JPanel telefonopanel;
	private JLabel telefonolab;
	private JTextField telefonotf;
	private JPanel titlepanel;
	private JLabel titlelabel;

	public void elementi() {
	    // Impostazioni di base del frame
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 700, 500);
	    setLocationRelativeTo(null);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));

	    // Pannello principale
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    contentPane.setLayout(new BorderLayout(0, 0));
	    setContentPane(contentPane);

	    // Pannello del titolo
	    titlepanel = new JPanel();
	    titlepanel.setBackground(Color.ORANGE);
	    titlelabel = new JLabel("Modifica Dipendente");
	    titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlepanel.add(titlelabel);
	    contentPane.add(titlepanel, BorderLayout.NORTH);

	    // Pannello per i bottoni
	    buttonpanel = new JPanel();
	    buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centrato
	    contentPane.add(buttonpanel, BorderLayout.SOUTH);

	    // Bottone Inserisci
	    addbutton = new JButton("Inserisci");
	    addbutton.setBackground(Color.BLUE);
	    addbutton.setForeground(Color.WHITE); // Colore del testo bianco per contrasto
	    buttonpanel.add(addbutton);

	    // Bottone Pulisci
	    clearbutton = new JButton("Pulisci");
	    buttonpanel.add(clearbutton);

	    // Bottone Indietro
	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    backbutton.setForeground(Color.WHITE); // Colore del testo bianco per contrasto
	    buttonpanel.add(backbutton);

	    // Pannello per gli elementi di input
	    elempanel = new JPanel();
	    elempanel.setBorder(new EmptyBorder(20, 50, 20, 50)); // Migliora il padding
	    elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS)); // Layout verticale per gli input
	    contentPane.add(elempanel, BorderLayout.CENTER);

	    // Nome
	    nomepanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrato
	    nomelab = new JLabel("Nome :");
	    nomepanel.add(nomelab);
	    nometf = new JTextField(10);
	    nomepanel.add(nometf);
	    elempanel.add(nomepanel);

	    // Cognome
	    cognomepanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrato
	    cognomelab = new JLabel("Cognome :");
	    cognomepanel.add(cognomelab);
	    cognometf = new JTextField(10);
	    cognomepanel.add(cognometf);
	    elempanel.add(cognomepanel);

	    // Codice Fiscale
	    codfiscpanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrato
	    codfisclab = new JLabel("Codice Fiscale :");
	    codfiscpanel.add(codfisclab);
	    codfisctf = new JTextField(10);
	    codfiscpanel.add(codfisctf);
	    elempanel.add(codfiscpanel);

	    // Email
	    emailpanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrato
	    emaillab = new JLabel("Email :");
	    emailpanel.add(emaillab);
	    emailtf = new JTextField(10);
	    emailpanel.add(emailtf);
	    elempanel.add(emailpanel);

	    // Indirizzo
	    indirizzopanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrato
	    indirizzolab = new JLabel("Indirizzo :");
	    indirizzopanel.add(indirizzolab);
	    indirizzotf = new JTextField(10);
	    indirizzopanel.add(indirizzotf);
	    elempanel.add(indirizzopanel);

	    // Telefono
	    telefonopanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrato
	    telefonolab = new JLabel("Telefono : +39");
	    telefonopanel.add(telefonolab);
	    telefonotf = new JTextField(10);
	    telefonopanel.add(telefonotf);
	    elempanel.add(telefonopanel);
	}
	
	public void clean() {
	    nometf.setText("");
	    cognometf.setText("");
	    codfisctf.setText("");
	    indirizzotf.setText("");
	    emailtf.setText("");
	    telefonotf.setText("");
	}

	public void viewdip(Dipendente de) {
	    cod = de.getCodDIP();
	    nometf.setText(de.getNome());
	    cognometf.setText(de.getCognome());
	    codfisctf.setText(de.getCodFis());
	    indirizzotf.setText(de.getInd());
	    emailtf.setText(de.getEmail());
	    telefonotf.setText(de.getTel());
	}

	public void azioni(Controller c) {
	    backbutton.addActionListener(e -> {
	        clean();
	        c.visAnddip(3);
	    });

	    addbutton.addActionListener(e -> {
	        try {
	            Dipendente dipendente = new Dipendente(
	                cod, 
	                nometf.getText(), 
	                cognometf.getText(), 
	                codfisctf.getText(),
	                emailtf.getText(), 
	                indirizzotf.getText(), 
	                telefonotf.getText()
	            );
	            c.updip(dipendente);
	            clean();
	            c.visAnddip(3);
	            JOptionPane.showMessageDialog(this, "Dipendente modificato", "Successo", JOptionPane.INFORMATION_MESSAGE);
	        } catch (SQLException e1) {
	            JOptionPane.showMessageDialog(this, "Errore!" + "\n" + "Tipo di errore: " + e1, "Errore", JOptionPane.ERROR_MESSAGE);
	        }
	    });

	    clearbutton.addActionListener(e -> clean());
	}

	public ModificaDipendenteFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}