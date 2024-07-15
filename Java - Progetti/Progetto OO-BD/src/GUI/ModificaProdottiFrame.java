package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
	private String cod;
	private JTextField nometf;
	private JTextField provtf;
	private JTextField prezzotf;
	private JTextField racctf;
	private JTextField mungtf;
	private JTextField scadtf;
	private JTextField scortatf;
	private JTextArea descta;
	private JCheckBox glutcb;
	private JComboBox categoriacb;
	private JButton backbutton;
	private JButton updatebutton;
	private JButton clearbutton;

	public void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

		JPanel elempanel = new JPanel();
		elempanel.setBorder(new EmptyBorder(20, 100, 20, 100));
		contentPane.add(elempanel, BorderLayout.CENTER);
		elempanel.setLayout(new BoxLayout(elempanel, BoxLayout.Y_AXIS));

		JPanel nomepanel = new JPanel();
		elempanel.add(nomepanel);

		JLabel nomelab = new JLabel("Nome :");
		nomepanel.add(nomelab);

		nometf = new JTextField();
		nometf.setColumns(10);
		nomepanel.add(nometf);

		JPanel descrizionepanel = new JPanel();
		elempanel.add(descrizionepanel);

		JLabel descrlab = new JLabel("Descrizione :");
		descrizionepanel.add(descrlab);

		descta = new JTextArea();
		descta.setRows(1);
		descta.setColumns(10);
		descrizionepanel.add(descta);

		JPanel provenienzapanel = new JPanel();
		elempanel.add(provenienzapanel);

		JLabel provlab = new JLabel("Provenienza :");
		provenienzapanel.add(provlab);

		provtf = new JTextField();
		provtf.setColumns(10);
		provenienzapanel.add(provtf);

		JPanel prezzopanel = new JPanel();
		elempanel.add(prezzopanel);

		JLabel prezzolab = new JLabel("Prezzo :");
		prezzopanel.add(prezzolab);

		prezzotf = new JTextField();
		prezzotf.setColumns(10);
		prezzopanel.add(prezzotf);

		JPanel raccoltapanel = new JPanel();
		elempanel.add(raccoltapanel);

		JLabel racclab = new JLabel("Data Raccolta (YYYY-MM-DD)  :");
		raccoltapanel.add(racclab);

		racctf = new JTextField();
		racctf.setEditable(false);
		racctf.setColumns(10);
		raccoltapanel.add(racctf);

		JPanel mungiturapanel = new JPanel();
		elempanel.add(mungiturapanel);

		JLabel munglab = new JLabel("Data Mungitura (YYYY-MM-DD)  :");
		mungiturapanel.add(munglab);

		mungtf = new JTextField();
		mungtf.setEditable(false);
		mungtf.setColumns(10);
		mungiturapanel.add(mungtf);

		JPanel glutinepanel = new JPanel();
		elempanel.add(glutinepanel);

		JLabel glutlab = new JLabel("Glutine :");
		glutinepanel.add(glutlab);

		glutcb = new JCheckBox("Si");
		glutcb.setEnabled(false);
		glutinepanel.add(glutcb);

		JPanel scadenzapanel = new JPanel();
		elempanel.add(scadenzapanel);

		JLabel scadlab = new JLabel("Data Scadenza  (YYYY-MM-DD) :");
		scadenzapanel.add(scadlab);

		scadtf = new JTextField();
		scadtf.setEditable(false);
		scadtf.setColumns(10);
		scadenzapanel.add(scadtf);

		JPanel scortapanel = new JPanel();
		elempanel.add(scortapanel);

		JLabel scortalab = new JLabel("Scorta :");
		scortapanel.add(scortalab);

		scortatf = new JTextField();
		scortatf.setColumns(10);
		scortapanel.add(scortatf);

		JPanel categoriapanel = new JPanel();
		elempanel.add(categoriapanel);

		categoriacb = new JComboBox(new String[] {"Ortofrutticoli","Inscatolati","Latticini","Farinacei"});
		categoriapanel.add(categoriacb);

		JButton selbutton = new JButton("Seleziona");
		categoriapanel.add(selbutton);

	    JPanel buttonpanel = new JPanel();
	    contentPane.add(buttonpanel, BorderLayout.SOUTH);

	    updatebutton = new JButton("Inserisci");
	    updatebutton.setBackground(Color.BLUE);
	    buttonpanel.add(updatebutton);

	    clearbutton = new JButton("Pulisci");
	    clearbutton.setBackground(Color.WHITE);
	    buttonpanel.add(clearbutton);

	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    buttonpanel.add(backbutton);

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
		descta.setText(pe.getDscrizione());
		provtf.setText(pe.getLuogoProv());
		prezzotf.setText(String.valueOf(pe.getPrezzo()));
		scortatf.setText(String.valueOf(pe.getScorta()));
		if(pe.getGlutine()) {
			glutcb.setSelected(true);
		}
		if(pe.getCategoria().equals("Ortofrutticoli")) {
			categoriacb.setSelectedIndex(0);
		}
		if(pe.getCategoria().equals("Inscatolati")) {
			categoriacb.setSelectedIndex(1);
		}
		if(pe.getCategoria().equals("Latticini")) {
			categoriacb.setSelectedIndex(2);
		}
		if(pe.getCategoria().equals("Farinacei")) {
			categoriacb.setSelectedIndex(3);
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
		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clean();
				c.visAndprod(3);
			}
		});

		clearbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clean();
			}
		});

		updatebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat data = new SimpleDateFormat ("yyyy-MM-dd");
				try {
					if(nometf.getText().equals("") || descta.getText().equals("") || prezzotf.getText().equals("") || provtf.getText().equals("") || scortatf.getText().equals("")) {
    		    		JOptionPane.showMessageDialog(null, "Inserisci tutti i componenti");
    		    	} else {
    		    		if(categoriacb.getSelectedItem().toString().equals("Ortofrutticoli")) {
            		    	c.upprod(new Prodotto(cod, nometf.getText(), descta.getText(), Double.parseDouble(prezzotf.getText()), provtf.getText(), data.parse(racctf.getText()), null, glutcb.isSelected(), null, categoriacb.getSelectedItem().toString(), Integer.parseInt(scortatf.getText())));
            		    } else if(categoriacb.getSelectedItem().toString().equals("Inscatolati")) {
            		    	c.upprod(new Prodotto(cod, nometf.getText(), descta.getText(), Double.parseDouble(prezzotf.getText()), provtf.getText(), null, null, glutcb.isSelected(), data.parse(scadtf.getText()), categoriacb.getSelectedItem().toString(), Integer.parseInt(scortatf.getText())));
            		    } else if(categoriacb.getSelectedItem().toString().equals("Latticini")) {
            		    	c.upprod(new Prodotto(cod, nometf.getText(), descta.getText(), Double.parseDouble(prezzotf.getText()), provtf.getText(), null, data.parse(mungtf.getText()), glutcb.isSelected(), null, categoriacb.getSelectedItem().toString(), Integer.parseInt(scortatf.getText())));
            		    } else if(categoriacb.getSelectedItem().toString().equals("Farinacei")) {
        					c.upprod(new Prodotto(cod, nometf.getText(), descta.getText(), Double.parseDouble(prezzotf.getText()), provtf.getText(), null, null, glutcb.isSelected(), null, categoriacb.getSelectedItem().toString(), Integer.parseInt(scortatf.getText())));
            		    }
    		    		clean();
    		    		c.visAndprod(3);
					    JOptionPane.showMessageDialog(null, "Prodotto Modificato");
    		    	}
				} catch (NumberFormatException | SQLException | ParseException e1) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
				}
		  }
		});
	}

	public ModificaProdottiFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}