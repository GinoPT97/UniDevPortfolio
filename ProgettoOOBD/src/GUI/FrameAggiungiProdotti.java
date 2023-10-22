package GUI;

import DBEntities.Prodotto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FrameAggiungiProdotti extends JFrame {
    private JPanel mainPanel;
    private JComboBox categoryComboBox;
    private JTextField nameTextField;
    private JLabel categoryLabel;
    private JLabel nameLabel;
    private JTextField descriptionTextField;
    private JLabel descriptionLabel;
    private JTextField priceTextField;
    private JLabel priceLabel;
    private JTextField placeTextField;
    private JLabel placeLabel;
    private JLabel collectiondateLabel;
    private JComboBox collectionMonthComboBox;
    private JComboBox collectiondayComboBox;
    private JComboBox milkingdayComboBox;
    private JComboBox milkingmonthComboBox;
    private JLabel milkingdateLabel;
    private JLabel expirationLabel;
    private JComboBox expirationdayComboBox;
    private JComboBox expirationmonthComboBox;
    private JComboBox expirationyearComboBox;
    private JCheckBox glutenyesCheckBox;
    private JLabel glutenLabel;
    private JCheckBox glutennoCheckBox;
    private JButton addButton;
    private JButton clearButton;
    private JButton backButton;
    private JTextField stockTextField;
    private JLabel stockLabel;
    private Controller theController;
    private ButtonGroup glutenGroup = new ButtonGroup();
    private int selected = 0;

    public FrameAggiungiProdotti(String title, Controller c) {
        super(title);
        theController = c;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setBounds(500,300,900,500);
        glutenGroup.add(glutenyesCheckBox);
        glutenGroup.add(glutennoCheckBox);
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected = 1;
                switch (categoryComboBox.getSelectedItem().toString()) {
                    case "Ortofrutticoli":
                        disableCategoryElements();
                        collectiondateLabel.setEnabled(true);
                        collectiondayComboBox.setEnabled(true);
                        collectionMonthComboBox.setEnabled(true);
                        break;
                    case "Latticini":
                        disableCategoryElements();
                        milkingdateLabel.setEnabled(true);
                        milkingdayComboBox.setEnabled(true);
                        milkingmonthComboBox.setEnabled(true);
                        break;
                    case "Inscatolati":
                        disableCategoryElements();
                        expirationLabel.setEnabled(true);
                        expirationdayComboBox.setEnabled(true);
                        expirationmonthComboBox.setEnabled(true);
                        expirationyearComboBox.setEnabled(true);
                        break;
                    case "Farinacei":
                        disableCategoryElements();
                        glutenLabel.setEnabled(true);
                        glutenyesCheckBox.setEnabled(true);
                        glutennoCheckBox.setEnabled(true);
                        break;
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
                descriptionTextField.setText("");
                placeTextField.setText("");
                priceTextField.setText("");
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date;
                Prodotto p = null;
                if (!nameTextField.getText().equals("") && !priceTextField.equals("") && !stockTextField.equals("")) {
                    if (categoryComboBox.getSelectedItem().toString().equals("Ortofrutticoli")) {
                        date = "2021-" + collectionMonthComboBox.getSelectedItem().toString() + "-" + collectiondayComboBox.getSelectedItem().toString();
                        try {
                            p = new Prodotto(null, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), df.parse(date), null, null, null,
                                    categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText()));
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    }
                    if (categoryComboBox.getSelectedItem().toString().equals("Latticini")) {
                        date = "2021-" + milkingmonthComboBox.getSelectedItem().toString() + "-" + milkingdayComboBox.getSelectedItem().toString();
                        try {
                            p = new Prodotto(null, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), null, df.parse(date), null,
                                    null, categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText()));
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    }
                    if (categoryComboBox.getSelectedItem().toString().equals("Inscatolati")) {
                        date = expirationyearComboBox.getSelectedItem().toString() + "-" + expirationmonthComboBox.getSelectedItem().toString() + "-" + expirationdayComboBox.getSelectedItem().toString();
                        try {
                            p = new Prodotto(null, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), null, null, df.parse(date), null,
                                    categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText()));
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    }
                    if (categoryComboBox.getSelectedItem().toString().equals("Farinacei")) {
                        if (glutenyesCheckBox.isSelected()) {
                            p = new Prodotto(null, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), null, null, null,
                                    true, categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText()));
                        } else {
                            p = new Prodotto(null, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), null, null, null,
                                    false, categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText()));
                        }
                    }
                    if (selected == 0) {
                        JOptionPane.showMessageDialog(mainPanel, "Errore! Selezionare la categoria del prodotto!", "Errore!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    System.out.println("Test");
                    try {
                        if (theController.addProdotto(p))
                            JOptionPane.showMessageDialog(mainPanel, "Prodotto aggiunto con successo!");
                        theController.closeFrame(theController.getAddProdottiFrame());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(mainPanel, "SQL Exception: " + ex, "Errore!", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(mainPanel, "Erorre! Uno o più campi sono obbligatori!", "Errore!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void disableCategoryElements() {
        collectiondateLabel.setEnabled(false);
        collectiondayComboBox.setEnabled(false);
        collectionMonthComboBox.setEnabled(false);
        milkingdateLabel.setEnabled(false);
        milkingdayComboBox.setEnabled(false);
        milkingmonthComboBox.setEnabled(false);
        expirationLabel.setEnabled(false);
        expirationdayComboBox.setEnabled(false);
        expirationmonthComboBox.setEnabled(false);
        expirationyearComboBox.setEnabled(false);
        glutenLabel.setEnabled(false);
        glutennoCheckBox.setEnabled(false);
        glutenyesCheckBox.setEnabled(false);
    }

}
