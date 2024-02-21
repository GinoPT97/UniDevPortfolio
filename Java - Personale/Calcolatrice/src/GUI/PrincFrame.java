package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class PrincFrame extends JFrame{
	private Controller c;
	private JButton calcbutt = new JButton("Calcolatrice");
	private JButton storicobutt = new JButton("Storico");

	public void elem() {
		getContentPane().setLayout(null);
		storicobutt.setBounds(163, 97, 150, 21);
		getContentPane().add(storicobutt);
		calcbutt.setBounds(163, 66, 150, 21);
		getContentPane().add(calcbutt);
		this.setBounds(100, 200, 454, 284);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void azioni(Controller c) {
		calcbutt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.calc();
			}
		});
		storicobutt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.storico();
			}
		});
	}

	public PrincFrame(String title, Controller c) {
		super(title);
		this.elem();
		this.azioni(c);
	}
}