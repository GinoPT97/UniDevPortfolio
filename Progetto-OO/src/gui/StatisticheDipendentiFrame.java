package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.Controller;

public class StatisticheDipendentiFrame extends JFrame {
    private JPanel contentPane;
    private final String[] datacb = {"3 mesi", "6 mesi", "9 mesi", "12 mesi", "Tutti"};
    private final LocalDate dataod = LocalDate.now();
    private JPanel searchPanel, introitiPanel, venditePanel, buttonPanel, titlePanel;
    private JButton selectButton, backButton, clearButton, searchButton;
    private JComboBox<String> periodoCB;
    private JLabel periodoLab, cognomeVenditeLab, titleLabel, startLab, finalLab, nomeLab, cognomeLab, introitiValLab, nomeVenditeLab, venditeValLab;
    private JTextField startTF, finalTF;
    private final JTextField[] introitiFields = new JTextField[3]; // nome, cognome, introiti
    private final JTextField[] venditeFields = new JTextField[3]; // nome, cognome, vendite

    public StatisticheDipendentiFrame(String title, Controller c) throws SQLException {
        super(title);
        this.elementi();
        this.azioni(c);
    }

    private void elementi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 870, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 10, 0));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Pannello del titolo
        titlePanel = new JPanel();
        titlePanel.setBackground(new Color(147, 112, 219));
        titleLabel = new JLabel("Statistiche dipendenti");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titlePanel.add(titleLabel);
        contentPane.add(titlePanel, BorderLayout.NORTH);

        // Pannello di ricerca
        searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Ricerca"));
        GroupLayout searchLayout = new GroupLayout(searchPanel);
        searchPanel.setLayout(searchLayout);

        periodoLab = new JLabel("Periodo ricerca (YYYY-MM-DD)");
        startLab = new JLabel("Da : ");
        startTF = new JTextField(10);
        finalLab = new JLabel("Fino a :");
        finalTF = new JTextField(10);
        periodoCB = new JComboBox<>(datacb);
        periodoCB.setMaximumRowCount(5);
        selectButton = new JButton("Seleziona", gui.IconUtils.getIconForText("Seleziona", new Color(46, 204, 113)));
        selectButton.setBackground(new Color(46, 204, 113));
        selectButton.setForeground(Color.WHITE);
        selectButton.setFocusPainted(false);

        searchLayout.setAutoCreateGaps(true);
        searchLayout.setAutoCreateContainerGaps(true);

        searchLayout.setHorizontalGroup(
                searchLayout.createSequentialGroup()
                        .addGroup(searchLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(periodoLab)
                                .addComponent(startLab)
                                .addComponent(finalLab)
                                .addComponent(periodoCB))
                        .addGroup(searchLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(startTF)
                                .addComponent(finalTF)
                                .addComponent(selectButton))
        );

        searchLayout.setVerticalGroup(
                searchLayout.createSequentialGroup()
                        .addComponent(periodoLab)
                        .addGroup(searchLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(startLab)
                                .addComponent(startTF))
                        .addGroup(searchLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(finalLab)
                                .addComponent(finalTF))
                        .addGroup(searchLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(periodoCB)
                                .addComponent(selectButton))
        );

        contentPane.add(searchPanel, BorderLayout.WEST);

        // Pannello introiti
        introitiPanel = new JPanel();
        introitiPanel.setBorder(BorderFactory.createTitledBorder("Dipendente con più introiti"));
        GroupLayout introitiLayout = new GroupLayout(introitiPanel);
        introitiPanel.setLayout(introitiLayout);

        nomeLab = new JLabel("Nome :");
        introitiFields[0] = new JTextField(15);
        introitiFields[0].setEditable(false);
        cognomeLab = new JLabel("Cognome :");
        introitiFields[1] = new JTextField(15);
        introitiFields[1].setEditable(false);
        introitiValLab = new JLabel("Introiti :");
        introitiFields[2] = new JTextField(15);
        introitiFields[2].setEditable(false);

        introitiLayout.setAutoCreateGaps(true);
        introitiLayout.setAutoCreateContainerGaps(true);

        introitiLayout.setHorizontalGroup(
                introitiLayout.createSequentialGroup()
                        .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nomeLab)
                                .addComponent(cognomeLab)
                                .addComponent(introitiValLab))
                        .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(introitiFields[0])
                                .addComponent(introitiFields[1])
                                .addComponent(introitiFields[2]))
        );

        introitiLayout.setVerticalGroup(
                introitiLayout.createSequentialGroup()
                        .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nomeLab)
                                .addComponent(introitiFields[0]))
                        .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(cognomeLab)
                                .addComponent(introitiFields[1]))
                        .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(introitiValLab)
                                .addComponent(introitiFields[2]))
        );

        contentPane.add(introitiPanel, BorderLayout.CENTER);

        // Pannello vendite
        venditePanel = new JPanel();
        venditePanel.setBorder(BorderFactory.createTitledBorder("Dipendente con più vendite"));
        GroupLayout venditeLayout = new GroupLayout(venditePanel);
        venditePanel.setLayout(venditeLayout);

        nomeVenditeLab = new JLabel("Nome :");
        venditeFields[0] = new JTextField(15);
        venditeFields[0].setEditable(false);
        cognomeVenditeLab = new JLabel("Cognome :");
        venditeFields[1] = new JTextField(15);
        venditeFields[1].setEditable(false);
        venditeValLab = new JLabel("Vendite :");
        venditeFields[2] = new JTextField(15);
        venditeFields[2].setEditable(false);

        venditeLayout.setAutoCreateGaps(true);
        venditeLayout.setAutoCreateContainerGaps(true);

        venditeLayout.setHorizontalGroup(
                venditeLayout.createSequentialGroup()
                        .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nomeVenditeLab)
                                .addComponent(cognomeVenditeLab)
                                .addComponent(venditeValLab))
                        .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(venditeFields[0])
                                .addComponent(venditeFields[1])
                                .addComponent(venditeFields[2]))
        );

        venditeLayout.setVerticalGroup(
                venditeLayout.createSequentialGroup()
                        .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nomeVenditeLab)
                                .addComponent(venditeFields[0]))
                        .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(cognomeVenditeLab)
                                .addComponent(venditeFields[1]))
                        .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(venditeValLab)
                                .addComponent(venditeFields[2]))
        );

        contentPane.add(venditePanel, BorderLayout.EAST);

        // Pannello dei pulsanti
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchButton = new JButton("Cerca", gui.IconUtils.getIconForText("Cerca", new Color(52, 152, 219)));
        searchButton.setBackground(new Color(52, 152, 219));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        buttonPanel.add(searchButton);

        clearButton = new JButton("Pulisci", gui.IconUtils.getIconForText("Pulisci", new Color(255, 140, 0)));
        clearButton.setBackground(new Color(255, 140, 0));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        buttonPanel.add(clearButton);

        backButton = new JButton("Indietro", gui.IconUtils.getIconForText("Indietro", new Color(178, 34, 34)));
        backButton.setBackground(new Color(178, 34, 34));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        buttonPanel.add(backButton);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void azioni(Controller c) throws SQLException {
        String oldDate = c.OldDate();

        selectButton.addActionListener(e -> {
            finalTF.setText(dataod.toString());
            String selectedPeriod = (String) periodoCB.getSelectedItem();
            String startDate = switch (selectedPeriod) {
                case "3 mesi" -> dataod.minusMonths(3).toString();
                case "6 mesi" -> dataod.minusMonths(6).toString();
                case "9 mesi" -> dataod.minusMonths(9).toString();
                case "12 mesi" -> dataod.minusMonths(12).toString();
                case "Tutti" -> oldDate;
                default -> oldDate;
            };
            startTF.setText(startDate);
        });

        searchButton.addActionListener(e -> {
            if (startTF.getText().isEmpty() || finalTF.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Inserire le date di ricerca!");
                return;
            }

            try {
                java.sql.Date di = java.sql.Date.valueOf(startTF.getText());
                java.sql.Date df = java.sql.Date.valueOf(finalTF.getText());

                List<String> ordInt = c.introitidip(di, df);
                List<String> ordVen = c.venditedip(di, df);

                if (ordInt.isEmpty() || ordVen.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "In questo lasso di tempo non ci sono risultati!\nAmpliare il lasso di tempo");
                    clean();
                } else {
                    for (int i = 0; i < 3; i++) introitiFields[i].setText(ordInt.get(i));
                    for (int i = 0; i < 3; i++) venditeFields[i].setText(ordVen.get(i));
                }
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Errore!\nTipo di errore: " + e1);
            } catch (IllegalArgumentException e2) {
                JOptionPane.showMessageDialog(null, "Le date inserite non sono valide. Utilizzare il formato yyyy-MM-dd.");
            }
        });

        clearButton.addActionListener(e -> clean());

        backButton.addActionListener(e -> {
            clean();
            c.returnToLastFrame();
        });
    }

    private void clean() {
        startTF.setText("");
        finalTF.setText("");
        for (JTextField f : introitiFields) f.setText("");
        for (JTextField f : venditeFields) f.setText("");
    }
}
