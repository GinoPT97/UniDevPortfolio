package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class RicercaFrame extends JFrame {

	private JPanel contentPane;
	private JTable searchtable;
	private JComboBox categoriacb;
	private DefaultTableModel searchmodel = new DefaultTableModel();
	private Object[] searchcolonne = {"Nome","Cognome","Categoria","Numero Punti"};
	private JButton backbutton;
	private JButton searchbutton;
	private JComboBox punticb;
	public int x;

	public void elementi() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RicercaFrame.class.getResource("/Immagini/ImmIcon.png")));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 904, 433);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
		setLocationRelativeTo(null);

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(new Color(100, 149, 237));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		JLabel titlelabel = new JLabel("Ricerca Clienti");
		titlelabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlelabel.setBackground(new Color(100, 149, 237));
		titlepanel.add(titlelabel);

		JPanel searchpanel = new JPanel();
		searchpanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		contentPane.add(searchpanel, BorderLayout.WEST);
		searchpanel.setLayout(new BoxLayout(searchpanel, BoxLayout.Y_AXIS));

		JPanel puntipanel = new JPanel();
		searchpanel.add(puntipanel);

		JLabel puntilab = new JLabel("Ricerca Clienti per punti");
		puntipanel.add(puntilab);

		JPanel panel = new JPanel();
		searchpanel.add(panel);

				categoriacb = new JComboBox(new String[]{"Ortofrutticoli","Inscatolati","Latticini","Farinacei"});
				panel.add(categoriacb);

		punticb = new JComboBox(new String[] {"0-500","501-1000","1001-5000",">5000","Tutti"});
		panel.add(punticb);

		JPanel tablepanel = new JPanel();
		contentPane.add(tablepanel);
		tablepanel.setLayout(new BoxLayout(tablepanel, BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		tablepanel.add(scrollPane);

		searchtable = new JTable();
		searchtable.setModel(searchmodel);
		searchtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(searchtable);

		JPanel buttonpanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonpanel.getLayout();
		flowLayout.setAlignment(FlowLayout.TRAILING);
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		searchbutton = new JButton("Ricerca");
		searchbutton.setBackground(new Color(30, 144, 255));
		buttonpanel.add(searchbutton);

		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		backbutton.setHorizontalAlignment(SwingConstants.TRAILING);
		buttonpanel.add(backbutton);
		final Object[] rows = new Object[5];
		searchmodel.setColumnIdentifiers(searchcolonne);
	}

	public void azioni(Controller c) throws SQLException{
		c.ClientSearch(searchmodel);

		searchbutton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String categoriaSelezionata = (String) categoriacb.getSelectedItem();
		        String intervalloPunti = (String) punticb.getSelectedItem();

		        // Filtrare i risultati basati sulla categoria
		        RowFilter<Object, Object> categoriaFilter = null;
		        if (!categoriaSelezionata.equals("Tutti")) {
		            categoriaFilter = RowFilter.regexFilter(categoriaSelezionata, 2);
		        }

		        // Filtrare i risultati basati sull'intervallo di punti
		        RowFilter<Object, Object> puntiFilter = null;
		        switch (intervalloPunti) {
		            case "0-500":
		                puntiFilter = RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 501, 3);
		                break;
		            case "501-1000":
		                puntiFilter = RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 1001, 3);
		                puntiFilter = RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 500, 3);
		                break;
		            case "1001-5000":
		                puntiFilter = RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 5001, 3);
		                puntiFilter = RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 1000, 3);
		                break;
		            case ">5000":
		                puntiFilter = RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 5000, 3);
		                break;
		            default:
		                break;
		        }

		        // Applicare i filtri
		        List<RowFilter<Object, Object>> filters = new ArrayList<>();
		        if (categoriaFilter != null) {
		            filters.add(categoriaFilter);
		        }
		        if (puntiFilter != null) {
		            filters.add(puntiFilter);
		        }

		        if (!filters.isEmpty()) {
		            RowFilter<Object, Object> combinedFilter = RowFilter.andFilter(filters);
		            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(searchmodel);
		            sorter.setRowFilter(combinedFilter);
		            searchtable.setRowSorter(sorter);
		        } else {
		            searchtable.setRowSorter(null); // Rimuovi tutti i filtri se non ci sono criteri selezionati
		        }
		    }
		});

		backbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.returnToLastFrame();
			}
		});
		
	}

	public RicercaFrame(String title,Controller c) throws SQLException{
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
