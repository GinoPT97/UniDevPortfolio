package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class Login extends JFrame {

    private Controller theController;
    private JLabel loginLabel;
    private JPanel rightPanel;
    private JPanel leftPanel;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JComboBox roleComboBox;
    private JLabel roleLabel;
    private JLabel userLabel;
    private JLabel passwordLabel;
    private JTextField userTextField;
    private JPasswordField pwdPasswordField;
    private JButton loginButton;
    private JButton clearButton;
    private JPanel buttonsPanel;
    private JLabel storeLabel;

    public Login(String title, Controller c) throws SQLException {
        super(title);
        theController = c;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        //setLocation((getToolkit().getScreenSize().width / 2) - (getWidth() / 2), (getToolkit().getScreenSize().height / 2) - (getHeight() / 2));
        //this.setBounds((getToolkit().getScreenSize().width / 2) - (getWidth() / 2), (getToolkit().getScreenSize().height / 2) - (getHeight() / 2),700,400);
        this.setBounds(650,250,700,500);
        c.startConnection();
        //Azione alla pressione del pulsante "Login"
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(roleComboBox.getSelectedItem().equals("Gestore")) {
                    if (userTextField.getText().equals("Admin")) {
                        String password = new String(pwdPasswordField.getPassword());
                        if (password.equals("1")) {
                            try {
                                c.login((String)roleComboBox.getSelectedItem());
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                            userTextField.setText("");
                            pwdPasswordField.setText("");
                        }
                    } else {
                        JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                        userTextField.setText("");
                        pwdPasswordField.setText("");
                    }
                }
                /*else  {
                    JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                    userTextField.setText("");
                    pwdPasswordField.setText("");
                }*/
                if (roleComboBox.getSelectedItem().equals("Dipendente")) {
                    try {
                        //Se l'ID corrisponde ad un Dipendente
                        if (c.checkID(userTextField.getText())) {
                            String password = new String(pwdPasswordField.getPassword());
                            if (password.equals("User"))
                                c.login((String)roleComboBox.getSelectedItem());
                            else {
                                JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                                userTextField.setText("");
                                pwdPasswordField.setText("");
                            }
                        }
                            else {
                            JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                            userTextField.setText("");
                            pwdPasswordField.setText("");
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTextField.setText("");
                pwdPasswordField.setText("");
            }
        });
        /*mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        c.login((String)roleComboBox.getSelectedItem());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });*/
        pwdPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                checkCredentials();
            }
        });
    }

    public void checkCredentials() {
        if(roleComboBox.getSelectedItem().equals("Gestore")) {
            if (userTextField.getText().equals("Admin")) {
                String password = new String(pwdPasswordField.getPassword());
                if (password.equals("Administrator")) {
                    try {
                        theController.login((String)roleComboBox.getSelectedItem());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                    userTextField.setText("");
                    pwdPasswordField.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                userTextField.setText("");
                pwdPasswordField.setText("");
            }
        }
                /*else  {
                    JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                    userTextField.setText("");
                    pwdPasswordField.setText("");
                }*/
        if (roleComboBox.getSelectedItem().equals("Dipendente")) {
            try {
                //Se l'ID corrisponde ad un Dipendente
                if (theController.checkID(userTextField.getText())) {
                    String password = new String(pwdPasswordField.getPassword());
                    if (password.equals("User"))
                        theController.login((String)roleComboBox.getSelectedItem());
                    else {
                        JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                        userTextField.setText("");
                        pwdPasswordField.setText("");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(loginPanel, "L'ID fornito e/o la password inserita sono errati!", "Errore!", JOptionPane.ERROR_MESSAGE);
                    userTextField.setText("");
                    pwdPasswordField.setText("");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void clearFields() {
        this.userTextField.setText("");
        this.pwdPasswordField.setText("");
    }
}
