package GUI;

import DAOImplementations.DipendenteDAOPostgreImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import DBEntities.Dipendente;
import java.sql.*;

public class FrameGestioneDipendenti extends JFrame {

    private Controller theController;
    private JPanel mainPanel;
    private JButton logoutButton;
    private JPanel rightPanel;
    private JScrollPane tableScrollPane;
    private JTable workerTable;
    private JComboBox filterComboBox;
    private JLabel filterLabel;
    private JButton addButton;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton editButton;
    private JPanel leftPanel;
    private JLabel labelFrame;
    private JButton backButton;
    private JButton updateButton;
    private ArrayList<Dipendente> dipendentiList = new ArrayList<Dipendente>();


    public FrameGestioneDipendenti(String title, Controller c) throws SQLException {
        super(title);
        theController = c;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setBounds(500,300,900,500);
        createDipendentiTable();
        DipendenteDAOPostgreImpl d = new DipendenteDAOPostgreImpl(theController.getConnection());
        dipendentiList = d.getAllDipendenti();
        fillTable(c.getConnection(), dipendentiList);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theController.logout(theController.getDipendentiFrame());
            }
        });
        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dipendentiList.clear();
                System.out.println((String)filterComboBox.getSelectedItem());
                dipendentiList = d.filterDipendentiListBy((String)filterComboBox.getSelectedItem());
                fillTable(c.getConnection(), dipendentiList);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    theController.showAddDipendenteFrame();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dipendentiList.clear();
                dipendentiList = d.filterDipendentiListBy((String)filterComboBox.getSelectedItem());
                fillTable(theController.getConnection(), dipendentiList);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theController.backFrame(theController.getDipendentiFrame(), theController.getAdminFrame());
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theController.logout(theController.getDipendentiFrame());
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = null;
                String name = null;
                String lastname = null;
                String cf = null;
                String address = null;
                String phone = null;
                String email = null;
                if (workerTable.getSelectedRow() == -1)
                    JOptionPane.showMessageDialog(mainPanel, "Selezionare il valore da modificare!", "Errore!", JOptionPane.ERROR_MESSAGE);
                else {
                    code = workerTable.getValueAt(workerTable.getSelectedRow(), 0).toString();
                    name = workerTable.getValueAt(workerTable.getSelectedRow(), 1).toString();
                    lastname = workerTable.getValueAt(workerTable.getSelectedRow(), 2).toString();
                    cf = workerTable.getValueAt(workerTable.getSelectedRow(), 3).toString();
                    address = workerTable.getValueAt(workerTable.getSelectedRow(), 4).toString();
                    phone = workerTable.getValueAt(workerTable.getSelectedRow(), 5).toString();
                    email = workerTable.getValueAt(workerTable.getSelectedRow(), 7).toString();
                    try {
                        theController.showChangeDipendenteFrame(code, name, lastname,
                                cf, address, phone, email);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });
    }

    private void createDipendentiTable() {
        workerTable.setModel(new DefaultTableModel(null, new String[] {"Codice Dipendente", "Nome", "Cognome", "Codice Fiscale",
                "Indirizzo", "Telefono 1", "Telefono 2", "Email"}));
    }

    private void fillTable(Connection connection, ArrayList<Dipendente> dipendentiList) {
        DefaultTableModel model = (DefaultTableModel)workerTable.getModel();
        model.setRowCount(0);
        for (Dipendente dp : dipendentiList) {
            model.addRow(new Object[] {dp.getCodiceDipendente(), dp.getNome(), dp.getCognome(), dp.getCodiceFiscale(),
                    dp.getIndirizzo(), dp.getTelefono(), "null", dp.getEmail()});
        }
    }
}
