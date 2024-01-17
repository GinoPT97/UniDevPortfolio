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
import javax.swing.border.EmptyBorder;

import Entita.Cliente;

public class NuovoClienteFrame extends JFrame {
	private Controller c;
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
	
	public void clean() {
		nometf.setText("");
		cognometf.setText("");
		codfisctf.setText("");
		indirizzotf.setText("");
		emailtf.setText("");
		telefonotf.setText("");
	}

	public void azioni(Controller c) {
		backbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clean();
				c.visAndcl(3);
			}
		});

		addbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
						c.newclt(new Cliente("", nometf.getText(), cognometf.getText(), codfisctf.getText(), emailtf.getText(), indirizzotf.getText(), telefonotf.getText(),null,null));
						c.nuovatessera(nometf.getText(), cognometf.getText(), codfisctf.getText());
						clean();
						JOptionPane.showMessageDialog(null, "Cliente e relativa tessera aggiunti");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
				}
			}
		});

		clearbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clean();
			}
		});
	}
	
	public void elementi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
		
		JPanel buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);
		
		addbutton = new JButton("Aggiungi");
		addbutton.setBackground(Color.GREEN);
		buttonpanel.add(addbutton);
		
		clearbutton = new JButton("Pulisci");
		buttonpanel.add(clearbutton);
		
		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		buttonpanel.add(backbutton);
		
		JPanel elempanel = new JPanel();
		elempanel.setBorder(new EmptyBorder(100, 100, 100, 100));
		contentPane.add(elempanel);
		elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
		
		JPanel namepanel = new JPanel();
		elempanel.add(namepanel);
		
		JLabel nomelab = new JLabel("Nome :");
		namepanel.add(nomelab);
		
		nometf = new JTextField();
		nometf.setColumns(10);
		namepanel.add(nometf);
		
		JPanel cognomepanel = new JPanel();
		elempanel.add(cognomepanel);
		
		JLabel cognomelab = new JLabel("Cognome :");
		cognomepanel.add(cognomelab);
		
		cognometf = new JTextField();
		cognometf.setColumns(10);
		cognomepanel.add(cognometf);
		
		JPanel codfiscalepanel = new JPanel();
		elempanel.add(codfiscalepanel);
		
		JLabel codfisclab = new JLabel("Codice Fiscale :");
		codfiscalepanel.add(codfisclab);
		
		codfisctf = new JTextField();
		codfisctf.setColumns(10);
		codfiscalepanel.add(codfisctf);
		
		JPanel emailpanel = new JPanel();
		elempanel.add(emailpanel);
		
		JLabel emaillab = new JLabel("Email :");
		emailpanel.add(emaillab);
		
		emailtf = new JTextField();
		emailtf.setColumns(10);
		emailpanel.add(emailtf);
		
		JPanel addresspanel = new JPanel();
		elempanel.add(addresspanel);
		
		JLabel indirizzolab = new JLabel(" Indirizzo : ");
		addresspanel.add(indirizzolab);
		
		indirizzotf = new JTextField();
		indirizzotf.setColumns(10);
		addresspanel.add(indirizzotf);
		
		JPanel telefonopanel = new JPanel();
		elempanel.add(telefonopanel);
		
		JLabel lblTelefono = new JLabel("Telefono :     +39");
		telefonopanel.add(lblTelefono);
		
		telefonotf = new JTextField();
		telefonotf.setColumns(10);
		telefonopanel.add(telefonotf);
		
		JPanel tesserapanel = new JPanel();
		elempanel.add(tesserapanel);
		
		JLabel tesseralab = new JLabel("La relativa tessera verrà creata in automatico");
		tesserapanel.add(tesseralab);
		
		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(new Color(85, 107, 47));
		contentPane.add(titlepanel, BorderLayout.NORTH);
		
		JLabel titlelabel = new JLabel("Inserimento Nuovo Utente");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelabel);
	}
	
	public NuovoClienteFrame(String title,Controller c) {
		super(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(NuovoClienteFrame.class.getResource("/Immagini/ImmIcon.png")));
		this.elementi();
		this.azioni(c);
	}
}