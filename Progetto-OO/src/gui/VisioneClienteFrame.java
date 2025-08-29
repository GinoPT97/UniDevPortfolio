package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.Controller;

public class VisioneClienteFrame extends JFrame {
	private JTable table;
	private JButton backbutton;
	private JButton addbutton;
	private JButton updatebutton;
	private JTextField searchtf;
	private JButton searchbutton;

	public VisioneClienteFrame(String title, Controller c) throws SQLException {
		super(title);
		this.elementi(c);
		this.azioni(c);
	}

	private void elementi(Controller c) {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 450);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);

		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(new Color(0, 128, 0));
		titlepanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		JLabel titlelab = new JLabel("Amministrazione Clienti");
		titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlelab.setForeground(Color.WHITE);
		titlepanel.add(titlelab);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable(c.clienteModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);
		table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (!isSelected)
					c.setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(240, 240, 240));
				return c;
			}
		});
		table.getTableHeader().setToolTipText("Clicca per ordinare la colonna");
		// Nascondi sempre la prima colonna (identificativo)
		if (table.getColumnCount() > 0) {
			table.getColumnModel().getColumn(0).setMinWidth(0);
			table.getColumnModel().getColumn(0).setMaxWidth(0);
			table.getColumnModel().getColumn(0).setWidth(0);
		}
		scrollPane.setViewportView(table);

		JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		searchtf = new JTextField(10);
		buttonpanel.add(searchtf);

		searchbutton = creaButton("Cerca", new Color(107, 142, 35));
		buttonpanel.add(searchbutton);

		addbutton = creaButton("Aggiungi", new Color(34, 139, 34));
		buttonpanel.add(addbutton);

		updatebutton = creaButton("Modifica", new Color(70, 130, 180));
		buttonpanel.add(updatebutton);

		// ...existing code...

		backbutton = creaButton("Indietro", new Color(178, 34, 34));
		buttonpanel.add(backbutton);
	}

	private JButton creaButton(String text, Color color) {
		JButton button = new JButton(text, gui.IconUtils.getIconForText(text, color));
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
		return button;
	}

	private void azioni(Controller c) throws SQLException {
		c.allCliente();

		searchbutton.addActionListener(e -> filtraTabella(c));
		addbutton.addActionListener(e -> c.visAndElem(3, 1));
		updatebutton.addActionListener(e -> aggiornaCliente(c));
		backbutton.addActionListener(e -> c.returnToLastFrame());
	}

	private void filtraTabella(Controller c) {
		String query = searchtf.getText().trim();
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(c.clienteModel);
		table.setRowSorter(sorter);
		if (query.isEmpty())
			sorter.setRowFilter(null);
		else
			try {
				RowFilter<DefaultTableModel, Object> filtro = creaFiltro(query);
				sorter.setRowFilter(filtro);
				if (table.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "Nessun risultato trovato.", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					sorter.setRowFilter(null);
				}
			} catch (RuntimeException ex) {
				JOptionPane.showMessageDialog(null, "Errore nella ricerca: " + ex.getMessage(), "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
	}

	private RowFilter<DefaultTableModel, Object> creaFiltro(String query) {
		String[] parole = query.split("\\s+");
		return new RowFilter<>() {
			@Override
			public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
				for (String parola : parole) {
					boolean trovata = false;
					for (int i = 0; i < entry.getValueCount(); i++) {
						Object cell = entry.getValue(i);
						if (cell != null && cell.toString().toLowerCase().contains(parola.toLowerCase())) {
							trovata = true;
							break;
						}
					}
					if (!trovata)
						return false;
				}
				return true;
			}
		};
	}

	private void aggiornaCliente(Controller c) {
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0) {
			String[] clienteData = new String[7];
			for (int i = 0; i < clienteData.length; i++)
				clienteData[i] = table.getValueAt(selectedRow, i).toString();
			c.visAndElem(3, 2);
			c.upclf.viewct(clienteData[0], // codCliente
					clienteData[1], // nome
					clienteData[2], // cognome
					clienteData[3], // codFis
					clienteData[5], // indirizzo
					clienteData[4], // email
					clienteData[6] // telefono
			);
		} else
			JOptionPane.showMessageDialog(null, "Scegli una riga da modificare", "Attenzione",
					JOptionPane.WARNING_MESSAGE);
	}
}
