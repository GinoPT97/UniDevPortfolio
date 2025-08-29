package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.Controller;

public class NuovoProdottoFrame extends JFrame {
	// Costanti per le categorie
	private static final String FRUTTA = "FRUTTA";
	private static final String VERDURA = "VERDURA";
	private static final String FARINACEI = "FARINACEI";
	private static final String LATTICINI = "LATTICINI";
	private static final String UOVA = "UOVA";
	private static final String CONFEZIONATI = "CONFEZIONATI";

	private final String[] labels = { "Nome:", "Descrizione:", "Provenienza:", "Prezzo:", "Data Raccolta (YYYY-MM-DD):",
			"Data Mungitura (YYYY-MM-DD):", "Data Produzione (YYYY-MM-DD):", "Glutine:", "Data Scadenza (YYYY-MM-DD):",
			"Scorta:" };
	private final JComponent[] fields = new JComponent[labels.length];
	private JButton backbutton;
	private JButton clearbutton;
	private JButton insertbutton;
	// private JButton selbutton; // Rimosso, non più necessario
	private JComboBox<String> categoriacb;

	public NuovoProdottoFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}

	private void elementi() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 765); // Finestra più compatta, textarea invariata
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);

		JPanel elempanel = new JPanel(new GridBagLayout());
		elempanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
		elempanel.setBackground(new Color(245, 245, 250));
		contentPane.add(elempanel, BorderLayout.CENTER);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		// Inizializza i componenti e li inserisce nell'array
		fields[0] = new JTextField(24); // Nome
		fields[1] = new JTextArea(); // Descrizione
		JTextArea descArea = (JTextArea) fields[1];
		descArea.setRows(5);
		descArea.setColumns(28);
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		descArea.setForeground(Color.GRAY);
		descArea.setText("Inserisci la descrizione");
		// Forza dimensioni minime/preferite/massime
		Dimension areaDim = new Dimension(280, 80);
		descArea.setMinimumSize(areaDim);
		descArea.setPreferredSize(areaDim);
		descArea.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		// Placeholder: rimuovi al focus
		descArea.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if (descArea.getText().equals("Inserisci la descrizione")) {
					descArea.setText("");
					descArea.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (descArea.getText().isEmpty()) {
					descArea.setForeground(Color.GRAY);
					descArea.setText("Inserisci la descrizione");
				}
			}
		});
		fields[2] = new JTextField(24); // Provenienza
		fields[3] = new JTextField(14); // Prezzo
		fields[4] = new JTextField(16); // Data Raccolta
		fields[5] = new JTextField(16); // Data Mungitura
		fields[6] = new JTextField(16); // Data Produzione
		fields[7] = new JCheckBox("Contiene glutine"); // Glutine
		fields[8] = new JTextField(16); // Data Scadenza
		fields[9] = new JTextField(12); // Scorta

		Font labelFont = new Font("Tahoma", Font.BOLD, 16);
		Font fieldFont = new Font("Tahoma", Font.PLAIN, 15);

		int row = 0;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel nomeLabel = new JLabel("Nome prodotto:");
		nomeLabel.setFont(labelFont);
		elempanel.add(nomeLabel, gbc);
		gbc.gridx = 1;
		((JTextField) fields[0]).setFont(fieldFont);
		elempanel.add(fields[0], gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel descLabel = new JLabel("Descrizione:");
		descLabel.setFont(labelFont);
		elempanel.add(descLabel, gbc);
		gbc.gridx = 1;
		JScrollPane descScroll = new JScrollPane(fields[1]);
		descScroll.setMinimumSize(areaDim);
		descScroll.setPreferredSize(areaDim);
		descScroll.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		JPanel descPanel = new JPanel(new BorderLayout());
		descPanel.add(descScroll, BorderLayout.CENTER);
		JLabel charCountLabel = new JLabel("0/500 caratteri");
		charCountLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
		charCountLabel.setForeground(Color.DARK_GRAY);
		descPanel.add(charCountLabel, BorderLayout.SOUTH);
		// Listener per contatore caratteri e limite
		descArea.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				String text = descArea.getText();
				if (text.equals("Inserisci la descrizione"))
					text = "";
				if (text.length() >= 500 && descArea.getSelectedText() == null)
					e.consume();
			}

			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {
				String text = descArea.getText();
				if (text.equals("Inserisci la descrizione"))
					text = "";
				charCountLabel.setText(text.length() + "/500 caratteri");
			}
		});
		elempanel.add(descPanel, gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel provLabel = new JLabel("Provenienza:");
		provLabel.setFont(labelFont);
		elempanel.add(provLabel, gbc);
		gbc.gridx = 1;
		((JTextField) fields[2]).setFont(fieldFont);
		elempanel.add(fields[2], gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel prezzoLabel = new JLabel("Prezzo (€):");
		prezzoLabel.setFont(labelFont);
		elempanel.add(prezzoLabel, gbc);
		gbc.gridx = 1;
		((JTextField) fields[3]).setFont(fieldFont);
		elempanel.add(fields[3], gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel raccLabel = new JLabel("Data Raccolta:");
		raccLabel.setFont(labelFont);
		elempanel.add(raccLabel, gbc);
		gbc.gridx = 1;
		((JTextField) fields[4]).setFont(fieldFont);
		elempanel.add(fields[4], gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel mungLabel = new JLabel("Data Mungitura:");
		mungLabel.setFont(labelFont);
		elempanel.add(mungLabel, gbc);
		gbc.gridx = 1;
		((JTextField) fields[5]).setFont(fieldFont);
		elempanel.add(fields[5], gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel prodLabel = new JLabel("Data Produzione:");
		prodLabel.setFont(labelFont);
		elempanel.add(prodLabel, gbc);
		gbc.gridx = 1;
		((JTextField) fields[6]).setFont(fieldFont);
		elempanel.add(fields[6], gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel glutLabel = new JLabel("Glutine:");
		glutLabel.setFont(labelFont);
		elempanel.add(glutLabel, gbc);
		gbc.gridx = 1;
		fields[7].setFont(fieldFont);
		elempanel.add(fields[7], gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel scadLabel = new JLabel("Data Scadenza:");
		scadLabel.setFont(labelFont);
		elempanel.add(scadLabel, gbc);
		gbc.gridx = 1;
		((JTextField) fields[8]).setFont(fieldFont);
		elempanel.add(fields[8], gbc);

		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		JLabel scortaLabel = new JLabel("Scorta:");
		scortaLabel.setFont(labelFont);
		elempanel.add(scortaLabel, gbc);
		gbc.gridx = 1;
		((JTextField) fields[9]).setFont(fieldFont);
		elempanel.add(fields[9], gbc);

		// Categoria e pulsanti
		row++;
		gbc.gridy = row;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		JPanel categoriapanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
		categoriapanel.setOpaque(false);
		categoriacb = new JComboBox<>(new String[] { FRUTTA, VERDURA, FARINACEI, LATTICINI, UOVA, CONFEZIONATI });
		categoriacb.setFont(fieldFont);
		categoriapanel.add(categoriacb);
		elempanel.add(categoriapanel, gbc);

		// Pannello per i pulsanti di azione
		JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
		buttonpanel.setBorder(new EmptyBorder(28, 0, 28, 0));
		buttonpanel.setBackground(new Color(245, 245, 250));
		insertbutton = creaButton("Inserisci", new Color(34, 139, 34));
		insertbutton.setFont(new Font("Tahoma", Font.BOLD, 18));
		buttonpanel.add(insertbutton);
		clearbutton = creaButton("Pulisci", new Color(255, 165, 0));
		clearbutton.setFont(new Font("Tahoma", Font.BOLD, 18));
		buttonpanel.add(clearbutton);
		backbutton = creaButton("Indietro", new Color(178, 34, 34));
		backbutton.setFont(new Font("Tahoma", Font.BOLD, 18));
		buttonpanel.add(backbutton);
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		// Pannello per il titolo
		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(new Color(178, 34, 34));
		JLabel titlelabel = new JLabel("Inserimento nuovo prodotto");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 32));
		titlelabel.setForeground(Color.WHITE);
		titlepanel.add(titlelabel);
		contentPane.add(titlepanel, BorderLayout.NORTH);

		// Migliora visibilità campi disabilitati
		UIManager.put("TextField.inactiveBackground", new Color(230, 230, 230));
		UIManager.put("TextArea.inactiveBackground", new Color(230, 230, 230));
	}

	private JPanel createInputPanel(String labelText, JComponent inputComponent) {
		return createInputPanel(labelText, inputComponent, true);
	}

	private JPanel createInputPanel(String labelText, JComponent inputComponent, boolean editable) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel label = new JLabel(labelText);
		panel.add(label);
		if (inputComponent instanceof JTextField textField)
			textField.setEditable(editable);
		else if (inputComponent instanceof JCheckBox checkBox)
			checkBox.setEnabled(editable);
		panel.add(inputComponent);
		return panel;
	}

	private JButton creaButton(String text, Color color) {
		JButton button = new JButton(text, gui.IconUtils.getIconForText(text, color));
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		return button;
	}

	public void clean() {
		for (JComponent field : fields) {
			if (field instanceof JTextField) {
				JTextField tf = (JTextField) field;
				tf.setText("");
				tf.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
			}
			if (field instanceof JTextArea) {
				JTextArea ta = (JTextArea) field;
				ta.setForeground(Color.GRAY);
				ta.setText("Inserisci la descrizione");
				ta.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextArea.border"));
			}
			if (field instanceof JCheckBox) {
				JCheckBox cb = (JCheckBox) field;
				cb.setSelected(false);
			}
		}
		aggiornaCampiCategoria((String) categoriacb.getSelectedItem());
	}

	private boolean validateFields() {
		boolean valid = true;
		int firstError = -1;
		// Campi obbligatori: nome, descrizione, provenienza, prezzo, scorta
		int[] obbligatori = { 0, 1, 2, 3, 9 };
		for (int idx : obbligatori)
			if (fields[idx] instanceof JTextField tf) {
				String text = tf.getText().trim();
				tf.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
				if (text.isEmpty()) {
					tf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					valid = false;
					if (firstError == -1)
						firstError = idx;
				}
			} else if (fields[idx] instanceof JTextArea ta) {
				String text = ta.getText().trim();
				ta.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextArea.border"));
				if (text.isEmpty()) {
					ta.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					valid = false;
					if (firstError == -1)
						firstError = idx;
				}
			}
		// Prezzo numerico
		String prezzo = ((JTextField) fields[3]).getText().trim();
		if (!prezzo.isEmpty())
			try {
				Double.parseDouble(prezzo);
			} catch (NumberFormatException ex) {
				fields[3].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				valid = false;
				if (firstError == -1)
					firstError = 3;
			}
		// Scorta numerica
		String scorta = ((JTextField) fields[9]).getText().trim();
		if (!scorta.isEmpty() && !scorta.matches("^\\d+$")) {
			fields[9].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
			valid = false;
			if (firstError == -1)
				firstError = 9;
		}
		// Date: formato yyyy-MM-dd se non vuote
		int[] dateIdx = { 4, 5, 6, 8 };
		for (int idx : dateIdx)
			if (fields[idx] instanceof JTextField tf) {
				String text = tf.getText().trim();
				if (!text.isEmpty() && !text.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
					tf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
					valid = false;
					if (firstError == -1)
						firstError = idx;
				}
			}
		if (!valid && firstError != -1) {
			if (fields[firstError] instanceof JTextField tf)
				tf.requestFocus();
			if (fields[firstError] instanceof JTextArea ta)
				ta.requestFocus();
		}
		return valid;
	}

	private void azioni(Controller c) {
		clearbutton.addActionListener(e -> clean());
		backbutton.addActionListener(e -> {
			clean();
			c.visAndElem(4, 3);
		});
		insertbutton.addActionListener(e -> {
			if (!validateFields()) {
				JOptionPane.showMessageDialog(null,
						"Compila correttamente tutti i campi obbligatori.\nControlla i valori numerici e le date.",
						"Errore di validazione", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				String categoria = categoriacb.getSelectedItem().toString();
				java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
						.ofPattern("yyyy-MM-dd");
				java.time.LocalDate dataRaccolta = null;
				java.time.LocalDate dataMungitura = null;
				java.time.LocalDate dataScadenza = null;
				java.time.LocalDate dataProduzione = null;
				if ((categoria.equals(FRUTTA) || categoria.equals(VERDURA))
						&& !((JTextField) fields[4]).getText().trim().isEmpty())
					dataRaccolta = java.time.LocalDate.parse(((JTextField) fields[4]).getText().trim(), formatter);
				if (categoria.equals(LATTICINI) && !((JTextField) fields[5]).getText().trim().isEmpty())
					dataMungitura = java.time.LocalDate.parse(((JTextField) fields[5]).getText().trim(), formatter);
				if ((categoria.equals(UOVA) || categoria.equals(CONFEZIONATI) || categoria.equals(LATTICINI))
						&& !((JTextField) fields[8]).getText().trim().isEmpty())
					dataScadenza = java.time.LocalDate.parse(((JTextField) fields[8]).getText().trim(), formatter);
				if (categoria.equals(LATTICINI) && !((JTextField) fields[6]).getText().trim().isEmpty())
					dataProduzione = java.time.LocalDate.parse(((JTextField) fields[6]).getText().trim(), formatter);
				c.newprod("", ((JTextField) fields[0]).getText(), ((JTextArea) fields[1]).getText(),
						Double.parseDouble(((JTextField) fields[3]).getText()), ((JTextField) fields[2]).getText(),
						dataRaccolta, dataMungitura, ((JCheckBox) fields[7]).isSelected(), dataScadenza, dataProduzione,
						categoria, Integer.parseInt(((JTextField) fields[9]).getText()));
				clean();
				JOptionPane.showMessageDialog(null, "Aggiunta effettuata");
			} catch (NumberFormatException | SQLException | java.time.format.DateTimeParseException e1) {
				JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore : " + e1.getMessage(), "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		// Gestione dinamica della selezione categoria: blocco/sblocco campi
		categoriacb.addItemListener(e -> {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED)
				aggiornaCampiCategoria((String) categoriacb.getSelectedItem());
		});
	}

	// Metodo per abilitare/disabilitare i campi in base alla categoria
	private void aggiornaCampiCategoria(String categoria) {
		fields[4].setEnabled(false); // Data Raccolta
		fields[5].setEnabled(false); // Data Mungitura
		fields[6].setEnabled(false); // Data Produzione
		fields[8].setEnabled(false); // Data Scadenza
		((JCheckBox) fields[7]).setEnabled(false); // Glutine

		if (categoria == null)
			return;
		switch (categoria) {
		case FRUTTA:
		case VERDURA:
			fields[4].setEnabled(true);
			break;
		case FARINACEI:
			((JCheckBox) fields[7]).setEnabled(true);
			break;
		case LATTICINI:
			fields[5].setEnabled(true);
			fields[6].setEnabled(true);
			fields[8].setEnabled(true);
			break;
		case UOVA:
		case CONFEZIONATI:
			fields[8].setEnabled(true);
			break;
		case null:
		default:
			break;
		}
	}
}
