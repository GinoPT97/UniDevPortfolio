package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import Model.Prodotto;

public class NuovoProdottoFrame extends JFrame {
	private JPanel contentPane;
	private JPanel buttonpanel;
	private JButton backbutton;
	private JButton clearbutton;
	private JButton insertbutton;
	private JButton selbutton;
	private JTextField nometf;
	private JTextField provtf;
	private JTextField prezzotf;
	private JTextField racctf;
	private JTextField mungtf;
	private JTextField scadtf;
	private JTextField scortatf;
	private JTextArea descta;
	private JCheckBox glutcb;
	private JComboBox<String> categoriacb;

	public void elementi() {
	    // Configurazione della finestra
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 650, 450);
	    setLocationRelativeTo(null);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(NuovoProdottoFrame.class.getResource("/Immagini/ImmIcon.png")));

	    // Pannello principale
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    contentPane.setLayout(new BorderLayout());
	    setContentPane(contentPane);

	    // Pannello per il contenuto centrale
	    JPanel elempanel = new JPanel();
	    elempanel.setBorder(new EmptyBorder(10, 50, 10, 50));
	    elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));
	    contentPane.add(elempanel, BorderLayout.CENTER);

	    // Nome
	    JPanel nomepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel nomelab = new JLabel("Nome :");
	    nomepanel.add(nomelab);
	    nometf = new JTextField(10);
	    nomepanel.add(nometf);
	    elempanel.add(nomepanel);

	    // Descrizione
	    JPanel descrizionepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel descrlab = new JLabel("Descrizione :");
	    descrizionepanel.add(descrlab);
	    descta = new JTextArea(2, 10);
	    descrizionepanel.add(descta);
	    elempanel.add(descrizionepanel);

	    // Provenienza
	    JPanel provenienzapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel provlab = new JLabel("Provenienza :");
	    provenienzapanel.add(provlab);
	    provtf = new JTextField(10);
	    provenienzapanel.add(provtf);
	    elempanel.add(provenienzapanel);

	    // Prezzo
	    JPanel prezzopanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel prezzolab = new JLabel("Prezzo :");
	    prezzopanel.add(prezzolab);
	    prezzotf = new JTextField(10);
	    prezzopanel.add(prezzotf);
	    elempanel.add(prezzopanel);

	    // Data Raccolta
	    JPanel raccoltapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel racclab = new JLabel("Data Raccolta (YYYY-MM-DD) :");
	    raccoltapanel.add(racclab);
	    racctf = new JTextField(10);
	    racctf.setEditable(false);
	    raccoltapanel.add(racctf);
	    elempanel.add(raccoltapanel);

	    // Data Mungitura
	    JPanel mungiturapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel munglab = new JLabel("Data Mungitura (YYYY-MM-DD) :");
	    mungiturapanel.add(munglab);
	    mungtf = new JTextField(10);
	    mungtf.setEditable(false);
	    mungiturapanel.add(mungtf);
	    elempanel.add(mungiturapanel);

	    // Glutine
	    JPanel glutinepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel glutlab = new JLabel("Glutine :");
	    glutinepanel.add(glutlab);
	    glutcb = new JCheckBox("Si");
	    glutcb.setEnabled(false);
	    glutinepanel.add(glutcb);
	    elempanel.add(glutinepanel);

	    // Data Scadenza
	    JPanel scadenzapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel scadlab = new JLabel("Data Scadenza (YYYY-MM-DD) :");
	    scadenzapanel.add(scadlab);
	    scadtf = new JTextField(10);
	    scadtf.setEditable(false);
	    scadenzapanel.add(scadtf);
	    elempanel.add(scadenzapanel);

	    // Scorta
	    JPanel scortapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    JLabel scortalab = new JLabel("Scorta :");
	    scortapanel.add(scortalab);
	    scortatf = new JTextField(10);
	    scortapanel.add(scortatf);
	    elempanel.add(scortapanel);

	    // Categoria e pulsante di selezione
	    JPanel categoriapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    categoriacb = new JComboBox<>(new String[] { "Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei" });
	    categoriapanel.add(categoriacb);
	    selbutton = new JButton("Selezione");
	    categoriapanel.add(selbutton);
	    elempanel.add(categoriapanel);

	    // Pannello per i pulsanti di azione
	    buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    insertbutton = new JButton("Inserisci");
	    insertbutton.setBackground(Color.GREEN);
	    buttonpanel.add(insertbutton);
	    clearbutton = new JButton("Pulisci");
	    clearbutton.setBackground(Color.WHITE);
	    buttonpanel.add(clearbutton);
	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    buttonpanel.add(backbutton);
	    contentPane.add(buttonpanel, BorderLayout.SOUTH);

	    // Pannello per il titolo
	    JPanel titlepanel = new JPanel();
	    titlepanel.setBackground(new Color(139, 0, 0));
	    JLabel titlelabel = new JLabel("Inserimento nuovo prodotto");
	    titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlepanel.add(titlelabel);
	    contentPane.add(titlepanel, BorderLayout.NORTH);
	}

	public void clean() {
	    nometf.setText("");
	    descta.setText("");
	    prezzotf.setText("");
	    provtf.setText("");
	    scortatf.setText("");
	    racctf.setText("");
	    mungtf.setText("");
	    scadtf.setText("");
	    glutcb.setSelected(false);
	}

	public void azioni(Controller c) {
	    clearbutton.addActionListener(e -> clean());

	    backbutton.addActionListener(e -> {
	        clean();
	        c.visAndElem(4, 3);
	    });

	    insertbutton.addActionListener(e -> {
	        DateFormat data = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            // Verifica che tutti i campi obbligatori siano compilati
	            if (nometf.getText().isEmpty() || descta.getText().isEmpty() ||
	                prezzotf.getText().isEmpty() || provtf.getText().isEmpty() ||
	                scortatf.getText().isEmpty()) {
	                JOptionPane.showMessageDialog(null, "Inserisci tutti i componenti");
	                return;
	            }

	            // Prepara i dati da salvare in base alla categoria selezionata
	            String categoria = categoriacb.getSelectedItem().toString();
	            Prodotto prodotto = new Prodotto(
	                "", nometf.getText(), descta.getText(),
	                Double.parseDouble(prezzotf.getText()), provtf.getText(),
	                categoria.equals("Ortofrutticoli") ? data.parse(racctf.getText()) : null,
	                categoria.equals("Latticini") ? data.parse(mungtf.getText()) : null,
	                glutcb.isSelected(),
	                categoria.equals("Inscatolati") ? data.parse(scadtf.getText()) : null,
	                categoria, Integer.parseInt(scortatf.getText())
	            );

	            // Salva il prodotto nel database
	            c.newprod(prodotto);

	            // Aggiungi il prodotto anche al modello della tabella
	            c.prodModel.addRow(new Object[]{
	                prodotto.getNome(),
	                prodotto.getDescrizione(),
	                prodotto.getPrezzo(),
	                prodotto.getLuogoProv(),
	                prodotto.getCategoria(),
	                prodotto.getScorta()
	            });

	            // Pulisci i campi e mostra un messaggio di successo
	            clean();
	            JOptionPane.showMessageDialog(null, "Aggiunta effettuata");

	        } catch (NumberFormatException | SQLException | ParseException e1) {
	            JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
	        }
	    });

	    selbutton.addActionListener(event -> {
	        // Abilita o disabilita i campi in base alla categoria selezionata
	        int type = categoriacb.getSelectedIndex();
	        racctf.setEditable(type == 0);
	        mungtf.setEditable(type == 2);
	        scadtf.setEditable(type == 1);
	        glutcb.setEnabled(type == 3);
	    });

	    descta.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyTyped(KeyEvent e) {
	            if (descta.getText().length() >= 500) {
	                e.consume();
	            }
	        }
	    });
	}

	public NuovoProdottoFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
