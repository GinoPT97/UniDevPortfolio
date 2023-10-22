package GUI;

import CodiceFiscale.CodiceFiscale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class FrameModificaDipendenti extends JFrame {

    private Controller theController;
    private JPanel mainPanel;
    private JComboBox sexComboBox;
    private JLabel sexLabel;
    private JTextField nameTextField;
    private JLabel firstnameLabel;
    private JTextField lastnameTextField;
    private JLabel lastnameLabel;
    private JLabel bdayLabel;
    private JComboBox dayComboBox;
    private JComboBox monthComboBox;
    private JComboBox yearComboBox;
    private JLabel addressLabel;
    private JTextField addressTextField;
    private JComboBox provinciaComboBox;
    private JLabel provinciaLabel;
    private JComboBox cityComboBox;
    private JLabel cityLabel;
    private JTextField cfTextField;
    private JButton generateButton;
    private JButton clearCFButton;
    private JTextField emailTexField;
    private JTextField phoneTextField;
    private JLabel cfLabel;
    private JLabel emailLabel;
    private JLabel phoneLabel;
    private JButton backButton;
    private JButton clearButton;
    private JButton changeButton;

    public FrameModificaDipendenti(String title, Controller c, String id, String n, String ln, String cf, String a, String ph, String e) throws SQLException {
        super(title);
        theController = c;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setBounds(500,300,900,500);
        nameTextField.setText(n);
        lastnameTextField.setText(ln);
        cfTextField.setText(cf);
        addressTextField.setText(a);
        phoneTextField.setText(ph);
        emailTexField.setText(e);
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
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (theController.changeDipendente(id, nameTextField.getText(), lastnameTextField.getText(), cfTextField.getText(),
                            addressTextField.getText(), phoneTextField.getText(), emailTexField.getText()))
                        JOptionPane.showMessageDialog(mainPanel, "Dipendente modificato con successo!", "Successo!", JOptionPane.INFORMATION_MESSAGE);
                    theController.closeFrame(theController.getChangeDipendenteFrame());
                } catch (SQLException exc) {
                    JOptionPane.showMessageDialog(mainPanel, "SQL Exception: " + exc, "Errore!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
