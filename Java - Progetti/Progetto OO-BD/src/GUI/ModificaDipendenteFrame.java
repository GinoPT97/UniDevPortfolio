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
		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clean();
				c.visAnddip(3);
			}
		});

		addbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					c.updip(new Dipendente(cod, nometf.getText(), cognometf.getText(), codfisctf.getText(), emailtf.getText(), indirizzotf.getText(), telefonotf.getText()));
					clean();
					c.visAnddip(3);
					JOptionPane.showMessageDialog(null, "Dipendente modificato");
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
		setIconImage(Toolkit.getDefaultToolkit().getImage(DipendenteFrame.class.getResource("/Immagini/ImmIcon.png")));
		setLocationRelativeTo(null);

		buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		addbutton = new JButton("Inserisci");
		addbutton.setBackground(Color.BLUE);
		buttonpanel.add(addbutton);

		clearbutton = new JButton("Pulisci");
		buttonpanel.add(clearbutton);

		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		buttonpanel.add(backbutton);

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

		telefonolab = new JLabel("Telefono :         +39");
		telefonopanel.add(telefonolab);

		telefonotf = new JTextField();
		telefonotf.setColumns(10);
		telefonopanel.add(telefonotf);

		titlepanel = new JPanel();
		titlepanel.setBackground(Color.ORANGE);
		contentPane.add(titlepanel, BorderLayout.NORTH);

		titlelabel = new JLabel("Modifica Dipendente");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelabel);
	}

	public ModificaDipendenteFrame(String title,Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}