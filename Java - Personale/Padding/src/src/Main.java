package src;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private JPanel contentPane;

	private JTextField dimension;


	public Main() {
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 317, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Dimensione Messaggio: ");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblNewLabel.setBounds(10, 31, 165, 34);
		contentPane.add(lblNewLabel);

		dimension = new JTextField();
		dimension.setBounds(170, 40, 86, 20);
		contentPane.add(dimension);
		dimension.setColumns(10);

		JLabel lblPadding = new JLabel("Padding: ");
		lblPadding.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblPadding.setBounds(10, 165, 62, 34);
		contentPane.add(lblPadding);

		JLabel lblPaddingResult = new JLabel("");
		lblPaddingResult.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblPaddingResult.setBounds(77, 165, 62, 34);
		contentPane.add(lblPaddingResult);

		JLabel lblDimensioneFinale = new JLabel("Dimensione finale:");
		lblDimensioneFinale.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblDimensioneFinale.setBounds(10, 219, 135, 34);
		contentPane.add(lblDimensioneFinale);

		JLabel lblDimensionResult = new JLabel("");
		lblDimensionResult.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblDimensionResult.setBounds(142, 219, 62, 34);
		contentPane.add(lblDimensionResult);



		JLabel lblLBlocchi = new JLabel("L Blocchi: ");
		lblLBlocchi.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblLBlocchi.setBounds(10, 276, 86, 34);
		contentPane.add(lblLBlocchi);

		JLabel lblBlocchiResult = new JLabel("");
		lblBlocchiResult.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblBlocchiResult.setBounds(83, 276, 62, 34);
		contentPane.add(lblBlocchiResult);

		JLabel lblNWord = new JLabel("N Word: ");
		lblNWord.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblNWord.setBounds(10, 339, 78, 34);
		contentPane.add(lblNWord);

		JLabel lblWordResult = new JLabel("");
		lblWordResult.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblWordResult.setBounds(77, 339, 62, 34);
		contentPane.add(lblWordResult);

		JButton calcolabutton = new JButton("Calcola");
		calcolabutton.setForeground(Color.ORANGE);
		calcolabutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int dim = Integer.parseInt(dimension.getText()) + 64;

				int padding = 512 - (dim%512);
				lblPaddingResult.setText(String.valueOf(padding));

				int dimFinal = dim + padding;
				lblDimensionResult.setText(String.valueOf(dimFinal));

				int blocchi = dimFinal/512;
				lblBlocchiResult.setText(String.valueOf(blocchi));

				int word = dimFinal/32;
				lblWordResult.setText(String.valueOf(word));

			}
		});

		calcolabutton.setBounds(104, 102, 89, 23);
		contentPane.add(calcolabutton);

	}
}
