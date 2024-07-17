package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private Controller c;
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
	private JComboBox categoriacb;

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
		clearbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clean();
			}
		});

		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clean();
				c.visAndprod(3);
			}
		});

		insertbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DateFormat data = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if (nometf.getText().equals("") || descta.getText().equals("") || prezzotf.getText().equals("")
							|| provtf.getText().equals("") || scortatf.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Inserisci tutti i componenti");
					} else {
						if (categoriacb.getSelectedItem().toString().equals("Ortofrutticoli")) {
							c.newprod(new Prodotto("", nometf.getText(), descta.getText(),
									Double.parseDouble(prezzotf.getText()), provtf.getText(),
									data.parse(racctf.getText()), null, glutcb.isSelected(), null,
									categoriacb.getSelectedItem().toString(), Integer.parseInt(scortatf.getText())));
						} else if (categoriacb.getSelectedItem().toString().equals("Inscatolati")) {
							c.newprod(new Prodotto("", nometf.getText(), descta.getText(),
									Double.parseDouble(prezzotf.getText()), provtf.getText(), null, null,
									glutcb.isSelected(), data.parse(scadtf.getText()),
									categoriacb.getSelectedItem().toString(), Integer.parseInt(scortatf.getText())));
						} else if (categoriacb.getSelectedItem().toString().equals("Latticini")) {
							c.newprod(new Prodotto("", nometf.getText(), descta.getText(),
									Double.parseDouble(prezzotf.getText()), provtf.getText(), null,
									data.parse(mungtf.getText()), glutcb.isSelected(), null,
									categoriacb.getSelectedItem().toString(), Integer.parseInt(scortatf.getText())));
						} else if (categoriacb.getSelectedItem().toString().equals("Farinacei")) {
							c.newprod(new Prodotto("", nometf.getText(), descta.getText(),
									Double.parseDouble(prezzotf.getText()), provtf.getText(), null, null,
									glutcb.isSelected(), null, categoriacb.getSelectedItem().toString(),
									Integer.parseInt(scortatf.getText())));
						}
						clean();
						JOptionPane.showMessageDialog(null, "Aggiunta effettuata");
					}
				} catch (NumberFormatException | SQLException | ParseException e1) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
				}
			}
		});

		selbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int type = categoriacb.getSelectedIndex();
				switch (type) {
				case 0:
					racctf.setEditable(true);
					mungtf.setEditable(false);
					scadtf.setEditable(false);
					glutcb.setEnabled(false);
					break;
				case 1:
					racctf.setEditable(false);
					mungtf.setEditable(false);
					scadtf.setEditable(true);
					glutcb.setEnabled(false);
					break;
				case 2:
					racctf.setEditable(false);
					mungtf.setEditable(true);
					scadtf.setEditable(false);
					glutcb.setEnabled(false);
					break;
				case 3:
					racctf.setEditable(false);
					mungtf.setEditable(false);
					scadtf.setEditable(false);
					glutcb.setEnabled(true);
					break;
				}
			}
		});

		descta.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (descta.getText().length() >= 500) {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}

	public void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(NuovoProdottoFrame.class.getResource("/Immagini/ImmIcon.png")));

		JPanel elempanel = new JPanel();
		elempanel.setBorder(new EmptyBorder(10, 50, 10, 50));
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
		descrizionepanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel descrlab = new JLabel("Descrizione :");
		descrizionepanel.add(descrlab);

		descta = new JTextArea();
		descta.setRows(2);
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

		JLabel racclab = new JLabel("Data Raccolta (YYYY-MM-DD) :");
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

		JLabel scadlab = new JLabel("Data Scadenza (YYYY-MM-DD)  :");
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

		categoriacb = new JComboBox(new String[] { "Ortofrutticoli", "Inscatolati", "Latticini", "Farinacei" });
		categoriapanel.add(categoriacb);

		selbutton = new JButton("Selezione");
		categoriapanel.add(selbutton);

		buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		insertbutton = new JButton("Inserisci");
		insertbutton.setBackground(Color.GREEN);
		buttonpanel.add(insertbutton);

		clearbutton = new JButton("Pulisci");
		clearbutton.setBackground(Color.WHITE);
		buttonpanel.add(clearbutton);

		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		buttonpanel.add(backbutton);

		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(new Color(139, 0, 0));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		JLabel titlelabel = new JLabel("Inserimento nuovo prodotto");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelabel);
	}

	public NuovoProdottoFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
