package GUI;

import DAOImplementations.ProdottoDAOPostgreImpl;
import DBEntities.Prodotto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class FrameGestioneProdotti extends JFrame {

    private Controller theController;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JButton editButton;
    private JButton addButton;
    private JButton deleteButton;
    private JTable productTable;
    private JButton logoutButton;
    private JPanel rightPanel;
    private JComboBox filterComboBox;
    private JLabel labelFrame;
    private JButton backButton;
    private JLabel filterLabel;
    private JScrollPane tableScrollPane;
    private JButton searchButton;
    private JButton updateButton;
    private ArrayList<Prodotto> prodottiList;

    public FrameGestioneProdotti(String title, Controller c) throws SQLException {
        super(title);
        theController = c;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        //this.setBounds(100, 100, 500, 300);
        this.setBounds(500, 300, 900, 500);
        createProdottiTable();
        ProdottoDAOPostgreImpl p = new ProdottoDAOPostgreImpl(theController.getConnection());
        prodottiList = p.getAllProdotti();
        fillTable(c.getConnection(), prodottiList);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theController.logout(theController.getProdottiFrame());
            }
        });
        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prodottiList.clear();
                System.out.println((String) filterComboBox.getSelectedItem());
                prodottiList = p.filterProdottiListBy((String) filterComboBox.getSelectedItem());
                try {
                    fillTable(c.getConnection(), prodottiList);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theController.backFrame(theController.getProdottiFrame(), theController.getAdminFrame());
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theController.showFrameAggiungiProdotti();
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                theController.logout(theController.getProdottiFrame());
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prodottiList.clear();
                prodottiList = p.filterProdottiListBy((String)filterComboBox.getSelectedItem());
                try {
                    fillTable(theController.getConnection(), prodottiList);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = null;
                String name = null;
                String descritpion = null;
                float price = 0;
                String place = null;
                java.util.Date collectionDate = null;
                java.util.Date milkingDate = null;
                Boolean gluten = null;
                java.util.Date expirationDate = null;
                String category = null;
                int stock = 0;
                if (productTable.getSelectedRow() == -1)
                    JOptionPane.showMessageDialog(mainPanel, "Selezionare il valore da modificare!", "Errore!", JOptionPane.ERROR_MESSAGE);
                else {
                    code = productTable.getValueAt(productTable.getSelectedRow(), 0).toString();
                    name = productTable.getValueAt(productTable.getSelectedRow(), 1).toString();
                    descritpion = productTable.getValueAt(productTable.getSelectedRow(), 2).toString();
                    price = (float) productTable.getValueAt(productTable.getSelectedRow(), 3);
                    place = productTable.getValueAt(productTable.getSelectedRow(), 4).toString();
                    collectionDate = (java.util.Date) productTable.getValueAt(productTable.getSelectedRow(), 5);
                    milkingDate = (java.util.Date) productTable.getValueAt(productTable.getSelectedRow(), 6);
                    gluten = (Boolean) productTable.getValueAt(productTable.getSelectedRow(), 7);
                    expirationDate = (java.util.Date) productTable.getValueAt(productTable.getSelectedRow(), 8);
                    category = productTable.getValueAt(productTable.getSelectedRow(), 9).toString();
                    stock = (int) productTable.getValueAt(productTable.getSelectedRow(), 10);
                    theController.showchangeProdottoFrame(code, name, descritpion, price, place, collectionDate, milkingDate, gluten, expirationDate,
                            category, stock);

                }


            }
        });
    }

    public void fillTable(Connection connection, ArrayList<Prodotto> prodottiList) throws SQLException {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);
        for (Prodotto pr : prodottiList) {
            model.addRow(new Object[]{pr.getCodiceProdotto(), pr.getNome(), pr.getDescrizione(), pr.getPrezzo(), pr.getLuogoProvenienza(), pr.getDataRaccolta(),
                    pr.getDataMungitura(), pr.getGlutine(), pr.getDataScadenza(), pr.getCategoria(), pr.getScorta()});
        }
    }

    public void createProdottiTable() {
        productTable.setModel(new DefaultTableModel(null, new String[]{"Codice Prodotto", "Nome", "Descrizione", "Prezzo", "Luogo di Provenienza",
                "Data di raccolta", "Data di mungitura", "Glutine", "Data di Scadenza", "Categoria", "Scorta"}));
    }
}
