package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
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

public class StatisticheDipendentiFrame extends JFrame {
    private Controller c;
    private JPanel contentPane;
    private String[] datacb = { "3 mesi", "6 mesi", "9 mesi", "12 mesi", "Tutti" };
    private LocalDate dataod = LocalDate.now();
    private JPanel searchPanel;
    private JPanel introitiPanel;
    private JPanel venditePanel;
    private JPanel buttonPanel;
    private JButton selectButton;
    private JComboBox<String> periodoCB;
    private JLabel periodoLab;
    private JLabel cognomeVenditeLab;
    private JButton backButton;
    private JButton clearButton;
    private JButton searchButton;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JLabel startLab;
    private JTextField startTF;
    private JTextField finalTF;
    private JLabel finalLab;
    private JTextField nomeIntroitiTF;
    private JLabel nomeLab;
    private JLabel cognomeLab;
    private JTextField cognomeIntroitiTF;
    private JLabel introitiValLab;
    private JTextField introitiTF;
    private JTextField nomeVenditeTF;
    private JLabel nomeVenditeLab;
    private JTextField cognomeVenditeTF;
    private JLabel venditeValLab;
    private JTextField venditeTF;
    private List<String> ordVen;
    private List<String> ordInt;
    private String oldDate = null;



    private void elementi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 870, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 10, 0)); // Rimosso margine superiore
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
        startTF = new JTextField(10); // Ridotto il numero di colonne
        finalLab = new JLabel("Fino a :");
        finalTF = new JTextField(10); // Ridotto il numero di colonne
        periodoCB = new JComboBox<>(datacb);
        periodoCB.setMaximumRowCount(5);
        selectButton = new JButton("Seleziona");

        searchLayout.setAutoCreateGaps(true);
        searchLayout.setAutoCreateContainerGaps(true);

        searchLayout.setHorizontalGroup(
            searchLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(searchLayout.createSequentialGroup()
                    .addGroup(searchLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(periodoLab)
                        .addComponent(startLab)
                        .addComponent(finalLab)
                        .addComponent(periodoCB))
                    .addGroup(searchLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(startTF)
                        .addComponent(finalTF)
                        .addComponent(selectButton)))
        );

        searchLayout.setVerticalGroup(
            searchLayout.createSequentialGroup()
                .addGroup(searchLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(periodoLab))
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
        nomeIntroitiTF = new JTextField(15);
        nomeIntroitiTF.setEditable(false);
        cognomeLab = new JLabel("Cognome :");
        cognomeIntroitiTF = new JTextField(15);
        cognomeIntroitiTF.setEditable(false);
        introitiValLab = new JLabel("Introiti :");
        introitiTF = new JTextField(15);
        introitiTF.setEditable(false);

        introitiLayout.setAutoCreateGaps(true);
        introitiLayout.setAutoCreateContainerGaps(true);

        introitiLayout.setHorizontalGroup(
            introitiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(introitiLayout.createSequentialGroup()
                    .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nomeLab)
                        .addComponent(cognomeLab)
                        .addComponent(introitiValLab))
                    .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nomeIntroitiTF)
                        .addComponent(cognomeIntroitiTF)
                        .addComponent(introitiTF)))
        );

        introitiLayout.setVerticalGroup(
            introitiLayout.createSequentialGroup()
                .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeLab)
                    .addComponent(nomeIntroitiTF))
                .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cognomeLab)
                    .addComponent(cognomeIntroitiTF))
                .addGroup(introitiLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(introitiValLab)
                    .addComponent(introitiTF))
        );

        contentPane.add(introitiPanel, BorderLayout.CENTER);

        // Pannello vendite
        venditePanel = new JPanel();
        venditePanel.setBorder(BorderFactory.createTitledBorder("Dipendente con più vendite"));
        GroupLayout venditeLayout = new GroupLayout(venditePanel);
        venditePanel.setLayout(venditeLayout);

        nomeVenditeLab = new JLabel("Nome :");
        nomeVenditeTF = new JTextField(15);
        nomeVenditeTF.setEditable(false);
        cognomeVenditeLab = new JLabel("Cognome :");
        cognomeVenditeTF = new JTextField(15);
        cognomeVenditeTF.setEditable(false);
        venditeValLab = new JLabel("Vendite :");
        venditeTF = new JTextField(15);
        venditeTF.setEditable(false);

        venditeLayout.setAutoCreateGaps(true);
        venditeLayout.setAutoCreateContainerGaps(true);

        venditeLayout.setHorizontalGroup(
            venditeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(venditeLayout.createSequentialGroup()
                    .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nomeVenditeLab)
                        .addComponent(cognomeVenditeLab)
                        .addComponent(venditeValLab))
                    .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nomeVenditeTF)
                        .addComponent(cognomeVenditeTF)
                        .addComponent(venditeTF)))
        );

        venditeLayout.setVerticalGroup(
            venditeLayout.createSequentialGroup()
                .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeVenditeLab)
                    .addComponent(nomeVenditeTF))
                .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cognomeVenditeLab)
                    .addComponent(cognomeVenditeTF))
                .addGroup(venditeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(venditeValLab)
                    .addComponent(venditeTF))
        );

        contentPane.add(venditePanel, BorderLayout.EAST);

        // Pannello dei pulsanti
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchButton = new JButton("Cerca");
        searchButton.setBackground(Color.GREEN);
        buttonPanel.add(searchButton);

        clearButton = new JButton("Pulisci");
        clearButton.setBackground(Color.WHITE);
        buttonPanel.add(clearButton);

        backButton = new JButton("Indietro");
        backButton.setBackground(Color.RED);
        buttonPanel.add(backButton);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void azioni(Controller c) throws SQLException {
        oldDate = c.OldDate();

        // Gestione del pulsante selezione
        selectButton.addActionListener(e -> {
            finalTF.setText(dataod.toString());
            String selectedPeriod = (String) periodoCB.getSelectedItem();
            String startDate = oldDate;

            switch (selectedPeriod) {
                case "3 mesi":
                    startDate = dataod.minusMonths(3).toString();
                    break;
                case "6 mesi":
                    startDate = dataod.minusMonths(6).toString();
                    break;
                case "9 mesi":
                    startDate = dataod.minusMonths(9).toString();
                    break;
                case "12 mesi":
                    startDate = dataod.minusMonths(12).toString();
                    break;
                case "Tutti":
                    break; // Start date remains as oldDate
            }
            startTF.setText(startDate);
        });

        // Gestione del pulsante cerca
        searchButton.addActionListener(e -> {
            String startText = startTF.getText();
            String finalText = finalTF.getText();

            if (startText.isEmpty() || finalText.isEmpty()) {
                showMessage("Inserire le date di ricerca!");
                return;
            }

            try {
                java.sql.Date di = java.sql.Date.valueOf(startText);
                java.sql.Date df = java.sql.Date.valueOf(finalText);

                ordInt = c.introitidip(di, df);
                ordVen = c.venditedip(di, df);

                if (ordInt.isEmpty() || ordVen.isEmpty()) {
                    showMessage("In questo lasso di tempo non ci sono risultati!\nAmpliare il lasso di tempo");
                    clean();
                } else {
                    populateFields(ordInt, nomeIntroitiTF, cognomeIntroitiTF, introitiTF);
                    populateFields(ordVen, nomeVenditeTF, cognomeVenditeTF, venditeTF);
                }
            } catch (SQLException e1) {
                showMessage("Errore!\nTipo di errore: " + e1);
            } catch (IllegalArgumentException e2) {
                showMessage("Le date inserite non sono valide. Utilizzare il formato yyyy-MM-dd.");
            }
        });

        // Gestione del pulsante pulisci
        clearButton.addActionListener(e -> clean());

        // Gestione del pulsante indietro
        backButton.addActionListener(e -> {
            clean();
            c.adminAndElem(5);
        });
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void populateFields(List<String> data, JTextField nomeTF, JTextField cognomeTF, JTextField valoreTF) {
        nomeTF.setText(data.get(0));
        cognomeTF.setText(data.get(1));
        valoreTF.setText(data.get(2));
    }

    private void clean() {
        startTF.setText("");
        finalTF.setText("");
        nomeIntroitiTF.setText("");
        cognomeIntroitiTF.setText("");
        nomeVenditeTF.setText("");
        cognomeVenditeTF.setText("");
        introitiTF.setText("");
        venditeTF.setText("");
    }

   public StatisticheDipendentiFrame(String title, Controller c) throws SQLException {
          super(title);
          setIconImage(Toolkit.getDefaultToolkit().getImage(StatisticheDipendentiFrame.class.getResource("/Immagini/ImmIcon.png")));
          this.elementi();
          this.azioni(c);
   }
}


