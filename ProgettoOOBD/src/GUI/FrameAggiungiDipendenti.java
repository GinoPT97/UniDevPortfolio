package GUI;

import CodiceFiscale.CodiceFiscale;
import DBEntities.Dipendente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class FrameAggiungiDipendenti extends JFrame {
    private JPanel mainPanel;
    private JTextField nameTextField;
    private JTextField lastnameTextField;
    private JComboBox sexComboBox;
    private JComboBox monthComboBox;
    private JComboBox yearComboBox;
    private JComboBox dayComboBox;
    private JTextField cfTextField;
    private JButton addButton;
    private JButton clearButton;
    private JButton backButton;
    private JLabel firstnameLabel;
    private JLabel lastnameLabel;
    private JLabel sexLabel;
    private JLabel bdayLabel;
    private JLabel cfLabel;
    private JLabel titleLabel;
    private JButton generateButton;
    private JButton clearCFButton;
    private JLabel addressLabel;
    private JTextField addressTextField;
    private JLabel provinciaLabel;
    private JTextField emailTexField;
    private JLabel emailLabel;
    private JTextField phoneTextField;
    private JLabel phoneLabel;
    private JLabel cityLabel;
    private JComboBox cityComboBox;
    private JComboBox provinciaComboBox;
    private Controller theController;

    public FrameAggiungiDipendenti(String title, Controller c) throws SQLException {
        super(title);
        System.out.println("Non dovrebbe essere chiamato");
        theController = c;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setBounds(500,300,900,500);
        ArrayList<String> province = theController.getProvince();
        Iterator<String> p = province.iterator();
        while(p.hasNext())
            provinciaComboBox.addItem(p.next());
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CodiceFiscale cf = null;
                try {
                    cf = new CodiceFiscale(sexComboBox.getSelectedItem().toString().charAt(0), nameTextField.getText().toUpperCase(),
                            lastnameTextField.getText().toUpperCase(), Integer.parseInt(dayComboBox.getSelectedItem().toString()), monthComboBox.getSelectedItem().toString(),
                            Integer.parseInt(yearComboBox.getSelectedItem().toString()), cityComboBox.getSelectedItem().toString(), theController);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    cfTextField.setText(cf.generateCF());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        provinciaComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cityComboBox.removeAllItems();
                    ArrayList<String> comuni = theController.getComuniByProvincia(provinciaComboBox.getSelectedItem().toString());
                    Iterator<String> c = comuni.iterator();
                    while(c.hasNext())
                        cityComboBox.addItem(c.next());
                } catch (SQLException exc) {
                    System.out.println("SQL Exception: " + exc);
                }
            }
        });
        clearCFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cfTextField.setText("");
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
                lastnameTextField.setText("");
                cfTextField.setText("");
                addressTextField.setText("");
                emailTexField.setText("");
                phoneTextField.setText("");
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dipendente d = new Dipendente("", nameTextField.getText(), lastnameTextField.getText(), cfTextField.getText(),
                        addressTextField.getText(), phoneTextField.getText(), emailTexField.getText());
                try {
                    if(theController.addDipendente(d))
                        JOptionPane.showMessageDialog(mainPanel, "Dipendente aggiunto con successo!", "Successo!", JOptionPane.INFORMATION_MESSAGE);
                    theController.closeFrame(theController.getAddDipendenteFrame());
                } catch (SQLException exc) {
                    JOptionPane.showMessageDialog(mainPanel, "SQL Exception: " + exc, "Errore!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
