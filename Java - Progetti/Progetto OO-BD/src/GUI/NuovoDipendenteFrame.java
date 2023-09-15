package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Entita.Dipendente;

import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import java.awt.Toolkit;

public class NuovoDipendenteFrame extends JFrame {
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
				c.visAnddip(3);
			}
		});

		addbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					c.newdip(new Dipendente("", nometf.getText(), cognometf.getText(), codfisctf.getText(), emailtf.getText(), indirizzotf.getText(), telefonotf.getText()));
					clean();
					JOptionPane.showMessageDialog(null, "Dipendente aggiunto");
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
		
		JPanel nomepanel = new JPanel();
		elempanel.add(nomepanel);
		
		JLabel nomelab = new JLabel("Nome :");
		nomepanel.add(nomelab);
		
		nometf = new JTextField();
		nometf.setColumns(10);
		nomepanel.add(nometf);
		
		JPanel cognomepanel = new JPanel();
		elempanel.add(cognomepanel);
		
		JLabel cognomelab = new JLabel("Cognome :");
		cognomepanel.add(cognomelab);
		
		cognometf = new JTextField();
		cognometf.setColumns(10);
		cognomepanel.add(cognometf);
		
		JPanel codfiscpanel = new JPanel();
		elempanel.add(codfiscpanel);
		
		JLabel codfisclab = new JLabel("Codice Fiscale :");
		codfiscpanel.add(codfisclab);
		
		codfisctf = new JTextField();
		codfisctf.setColumns(10);
		codfiscpanel.add(codfisctf);
		
		JPanel emailpanel = new JPanel();
		elempanel.add(emailpanel);
		
		JLabel emaillab = new JLabel("Email :");
		emailpanel.add(emaillab);
		
		emailtf = new JTextField();
		emailtf.setColumns(10);
		emailpanel.add(emailtf);
		
		JPanel indirizzopanel = new JPanel();
		elempanel.add(indirizzopanel);
		
		JLabel indirizzolab = new JLabel(" Indirizzo : ");
		indirizzopanel.add(indirizzolab);
		
		indirizzotf = new JTextField();
		indirizzotf.setColumns(10);
		indirizzopanel.add(indirizzotf);
		
		JPanel telefonopanel = new JPanel();
		elempanel.add(telefonopanel);
		
		JLabel lblTelefono = new JLabel("Telefono :       +39");
		telefonopanel.add(lblTelefono);
		
		telefonotf = new JTextField();
		telefonotf.setColumns(10);
		telefonopanel.add(telefonotf);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.ORANGE);
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel titlelabel = new JLabel("Inserimento Nuovo Dipendente");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		panel.add(titlelabel);
	}
	
	public NuovoDipendenteFrame(String title,Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
