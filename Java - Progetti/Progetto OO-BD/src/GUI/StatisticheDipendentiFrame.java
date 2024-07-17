package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class StatisticheDipendentiFrame extends JFrame {
	private Controller c;
	private JPanel contentPane;
	private String[] datacb = { "3 mesi", "6 mesi", "9 mesi", "12 mesi", "Tutti" };
	private LocalDate dataod = LocalDate.now();
	private JPanel searchpanel;
	private JPanel introitipanel;
	private JPanel venditepanel;
	private JPanel buttonpanel;
	private JButton selectbutton;
	private JComboBox periodocb;
	private JLabel periodolab;
	private JLabel introlab;
	private JLabel venditelab;
	private JButton backbutton;
	private JButton clearbutton;
	private JButton searchbutton;
	private JPanel titlepanel;
	private JLabel titlelabel;
	private JPanel firstpanel;
	private JPanel secondpanel;
	private JLabel startlab;
	private JTextField starttf;
	private JTextField finaltf;
	private JLabel finallab;
	private JPanel nomepanel;
	private JPanel rispanel;
	private JPanel cognomepanel;
	private JTextField nomeintroititf;
	private JLabel nomelab;
	private JLabel cognomelab;
	private JTextField cognomeintroititf;
	private JLabel introitilab;
	private JTextField introititf;
	private JPanel nomeintroitipanel;
	private JPanel cognomeintroitipanel;
	private JPanel risintroitipanel;
	private JLabel nomelab1;
	private JTextField nomevenditetf;
	private JLabel cognomelab1;
	private JTextField cognomevenditetf;
	private JLabel venditelab1;
	private JTextField venditetf;
	private List<String> ordven;
	private List<String> ordint;
	private String old = null;

	public void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));

		searchpanel = new JPanel();
		searchpanel.setBorder(new EmptyBorder(100, 10, 100, 10));
		contentPane.add(searchpanel, BorderLayout.WEST);
		searchpanel.setLayout(new BoxLayout(searchpanel, BoxLayout.Y_AXIS));

		periodolab = new JLabel("Periodo ricerca (YYYY-MM-DD)");
		searchpanel.add(periodolab);

		firstpanel = new JPanel();
		searchpanel.add(firstpanel);

		startlab = new JLabel("Da : ");
		firstpanel.add(startlab);

		starttf = new JTextField();
		starttf.setColumns(10);
		firstpanel.add(starttf);

		secondpanel = new JPanel();
		searchpanel.add(secondpanel);

		finallab = new JLabel("Fino a :");
		secondpanel.add(finallab);

		finaltf = new JTextField();
		finaltf.setColumns(10);
		secondpanel.add(finaltf);

		periodocb = new JComboBox(datacb);
		periodocb.setMaximumRowCount(4);
		searchpanel.add(periodocb);

		selectbutton = new JButton("Seleziona");
		searchpanel.add(selectbutton);

		introitipanel = new JPanel();
		introitipanel.setBorder(new EmptyBorder(100, 30, 100, 40));
		contentPane.add(introitipanel);
		introitipanel.setLayout(new BoxLayout(introitipanel, BoxLayout.Y_AXIS));

		introlab = new JLabel("Dipendente con piu' introiti");
		introitipanel.add(introlab);

		nomepanel = new JPanel();
		introitipanel.add(nomepanel);

		nomelab = new JLabel("Nome :");
		nomepanel.add(nomelab);

		nomeintroititf = new JTextField();
		nomeintroititf.setEditable(false);
		nomeintroititf.setColumns(10);
		nomepanel.add(nomeintroititf);

		cognomepanel = new JPanel();
		introitipanel.add(cognomepanel);

		cognomelab = new JLabel("Cognome :");
		cognomepanel.add(cognomelab);

		cognomeintroititf = new JTextField();
		cognomeintroititf.setEditable(false);
		cognomeintroititf.setColumns(10);
		cognomepanel.add(cognomeintroititf);

		rispanel = new JPanel();
		introitipanel.add(rispanel);

		introitilab = new JLabel("Introiti :");
		rispanel.add(introitilab);

		introititf = new JTextField();
		introititf.setEditable(false);
		introititf.setColumns(10);
		rispanel.add(introititf);

		venditepanel = new JPanel();
		venditepanel.setBorder(new EmptyBorder(100, 30, 100, 10));
		contentPane.add(venditepanel, BorderLayout.EAST);
		venditepanel.setLayout(new BoxLayout(venditepanel, BoxLayout.Y_AXIS));

		venditelab = new JLabel("Dipendente con piu' vendite");
		venditepanel.add(venditelab);

		nomeintroitipanel = new JPanel();
		venditepanel.add(nomeintroitipanel);

		nomelab1 = new JLabel("Nome :");
		nomeintroitipanel.add(nomelab1);

		nomevenditetf = new JTextField();
		nomevenditetf.setEditable(false);
		nomevenditetf.setColumns(10);
		nomeintroitipanel.add(nomevenditetf);

		cognomeintroitipanel = new JPanel();
		venditepanel.add(cognomeintroitipanel);

		cognomelab1 = new JLabel("Cognome :");
		cognomeintroitipanel.add(cognomelab1);

		cognomevenditetf = new JTextField();
		cognomevenditetf.setEditable(false);
		cognomevenditetf.setColumns(10);
		cognomeintroitipanel.add(cognomevenditetf);

		risintroitipanel = new JPanel();
		venditepanel.add(risintroitipanel);

		venditelab1 = new JLabel("Vendite :");
		risintroitipanel.add(venditelab1);

		venditetf = new JTextField();
		venditetf.setEditable(false);
		venditetf.setColumns(10);
		risintroitipanel.add(venditetf);

		buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		searchbutton = new JButton("Cerca");
		searchbutton.setBackground(Color.GREEN);
		buttonpanel.add(searchbutton);

		clearbutton = new JButton("Pulisci");
		clearbutton.setBackground(Color.WHITE);
		buttonpanel.add(clearbutton);

		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		buttonpanel.add(backbutton);

		titlepanel = new JPanel();
		titlepanel.setBackground(new Color(147, 112, 219));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		titlelabel = new JLabel("Statistiche dipendenti");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelabel);
	}

	private void clean() {
		starttf.setText("");
		finaltf.setText("");
		nomeintroititf.setText("");
		cognomeintroititf.setText("");
		nomevenditetf.setText("");
		cognomevenditetf.setText("");
		introititf.setText("");
		venditetf.setText("");
	}

	public void azioni(Controller c) throws SQLException {
		old = c.OldDate();
		selectbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				finaltf.setText(dataod.toString());
				if (periodocb.getSelectedItem().equals("3 mesi")) {
					starttf.setText(dataod.minusMonths(3) + "");
				} else if (periodocb.getSelectedItem().equals("6 mesi")) {
					starttf.setText(dataod.minusMonths(6).toString());
				} else if (periodocb.getSelectedItem().equals("9 mesi")) {
					starttf.setText(dataod.minusMonths(9).toString());
				} else if (periodocb.getSelectedItem().equals("12 mesi")) {
					starttf.setText(dataod.minusMonths(12).toString());
				} else if (periodocb.getSelectedItem().equals("Tutti")) {
					starttf.setText(old);
				}
			}
		});

		searchbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Bottone premuto");

				String startText = starttf.getText();
				String finalText = finaltf.getText();

				if (starttf.getText().equals("") || finaltf.getText().equals("")) {
					System.out.println("Mostrando messaggio di errore...");
					JOptionPane.showMessageDialog(null, "Inserire le date di ricerca!");
				}

				try {
					java.sql.Date di = java.sql.Date.valueOf(startText);
					java.sql.Date df = java.sql.Date.valueOf(finalText);

					System.out.println("Date convertite correttamente: " + di + ", " + df);

					ordint = c.introitidip(di, df);
					ordven = c.venditedip(di, df);

					if (ordint.isEmpty() || ordven.isEmpty()) {
						JOptionPane.showMessageDialog(null,
								"In questo lasso di tempo non ci sono risultati!\nAmpliare il lasso di tempo");
						clean();
					} else {
						nomeintroititf.setText(ordint.get(0).toString());
						cognomeintroititf.setText(ordint.get(1).toString());
						introititf.setText(ordint.get(2).toString());
						nomevenditetf.setText(ordven.get(0).toString());
						cognomevenditetf.setText(ordven.get(1).toString());
						venditetf.setText(ordven.get(2).toString());
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1);
				} catch (IllegalArgumentException e2) {
					JOptionPane.showMessageDialog(null,
							"Le date inserite non sono valide. Utilizzare il formato yyyy-MM-dd.");
				}
			}
		});

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
				c.adminAndElem(5);
			}
		});
	}

	public StatisticheDipendentiFrame(String title, Controller c) throws SQLException {
		super(title);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(StatisticheDipendentiFrame.class.getResource("/Immagini/ImmIcon.png")));
		this.elementi();
		this.azioni(c);
	}
}
