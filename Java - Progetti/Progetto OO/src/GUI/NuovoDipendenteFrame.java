package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class NuovoDipendenteFrame extends JFrame {
	private JPanel contentPane;
	private JTextField nometf;
	private JTextField cognometf;
	private JTextField codfisctf;
	private JTextField emailtf;
	private JTextField indirizzotf;
	private JTextField telefonotf;
	private JButton addbutton;
	private JButton clearbutton;
	private JButton backbutton;
	
	public void elementi() {
	    // Imposta le proprietà di base della finestra
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 700, 500);
	    setLocationRelativeTo(null);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

	    // Imposta il pannello principale e il layout BorderLayout
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    setContentPane(contentPane);
	    contentPane.setLayout(new BorderLayout(0, 0));

	    // Pannello dei pulsanti
	    JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    contentPane.add(buttonpanel, BorderLayout.SOUTH);

	    addbutton = new JButton("Aggiungi");
	    addbutton.setBackground(Color.GREEN);
	    buttonpanel.add(addbutton);

	    clearbutton = new JButton("Pulisci");
	    buttonpanel.add(clearbutton);

	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    buttonpanel.add(backbutton);

	    // Pannello per gli elementi
	    JPanel elempanel = new JPanel();
	    elempanel.setBorder(new EmptyBorder(20, 100, 20, 100));
	    elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
	    contentPane.add(elempanel, BorderLayout.CENTER);

	    // Pannello Nome
	    JPanel nomepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    elempanel.add(nomepanel);
	    JLabel nomelab = new JLabel("Nome :");
	    nomepanel.add(nomelab);
	    nometf = new JTextField(10);
	    nomepanel.add(nometf);

	    // Pannello Cognome
	    JPanel cognomepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    elempanel.add(cognomepanel);
	    JLabel cognomelab = new JLabel("Cognome :");
	    cognomepanel.add(cognomelab);
	    cognometf = new JTextField(10);
	    cognomepanel.add(cognometf);

	    // Pannello Codice Fiscale
	    JPanel codfiscpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    elempanel.add(codfiscpanel);
	    JLabel codfisclab = new JLabel("Codice Fiscale :");
	    codfiscpanel.add(codfisclab);
	    codfisctf = new JTextField(10);
	    codfiscpanel.add(codfisctf);

	    // Pannello Email
	    JPanel emailpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    elempanel.add(emailpanel);
	    JLabel emaillab = new JLabel("Email :");
	    emailpanel.add(emaillab);
	    emailtf = new JTextField(10);
	    emailpanel.add(emailtf);

	    // Pannello Indirizzo
	    JPanel indirizzopanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    elempanel.add(indirizzopanel);
	    JLabel indirizzolab = new JLabel(" Indirizzo : ");
	    indirizzopanel.add(indirizzolab);
	    indirizzotf = new JTextField(10);
	    indirizzopanel.add(indirizzotf);

	    // Pannello Telefono
	    JPanel telefonopanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    elempanel.add(telefonopanel);
	    JLabel lblTelefono = new JLabel("Telefono :       +39");
	    telefonopanel.add(lblTelefono);
	    telefonotf = new JTextField(10);
	    telefonopanel.add(telefonotf);

	    // Pannello Titolo
	    JPanel titlepanel = new JPanel();
	    titlepanel.setBackground(Color.ORANGE);
	    contentPane.add(titlepanel, BorderLayout.NORTH);
	    JLabel titlelabel = new JLabel("Inserimento Nuovo Dipendente");
	    titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlepanel.add(titlelabel);
	}

	public void clean() {
		nometf.setText("");
		cognometf.setText("");
		codfisctf.setText("");
		indirizzotf.setText("");
		emailtf.setText("");
		telefonotf.setText("");
	}

	public void azioni(Controller c) {
	    // Listener per il bottone di aggiunta di un nuovo dipendente
	    addbutton.addActionListener(e -> {
	        try {
	            // Crea un nuovo oggetto Dipendente utilizzando i valori dei campi di testo
	            c.newdip(new Dipendente("", nometf.getText(), cognometf.getText(), codfisctf.getText(),
	                    emailtf.getText(), indirizzotf.getText(), telefonotf.getText()));
	            // Pulisce i campi di input
	            clean();
	            // Mostra un messaggio di successo
	            JOptionPane.showMessageDialog(null, "Dipendente aggiunto");
	        } catch (SQLException e1) {
	            // Mostra un messaggio di errore in caso di eccezione
	            JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
	        }
	    });

	    // Listener per il bottone di pulizia dei campi di input
	    clearbutton.addActionListener(e -> clean());

	    // Listener per il bottone di ritorno alla schermata precedente
	    backbutton.addActionListener(e -> {
	        clean(); // Pulisce i campi di input
	        c.visAnddip(3); // Passa alla schermata dipendenti
	    });
	}

	public NuovoDipendenteFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
