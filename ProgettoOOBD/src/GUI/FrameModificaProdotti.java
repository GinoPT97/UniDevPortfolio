package GUI;

import DBEntities.Prodotto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FrameModificaProdotti extends JFrame {
    private JPanel mainPanel;
    private JComboBox categoryComboBox;
    private JLabel categoryLabel;
    private JTextField nameTextField;
    private JLabel nameLabel;
    private JTextField descriptionTextField;
    private JLabel descriptionLabel;
    private JTextField placeTextField;
    private JLabel placeLabel;
    private JTextField priceTextField;
    private JLabel priceLabel;
    private JComboBox collectionMonthComboBox;
    private JComboBox collectiondayComboBox;
    private JLabel collectiondateLabel;
    private JComboBox milkingmonthComboBox;
    private JComboBox milkingdayComboBox;
    private JLabel milkingdateLabel;
    private JComboBox expirationyearComboBox;
    private JComboBox expirationmonthComboBox;
    private JComboBox expirationdayComboBox;
    private JLabel expirationLabel;
    private JLabel glutenLabel;
    private JCheckBox glutenyesCheckBox;
    private JCheckBox glutennoCheckBox;
    private JTextField stockTextField;
    private JLabel stockLabel;
    private JButton changeButton;
    private JButton clearButton;
    private JButton backButton;
    private int selected = 0;
    private Controller theController;
    private ButtonGroup glutenGroup = new ButtonGroup();

    public FrameModificaProdotti(String title, Controller c, String id, String name, String descritpion, float price,
                                 String place, java.util.Date collectionDate, java.util.Date milkingDate, Boolean gluten, java.util.Date expirationDate,
                                 String category, int stock) {
        super(title);
        theController = c;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setBounds(500,300,900,500);
        glutenGroup.add(glutenyesCheckBox);
        glutenGroup.add(glutennoCheckBox);
        nameTextField.setText(name);
        descriptionTextField.setText(descritpion);
        priceTextField.setText(String.valueOf(price));
        placeTextField.setText(place);
        stockTextField.setText(String.valueOf(stock));
        Calendar calendar = Calendar.getInstance();
        if (category.equals("Ortofrutticoli")) {
            categoryComboBox.setSelectedItem("Ortofrutticoli");
            collectiondateLabel.setEnabled(true);
            collectiondayComboBox.setEnabled(true);
            collectionMonthComboBox.setEnabled(true);
            calendar.setTime(collectionDate);
            collectiondayComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))); //Calendar.get(Calendar.DAY_OF_WEEK).
            collectionMonthComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.MONTH) + 1)); //I mesi hanno indice iniziale 0, quindi per ottenere il mese corretto va sommato 1
        }
        if (category.equals("Latticini")) {
            categoryComboBox.setSelectedItem("Latticini");
            milkingdateLabel.setEnabled(true);
            milkingdayComboBox.setEnabled(true);
            milkingmonthComboBox.setEnabled(true);
            calendar.setTime(milkingDate);
            milkingdayComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            milkingdayComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        }
        if (category.equals("Farinacei")) {
            glutenLabel.setEnabled(true);
            if (gluten.equals(true)) {
                glutenyesCheckBox.setEnabled(true);
                glutenyesCheckBox.setSelected(true);
            }
            else {
                glutennoCheckBox.setEnabled(true);
                glutennoCheckBox.setSelected(false);
            }
        }
        if (category.equals("Inscatolati")) {
            categoryComboBox.setSelectedItem("Inscatolati");
            expirationLabel.setEnabled(true);
            expirationdayComboBox.setEnabled(true);
            expirationmonthComboBox.setEnabled(true);
            expirationyearComboBox.setEnabled(true);
            calendar.setTime(expirationDate);
            expirationdayComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            expirationmonthComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.MONTH) + 1));
            expirationyearComboBox.setSelectedItem(String.valueOf(calendar.get(Calendar.YEAR)));
        }
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
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date;
                if (!nameTextField.getText().equals("") && !priceTextField.equals("") && !stockTextField.equals("")) {
                    if (categoryComboBox.getSelectedItem().toString().equals("Ortofrutticoli")) {
                        date = "2021-" + collectionMonthComboBox.getSelectedItem().toString() + "-" + collectiondayComboBox.getSelectedItem().toString();
                        try {
                            if (theController.changeProdotto(id, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), df.parse(date), null, null, null,
                                    categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText())));
                                JOptionPane.showMessageDialog(mainPanel, "Prodotto modificato con successo!", "Successo!", JOptionPane.INFORMATION_MESSAGE);
                        } catch (ParseException | SQLException ex) {
                            JOptionPane.showMessageDialog(mainPanel, "ParseException | SQL Exception: " + ex, "Errore!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (categoryComboBox.getSelectedItem().toString().equals("Latticini")) {
                        date = "2021-" + milkingmonthComboBox.getSelectedItem().toString() + "-" + milkingdayComboBox.getSelectedItem().toString();
                        try {
                            if (theController.changeProdotto(id, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), null, df.parse(date), null, null,
                                    categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText())));
                            JOptionPane.showMessageDialog(mainPanel, "Prodotto modificato con successo!", "Successo!", JOptionPane.INFORMATION_MESSAGE);
                        } catch (ParseException | SQLException ex) {
                            JOptionPane.showMessageDialog(mainPanel, "ParseException | SQL Exception: " + ex, "Errore!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (categoryComboBox.getSelectedItem().toString().equals("Inscatolati")) {
                        date = expirationyearComboBox.getSelectedItem().toString() + "-" + expirationmonthComboBox.getSelectedItem().toString() + "-" + expirationdayComboBox.getSelectedItem().toString();
                        try {
                            if (theController.changeProdotto(id, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), null, null, null, df.parse(date),
                                    categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText())));
                            JOptionPane.showMessageDialog(mainPanel, "Prodotto modificato con successo!", "Successo!", JOptionPane.INFORMATION_MESSAGE);
                        } catch (ParseException | SQLException ex) {
                            JOptionPane.showMessageDialog(mainPanel, "ParseException | SQL Exception: " + ex, "Errore!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (categoryComboBox.getSelectedItem().toString().equals("Farinacei")) {
                        if (glutenyesCheckBox.isSelected()) {
                            try {
                            if (theController.changeProdotto(id, nameTextField.getText(), descriptionTextField.getText(),
                                    Float.parseFloat(priceTextField.getText()), placeTextField.getText(), null, null, true, null,
                                    categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText())));
                            JOptionPane.showMessageDialog(mainPanel, "Prodotto modificato con successo!", "Successo!", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(mainPanel, "SQL Exception: " + ex, "Errore!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            try {
                                if (theController.changeProdotto(id, nameTextField.getText(), descriptionTextField.getText(),
                                        Float.parseFloat(priceTextField.getText()), placeTextField.getText(), null, null, false, null,
                                        categoryComboBox.getSelectedItem().toString(), Integer.parseInt(stockTextField.getText())));
                                JOptionPane.showMessageDialog(mainPanel, "Prodotto modificato con successo!", "Successo!", JOptionPane.INFORMATION_MESSAGE);
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(mainPanel, "SQL Exception: " + ex, "Errore!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    theController.closeFrame(theController.getChangeProdottoFrame());
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
