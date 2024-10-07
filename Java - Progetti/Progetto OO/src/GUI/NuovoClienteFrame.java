package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import Model.Cliente;

public class NuovoClienteFrame extends JFrame {
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
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 700, 500);
	    setLocationRelativeTo(null);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

	    // Impostazione del contenuto e del layout principale
	    contentPane = new JPanel(new BorderLayout(0, 0));
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    setContentPane(contentPane);

	    // Pannello per i pulsanti
	    JPanel buttonpanel = new JPanel();
	    buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    contentPane.add(buttonpanel, BorderLayout.SOUTH);

	    addbutton = new JButton("Aggiungi");
	    addbutton.setBackground(Color.GREEN);
	    buttonpanel.add(addbutton);

	    clearbutton = new JButton("Pulisci");
	    buttonpanel.add(clearbutton);

	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    buttonpanel.add(backbutton);

	    // Pannello principale per i campi di inserimento
	    JPanel elempanel = new JPanel();
	    elempanel.setBorder(new EmptyBorder(20, 40, 20, 40));
	    elempanel.setLayout(new GridLayout(7, 2, 10, 10)); // 7 righe e 2 colonne per i campi di input
	    contentPane.add(elempanel, BorderLayout.CENTER);

	    // Creazione e aggiunta dei componenti al pannello dei campi di inserimento
	    JLabel nomelab = new JLabel("Nome:");
	    elempanel.add(nomelab);
	    nometf = new JTextField();
	    nometf.setColumns(20);
	    elempanel.add(nometf);

	    JLabel cognomelab = new JLabel("Cognome:");
	    elempanel.add(cognomelab);
	    cognometf = new JTextField();
	    cognometf.setColumns(20);
	    elempanel.add(cognometf);

	    JLabel codfisclab = new JLabel("Codice Fiscale:");
	    elempanel.add(codfisclab);
	    codfisctf = new JTextField();
	    codfisctf.setColumns(20);
	    elempanel.add(codfisctf);

	    JLabel emaillab = new JLabel("Email:");
	    elempanel.add(emaillab);
	    emailtf = new JTextField();
	    emailtf.setColumns(20);
	    elempanel.add(emailtf);

	    JLabel indirizzolab = new JLabel("Indirizzo:");
	    elempanel.add(indirizzolab);
	    indirizzotf = new JTextField();
	    indirizzotf.setColumns(20);
	    elempanel.add(indirizzotf);

	    JLabel telefonolab = new JLabel("Telefono: +39");
	    elempanel.add(telefonolab);
	    telefonotf = new JTextField();
	    telefonotf.setColumns(20);
	    elempanel.add(telefonotf);

	    JLabel tesseralab = new JLabel("La relativa tessera verrà creata in automatico");
	    elempanel.add(tesseralab);

	    // Pannello del titolo
	    JPanel titlepanel = new JPanel();
	    titlepanel.setBackground(new Color(85, 107, 47));
	    contentPane.add(titlepanel, BorderLayout.NORTH);

	    JLabel titlelabel = new JLabel("Inserimento Nuovo Utente");
	    titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlepanel.add(titlelabel);
	}


    public void clean() {
        // Pulizia dei campi di testo
        nometf.setText("");
        cognometf.setText("");
        codfisctf.setText("");
        indirizzotf.setText("");
        emailtf.setText("");
        telefonotf.setText("");
    }

    public void azioni(Controller c) {
        // Azione per il bottone "Indietro"
        backbutton.addActionListener(e -> {
            clean();
            c.visAndcl(3);
        });

        // Azione per il bottone "Aggiungi"
        addbutton.addActionListener(e -> {
            try {
                // Creazione del nuovo cliente e tessera
                Cliente newCliente = new Cliente("", nometf.getText(), cognometf.getText(), codfisctf.getText(),
                        emailtf.getText(), indirizzotf.getText(), telefonotf.getText(), null, null);
                c.newclt(newCliente);
                c.nuovatessera(nometf.getText(), cognometf.getText(), codfisctf.getText());
                clean();
                JOptionPane.showMessageDialog(null, "Cliente e relativa tessera aggiunti");
            } catch (SQLException e1) {
                // Gestione dell'errore
                JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
            }
        });

        // Azione per il bottone "Pulisci"
        clearbutton.addActionListener(e -> clean());
    }

	public NuovoClienteFrame(String title, Controller c) {
		super(title);
		setIconImage(Toolkit.getDefaultToolkit().getImage(NuovoClienteFrame.class.getResource("/Immagini/ImmIcon.png")));
		this.elementi();
		this.azioni(c);
	}
}
