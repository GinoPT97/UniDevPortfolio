package com.pellegrinoprincipe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class SpringLayoutDemo extends JFrame
{
    private JButton button_ok;
    private JTextField text_login;
    private JPasswordField text_pwd;
    private JLabel lbl_login;
    private JLabel lbl_pwd;
    private JPanel box = new JPanel();

    public SpringLayoutDemo()
    {
        super("SpringLayout DEMO");

        // creo i componenti grafici
        button_ok = new JButton("Invia");
        text_login = new JTextField(20);
        text_pwd = new JPasswordField(20);
        lbl_login = new JLabel("Login: ");
        lbl_pwd = new JLabel("Password: ");
        
        box.setPreferredSize(new Dimension(400, 180)); // dimensiono il pannello

        // creo il layout manager e lo assegno al container
        SpringLayout spring_layout = new SpringLayout();
        box.setLayout(spring_layout);

        // posizionamento dei bordi di lbl_login rispetto ai bordi del box
        spring_layout.putConstraint(SpringLayout.WEST, lbl_login, 5, SpringLayout.WEST, box);
        spring_layout.putConstraint(SpringLayout.NORTH, lbl_login, 5, SpringLayout.NORTH, box);

        // posizionamento dei bordi di text_login rispetto ai bordi del box e di lbl_login
        spring_layout.putConstraint(SpringLayout.WEST, text_login, 30, SpringLayout.EAST, lbl_login);
        spring_layout.putConstraint(SpringLayout.NORTH, text_login, 5, SpringLayout.NORTH, box);

        // posizionamento dei bordi di lbl_pwd rispetto ai bordi del box
        spring_layout.putConstraint(SpringLayout.WEST, lbl_pwd, 5, SpringLayout.WEST, box);
        spring_layout.putConstraint(SpringLayout.NORTH, lbl_pwd, lbl_login.getPreferredSize().height + 5, SpringLayout.NORTH, lbl_login);

        // posizionamento dei bordi di text_pwd rispetto ai bordi di lbl_pwd e di text_login
        spring_layout.putConstraint(SpringLayout.WEST, text_pwd, 3, SpringLayout.EAST, lbl_pwd);
        spring_layout.putConstraint(SpringLayout.NORTH, text_pwd, text_login.getPreferredSize().height + 5, SpringLayout.NORTH, text_login);

        // posizionamento dei bordi di button_ok rispetto ai bordi del box
        spring_layout.putConstraint(SpringLayout.WEST, button_ok, 236, SpringLayout.WEST, box);
        spring_layout.putConstraint(SpringLayout.SOUTH, button_ok, -100, SpringLayout.SOUTH, box);

        box.add(lbl_login);
        box.add(text_login);
        box.add(lbl_pwd);
        box.add(text_pwd);
        box.add(button_ok);

        add(box, BorderLayout.PAGE_START);
    }

    public static void main(String args[])
    {
        SpringLayoutDemo window = new SpringLayoutDemo();

        // imposto le dimensioni della finestra
        window.setSize(400, 180);

        // scelgo cosa succederà quando si chiuderà la finestra
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // rendo visibile la finestra
        window.setVisible(true);

        // centro la finestra nello schermo
        window.setLocationRelativeTo(null);
    }
}
