package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
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

import Model.Cliente;

public class ModificaClienteFrame extends JFrame {
	private Controller c;
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

	public void clean() {
		nometf.setText("");
		cognometf.setText("");
		codfisctf.setText("");
		indirizzotf.setText("");
		emailtf.setText("");
		telefonotf.setText("");
	}

	public void viewct(Cliente ce) {
		cod = ce.getCodCl();
		nometf.setText(ce.getNome());
		cognometf.setText(ce.getCognome());
		codfisctf.setText(ce.getCodFis());
		indirizzotf.setText(ce.getInd());
		emailtf.setText(ce.getEmail());
		telefonotf.setText(ce.getTel());
	}

	public void azioni(Controller c) {
		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clean();
				c.visAndcl(3);
			}
		});

		addbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					c.upcliente(new Cliente(cod, nometf.getText(), cognometf.getText(), codfisctf.getText(), emailtf.getText(), indirizzotf.getText(), telefonotf.getText(),null,null));
					clean();
					c.visAndcl(3);
					JOptionPane.showMessageDialog(null, "Cliente modificato");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
				}
			}
		});

		clearbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clean();
			}
		});
	}

	public void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));

		buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		addbutton = new JButton("Inserisci");
		addbutton.setBackground(Color.BLUE);
		buttonpanel.add(addbutton);

		clearbutton = new JButton("Pulisci");
		buttonpanel.add(clearbutton);

		backbutton = new JButton("Indietro");
		buttonpanel.add(backbutton);
		backbutton.setBackground(Color.RED);

		elempanel = new JPanel();
		elempanel.setBorder(new EmptyBorder(100, 100, 100, 100));
		contentPane.add(elempanel);
		elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));

		nomepanel = new JPanel();
		elempanel.add(nomepanel);

		nomelab = new JLabel("Nome :");
		nomepanel.add(nomelab);

		nometf = new JTextField();
		nometf.setColumns(10);
		nomepanel.add(nometf);

		cognomepanel = new JPanel();
		elempanel.add(cognomepanel);

		cognomelab = new JLabel("Cognome :");
		cognomepanel.add(cognomelab);

		cognometf = new JTextField();
		cognometf.setColumns(10);
		cognomepanel.add(cognometf);

		codfiscpanel = new JPanel();
		elempanel.add(codfiscpanel);

		codfisclab = new JLabel("Codice Fiscale :");
		codfiscpanel.add(codfisclab);

		codfisctf = new JTextField();
		codfisctf.setColumns(10);
		codfiscpanel.add(codfisctf);

		emailpanel = new JPanel();
		elempanel.add(emailpanel);

		emaillab = new JLabel("Email :");
		emailpanel.add(emaillab);

		emailtf = new JTextField();
		emailtf.setColumns(10);
		emailpanel.add(emailtf);

		indirizzopanel = new JPanel();
		elempanel.add(indirizzopanel);

		indirizzolab = new JLabel(" Indirizzo : ");
		indirizzopanel.add(indirizzolab);

		indirizzotf = new JTextField();
		indirizzotf.setColumns(10);
		indirizzopanel.add(indirizzotf);

		telefonopanel = new JPanel();
		elempanel.add(telefonopanel);

		lblTelefono = new JLabel("Telefono :       +39");
		telefonopanel.add(lblTelefono);

		telefonotf = new JTextField();
		telefonotf.setColumns(10);
		telefonopanel.add(telefonotf);

		titlepanel = new JPanel();
		titlepanel.setBackground(new Color(107, 142, 35));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		titlelabel = new JLabel("Modifica Cliente");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelabel);
	}

	public ModificaClienteFrame(String title,Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}