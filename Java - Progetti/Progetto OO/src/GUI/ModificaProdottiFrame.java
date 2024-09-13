package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ModificaProdottiFrame extends JFrame {
    private Controller c;
    private JPanel contentPane;
    private String cod;  // Variabile globale per il codice prodotto
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
    private JButton backbutton;
    private JButton updatebutton;
    private JButton clearbutton;

    public void elementi() {
        // Impostazioni base della finestra
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

        // Pannello centrale con layout BoxLayout
        JPanel elempanel = new JPanel();
        elempanel.setBorder(new EmptyBorder(20, 100, 20, 100));
        contentPane.add(elempanel, BorderLayout.CENTER);
        elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));

        // Pannelli per input con FlowLayout.CENTER
        JPanel nomepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nomelab = new JLabel("Nome :");
        nomepanel.add(nomelab);
        nometf = new JTextField(10);
        nomepanel.add(nometf);
        elempanel.add(nomepanel);

        JPanel descrizionepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel descrlab = new JLabel("Descrizione :");
        descrizionepanel.add(descrlab);
        descta = new JTextArea(1, 10);
        descrizionepanel.add(descta);
        elempanel.add(descrizionepanel);

        JPanel provenienzapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel provlab = new JLabel("Provenienza :");
        provenienzapanel.add(provlab);
        provtf = new JTextField(10);
        provenienzapanel.add(provtf);
        elempanel.add(provenienzapanel);

        JPanel prezzopanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel prezzolab = new JLabel("Prezzo :");
        prezzopanel.add(prezzolab);
        prezzotf = new JTextField(10);
        prezzopanel.add(prezzotf);
        elempanel.add(prezzopanel);

        JPanel raccoltapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel racclab = new JLabel("Data Raccolta (YYYY-MM-DD) :");
        raccoltapanel.add(racclab);
        racctf = new JTextField(10);
        racctf.setEditable(false);
        raccoltapanel.add(racctf);
        elempanel.add(raccoltapanel);

        JPanel mungiturapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel munglab = new JLabel("Data Mungitura (YYYY-MM-DD) :");
        mungiturapanel.add(munglab);
        mungtf = new JTextField(10);
        mungtf.setEditable(false);
        mungiturapanel.add(mungtf);
        elempanel.add(mungiturapanel);

        JPanel glutinepanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel glutlab = new JLabel("Glutine :");
        glutinepanel.add(glutlab);
        glutcb = new JCheckBox("Si");
        glutcb.setEnabled(false);
        glutinepanel.add(glutcb);
        elempanel.add(glutinepanel);

        JPanel scadenzapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel scadlab = new JLabel("Data Scadenza (YYYY-MM-DD) :");
        scadenzapanel.add(scadlab);
        scadtf = new JTextField(10);
        scadtf.setEditable(false);
        scadenzapanel.add(scadtf);
        elempanel.add(scadenzapanel);

        JPanel scortapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel scortalab = new JLabel("Scorta :");
        scortapanel.add(scortalab);
        scortatf = new JTextField(10);
        scortapanel.add(scortatf);
        elempanel.add(scortapanel);

        // Pannello per categorie
        JPanel categoriapanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        categoriacb = new JComboBox<>(new String[] { "Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei" });
        categoriapanel.add(categoriacb);
        JButton selbutton = new JButton("Seleziona");
        categoriapanel.add(selbutton);
        elempanel.add(categoriapanel);

        // Pannello dei bottoni
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updatebutton = new JButton("Inserisci");
        updatebutton.setBackground(Color.BLUE);
        buttonpanel.add(updatebutton);
        clearbutton = new JButton("Pulisci");
        clearbutton.setBackground(Color.WHITE);
        buttonpanel.add(clearbutton);
        backbutton = new JButton("Indietro");
        backbutton.setBackground(Color.RED);
        buttonpanel.add(backbutton);
        contentPane.add(buttonpanel, BorderLayout.SOUTH);

        // Pannello del titolo
        JPanel panel = new JPanel();
        panel.setBackground(new Color(178, 34, 34));
        contentPane.add(panel, BorderLayout.NORTH);
        JLabel titlelabel = new JLabel("Modifica Prodotto");
        titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        panel.add(titlelabel);
    }
    
	public void viewprod(Prodotto pe) {
	    cod = pe.getCodProd();
	    nometf.setText(pe.getNome());
	    descta.setText(pe.getDescrizione());
	    provtf.setText(pe.getLuogoProv());
	    prezzotf.setText(String.valueOf(pe.getPrezzo()));
	    scortatf.setText(String.valueOf(pe.getScorta()));
	    glutcb.setSelected(pe.isGlutine());

	    switch (pe.getCategoria()) {
	        case "Ortofrutticoli":
	            categoriacb.setSelectedIndex(0);
	            break;
	        case "Inscatolati":
	            categoriacb.setSelectedIndex(1);
	            break;
	        case "Latticini":
	            categoriacb.setSelectedIndex(2);
	            break;
	        case "Farinacei":
	            categoriacb.setSelectedIndex(3);
	            break;
	        default:
	            categoriacb.setSelectedIndex(-1); // O gestione alternativa se la categoria non è riconosciuta
	            break;
	    }
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
	    backbutton.addActionListener(e -> {
	        clean();
	        c.visAndprod(3);
	    });

	    clearbutton.addActionListener(e -> clean());

	    updatebutton.addActionListener(e -> {
	        DateFormat data = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            // Verifica se tutti i campi obbligatori sono compilati
	            if (nometf.getText().isEmpty() || descta.getText().isEmpty() || prezzotf.getText().isEmpty()
	                    || provtf.getText().isEmpty() || scortatf.getText().isEmpty()) {
	                JOptionPane.showMessageDialog(this, "Inserisci tutti i campi obbligatori", "Errore", JOptionPane.WARNING_MESSAGE);
	                return;
	            }

				// Crea il prodotto in base alla categoria selezionata
	            Prodotto prodotto = new Prodotto(
	                cod,
	                nometf.getText(),
	                descta.getText(),
	                Double.parseDouble(prezzotf.getText()),
	                provtf.getText(),
	                "Ortofrutticoli".equals(categoriacb.getSelectedItem().toString()) ? data.parse(racctf.getText()) : null,
	                "Latticini".equals(categoriacb.getSelectedItem().toString()) ? data.parse(mungtf.getText()) : null,
	                glutcb.isSelected(),
	                "Inscatolati".equals(categoriacb.getSelectedItem().toString()) ? data.parse(scadtf.getText()) : null,
	                categoriacb.getSelectedItem().toString(),
	                Integer.parseInt(scortatf.getText())
	            );

	            c.upprod(prodotto);
	            clean();
	            c.visAndprod(3);
	            JOptionPane.showMessageDialog(this, "Prodotto modificato", "Successo", JOptionPane.INFORMATION_MESSAGE);
	        } catch (NumberFormatException | SQLException | ParseException ex) {
	            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	}

	public ModificaProdottiFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}