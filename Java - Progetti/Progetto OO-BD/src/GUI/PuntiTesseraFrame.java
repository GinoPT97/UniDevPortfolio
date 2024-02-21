package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setIconImage(Toolkit.getDefaultToolkit().getImage(ModificaProdottiFrame.class.getResource("/Immagini/ImmIcon.png")));
		setLocationRelativeTo(null);

		JPanel buttonpanel = new JPanel();
		contentPane.add(buttonpanel, BorderLayout.SOUTH);

		visbutton = new JButton("Visualizza");
		visbutton.setBackground(Color.GREEN);
		buttonpanel.add(visbutton);

		clearbutton = new JButton("Pulisci");
		clearbutton.setBackground(Color.WHITE);
		buttonpanel.add(clearbutton);

		backbutton = new JButton("Indietro");
		backbutton.setBackground(Color.RED);
		buttonpanel.add(backbutton);

		JPanel titlepanel = new JPanel();
		titlepanel.setBackground(new Color(0, 128, 0));
		contentPane.add(titlepanel, BorderLayout.NORTH);

		titlelab = new JLabel("Tessera Punti");
		titlelab.setFont(new Font("Tahoma", Font.BOLD, 30));
		titlepanel.add(titlelab);

		JPanel elempanel = new JPanel();
		elempanel.setBorder(new EmptyBorder(90, 100, 80, 100));
		contentPane.add(elempanel);
		elempanel.setLayout(new BorderLayout(0, 0));

		requestpanel = new JPanel();
		elempanel.add(requestpanel, BorderLayout.NORTH);

		JLabel idlabel = new JLabel("Inserisci l'id :");
		requestpanel.add(idlabel);

		idtf = new JTextField();
		requestpanel.add(idtf);
		idtf.setColumns(10);

		rispanel = new JPanel();
		elempanel.add(rispanel);

		JLabel totlabel = new JLabel("Punti Totali :");
		rispanel.add(totlabel);

		totlab = new JLabel("0.00");
		rispanel.add(totlab);
	}

	void clean() {
		idtf.setText("");
		totlab.setText("0.00");
	}

	public void azioni(Controller c) {
		visbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(idtf.getText()!=null) {
						totlab.setText(c.punti(idtf.getText()));
					} else {
						JOptionPane.showMessageDialog(null, "Inserire l'id della tessera");
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore!" + "\n" + "Tipo di errore : " + e1);
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
				c.dipAndElem(4);
			}
		});
	}

	public PuntiTesseraFrame(String title, Controller c) {
		super(title);
		this.elementi();
		this.azioni(c);
	}
}
