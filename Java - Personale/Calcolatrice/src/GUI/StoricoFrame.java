package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class StoricoFrame extends JFrame {
	private Controller c;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;

	public StoricoFrame(String string, Controller c) {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 500);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(69, 43, 556, 335);
		this.getContentPane().add(scrollPane);

		JButton deletebutton = new JButton("Elimina");
		deletebutton.setBackground(Color.BLUE);
		deletebutton.setBounds(230, 403, 85, 21);
		contentPane.add(deletebutton);

		JButton backbutton = new JButton("Indietro");
		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.back2();
			}
		});
		backbutton.setBackground(Color.RED);
		backbutton.setBounds(375, 403, 85, 21);
		contentPane.add(backbutton);

		table = new JTable();
		model = new DefaultTableModel();
		Object[] colonne = {"1� Operando", "Operatore","2� Operando","Risultato"};
		final Object[] rows = new Object[4];
		model.setColumnIdentifiers(colonne);
		table.setModel(model);
		scrollPane.setViewportView(table);
	}
}
