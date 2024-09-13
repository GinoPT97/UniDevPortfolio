package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
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

public class PuntiTesseraFrame extends JFrame {

	private JPanel contentPane;
	private JTextField idtf;
	private JButton backbutton;
	private JButton clearbutton;
	private JButton visbutton;
	private JLabel totlab;
	private JLabel titlelab;
	private JPanel requestpanel;
	private JPanel rispanel;

	public void elementi() {
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    setBounds(100, 100, 850, 450);
	    setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
	    setLocationRelativeTo(null);

	    // Pannello principale
	    contentPane = new JPanel(new BorderLayout());
	    contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
	    setContentPane(contentPane);

	    // Pannello del titolo
	    JPanel titlePanel = new JPanel();
	    titlePanel.setBackground(new Color(0, 128, 0));
	    JLabel titleLabel = new JLabel("Tessera Punti");
	    titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
	    titlePanel.add(titleLabel);
	    contentPane.add(titlePanel, BorderLayout.NORTH);

	    // Pannello dei bottoni
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout((LayoutManager) new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Allineamento a destra con padding
	    contentPane.add(buttonPanel, BorderLayout.SOUTH);

	    visbutton = new JButton("Visualizza");
	    visbutton.setBackground(Color.GREEN);
	    buttonPanel.add(visbutton);

	    clearbutton = new JButton("Pulisci");
	    clearbutton.setBackground(Color.WHITE);
	    buttonPanel.add(clearbutton);

	    backbutton = new JButton("Indietro");
	    backbutton.setBackground(Color.RED);
	    buttonPanel.add(backbutton);

	    // Pannello dei contenuti
	    JPanel contentPanel = new JPanel(new BorderLayout());
	    contentPanel.setBorder(new EmptyBorder(90, 100, 80, 100));
	    contentPane.add(contentPanel, BorderLayout.CENTER);

	    // Pannello di richiesta
	    requestpanel = new JPanel();
	    contentPanel.add(requestpanel, BorderLayout.NORTH);

	    JLabel idLabel = new JLabel("Inserisci l'id :");
	    idtf = new JTextField(10); // Imposta la larghezza del campo di testo
	    requestpanel.add(idLabel);
	    requestpanel.add(idtf);

	    // Pannello dei risultati
	    rispanel = new JPanel();
	    contentPanel.add(rispanel, BorderLayout.SOUTH);

	    JLabel totLabel = new JLabel("Punti Totali :");
	    totlab = new JLabel("0.00");
	    rispanel.add(totLabel);
	    rispanel.add(totlab);
	}

	void clean() {
		idtf.setText("");
		totlab.setText("0.00");
	}

	public void azioni(Controller c) {
	    // Pulsante di visualizzazione
	    visbutton.addActionListener(e -> {
	        String id = idtf.getText().trim();
	        if (!id.isEmpty()) {
	            try {
	                String punti = c.punti(id);
	                totlab.setText(punti);
	            } catch (SQLException ex) {
	                JOptionPane.showMessageDialog(null, "Errore nella comunicazione con il database!\nTipo di errore: " + ex);
	            }
	        } else {
	            JOptionPane.showMessageDialog(null, "Inserire l'id della tessera.");
	        }
	    });

	    // Pulsante di pulizia
	    clearbutton.addActionListener(e -> clean());

	    // Pulsante di ritorno
	    backbutton.addActionListener(e -> {
	        clean();
	        c.dipAndElem(4);
	    });
	}

	public PuntiTesseraFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
