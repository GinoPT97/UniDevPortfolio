package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameAdminHome extends JFrame {

    private Controller theController;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JButton productButton;
    private JButton clientButton;
    private JLabel frameLabel;
    private JButton logoutButton;
    private JButton workerButton;

    public FrameAdminHome(String title, Controller c) {
        super(title);
        theController = c;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setBounds(500,300,500,300);
        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theController.showFrameGestioneProdotti();
            }
        });
        workerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                theController.showDipendentiFrame();
            }
        });
    }
}
