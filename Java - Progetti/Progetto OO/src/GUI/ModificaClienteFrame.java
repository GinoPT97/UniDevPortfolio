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

import Model.Cliente;

public class ModificaClienteFrame extends JFrame {
	private JPanel contentPane;
	private String cod;
	private JPanel buttonpanel;
	private JButton backbutton;
	private JButton clearbutton;
	private JButton addbutton;
	private JPanel elempanel;
	private JPanel nomepanel;
	private JTextField nometf;
	private JLabel nomelab;
	private JPanel cognomepanel;
	private JTextField cognometf;
	private JLabel cognomelab;
	private JPanel codfiscpanel;
	private JLabel codfisclab;
	private JTextField codfisctf;
	private JPanel emailpanel;
	private JTextField emailtf;
	private JLabel emaillab;
	private JPanel indirizzopanel;
	private JTextField indirizzotf;
	private JLabel indirizzolab;
	private JPanel telefonopanel;
	private JLabel lblTelefono;
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
	    titlepanel.setBackground(new Color(107, 142, 35));
	    titlelabel = new JLabel("Modifica Cliente");
	    titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlelabel.setForeground(Color.WHITE); // Colore del testo bianco per contrasto
	    titlepanel.add(titlelabel);
	    contentPane.add(titlepanel, BorderLayout.NORTH);

	    // Pannello per i bottoni
	    buttonpanel = new JPanel();
	    buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Layout centrato con spaziatura
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
	    elempanel.setBorder(new EmptyBorder(20, 50, 20, 50)); // Padding migliorato
	    elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS)); // Layout verticale per gli input
	    contentPane.add(elempanel, BorderLayout.CENTER);

	    // Nome
	    nomepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    nomelab = new JLabel("Nome :");
	    nomepanel.add(nomelab);
	    nometf = new JTextField(10);
	    nomepanel.add(nometf);
	    elempanel.add(nomepanel);

	    // Cognome
	    cognomepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    cognomelab = new JLabel("Cognome :");
	    cognomepanel.add(cognomelab);
	    cognometf = new JTextField(10);
	    cognomepanel.add(cognometf);
	    elempanel.add(cognomepanel);

	    // Codice Fiscale
	    codfiscpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    codfisclab = new JLabel("Codice Fiscale :");
	    codfiscpanel.add(codfisclab);
	    codfisctf = new JTextField(10);
	    codfiscpanel.add(codfisctf);
	    elempanel.add(codfiscpanel);

	    // Email
	    emailpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    emaillab = new JLabel("Email :");
	    emailpanel.add(emaillab);
	    emailtf = new JTextField(10);
	    emailpanel.add(emailtf);
	    elempanel.add(emailpanel);

	    // Indirizzo
	    indirizzopanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Allineamento centrato
	    indirizzolab = new JLabel("Indirizzo :");
	    indirizzopanel.add(indirizzolab);
	    indirizzotf = new JTextField(10);
	    indirizzopanel.add(indirizzotf);
	    elempanel.add(indirizzopanel);

	    // Telefono
	    telefonopanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Allineamento centrato
	    lblTelefono = new JLabel("Telefono : +39");
	    telefonopanel.add(lblTelefono);
	    telefonotf = new JTextField(10);
	    telefonopanel.add(telefonotf);
	    elempanel.add(telefonopanel);
	}

	// Metodo per pulire i campi di input
	public void clean() {
	    nometf.setText("");
	    cognometf.setText("");
	    codfisctf.setText("");
	    indirizzotf.setText("");
	    emailtf.setText("");
	    telefonotf.setText("");
	}

	// Metodo per visualizzare i dati di un cliente nei rispettivi campi
	public void viewct(Cliente ce) {
	    cod = ce.getCodCl();
	    nometf.setText(ce.getNome());
	    cognometf.setText(ce.getCognome());
	    codfisctf.setText(ce.getCodFis());
	    indirizzotf.setText(ce.getInd());
	    emailtf.setText(ce.getEmail());
	    telefonotf.setText(ce.getTel());
	}

	// Metodo per aggiungere le azioni ai bottoni
	public void azioni(Controller c) {
	    // Bottone per tornare indietro
	    backbutton.addActionListener(e -> {
	        clean();
	        c.visAndElem(3, 3); // Torna alla vista con indice 3
	    });

	    // Bottone per aggiornare un cliente
	    addbutton.addActionListener(e -> {
	        try {
	            // Aggiorna il cliente con i dati inseriti nei JTextField
	            Cliente clienteAggiornato = new Cliente(
	                cod,
	                nometf.getText(),
	                cognometf.getText(),
	                codfisctf.getText(),
	                emailtf.getText(),
	                indirizzotf.getText(),
	                telefonotf.getText(),
	                null,
	                null
	            );

	            // Aggiorna il cliente nel database
	            c.upcliente(clienteAggiornato);

	            // Aggiorna la riga corrispondente nel modello
	            for (int i = 0; i < c.clienteModel.getRowCount(); i++) {
	                if (c.clienteModel.getValueAt(i, 0).equals(clienteAggiornato.getCodCl())) { // Assumendo che il codice cliente sia il primo elemento
	                    c.clienteModel.setValueAt(clienteAggiornato.getNome(), i, 1);
	                    c.clienteModel.setValueAt(clienteAggiornato.getCognome(), i, 2);
	                    c.clienteModel.setValueAt(clienteAggiornato.getCodFis(), i, 3);
	                    c.clienteModel.setValueAt(clienteAggiornato.getEmail(), i, 4);
	                    c.clienteModel.setValueAt(clienteAggiornato.getInd(), i, 5);
	                    c.clienteModel.setValueAt(clienteAggiornato.getTel(), i, 6);
	                    break; // Esci dal ciclo dopo aver trovato e aggiornato la riga
	                }
	            }

	            clean(); // Pulisce i campi dopo l'aggiornamento
	            c.visAndElem(3, 3); // Torna alla vista con indice 3
	            JOptionPane.showMessageDialog(null, "Cliente modificato");
	        } catch (SQLException e1) {
	            JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1.getMessage());
	        }
	    });

	    // Bottone per pulire i campi
	    clearbutton.addActionListener(e -> clean());
	}

	public ModificaClienteFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}

