package daoimplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Cliente;
import daointerface.ClienteJDBC;

public class Clienteimpl implements ClienteJDBC {
    private static final String CODCLIENTE = "codcliente"; // Define constant for "codcliente"
    private PreparedStatement setNewCt;
    private PreparedStatement cercaCl;
    private PreparedStatement updateCl;
    private Statement getAllCt;
    private Statement idCl;

    // Costruttore
    public Clienteimpl(Connection connection) throws SQLException {
        getAllCt = connection.createStatement();
        cercaCl = connection.prepareStatement(
                "SELECT codcliente FROM cliente WHERE nome = ? AND cognome = ? AND codicefiscale = ?");
        setNewCt = connection.prepareStatement("INSERT INTO cliente (nome, cognome, codicefiscale, indirizzo, telefono, email) VALUES (?, ?, ?, ?, ?, ?)");
        updateCl = connection.prepareStatement(
                "UPDATE cliente SET nome = ?, cognome = ?, codicefiscale = ?, indirizzo = ?, telefono = ?, email = ? WHERE codcliente = ?");
        idCl = connection.createStatement();
    }

    @Override
    public boolean setNewCt(Cliente cliente) throws SQLException {
        setPreparedStatement(setNewCt, cliente);
        return setNewCt.executeUpdate() > 0;
    }

    @Override
    public ArrayList<Cliente> getAllCt() throws SQLException {
        ArrayList<Cliente> clienti = new ArrayList<>();
        try (ResultSet rs = getAllCt.executeQuery("SELECT codcliente, nome, cognome, codicefiscale, email, indirizzo, telefono FROM cliente ORDER BY cognome DESC")) {
            while (rs.next()) {
                clienti.add(new Cliente(
                        rs.getString(CODCLIENTE),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codicefiscale"),
                        rs.getString("email"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"),
                        null,
                        null));
            }
        }
        return clienti;
    }

    @Override
    public String getCtByNCCF(String nome, String cognome, String codicefiscale) throws SQLException {
        cercaCl.setString(1, nome);
        cercaCl.setString(2, cognome);
        cercaCl.setString(3, codicefiscale);
        try (ResultSet rs = cercaCl.executeQuery()) {
            if (rs.next()) {
                return rs.getString(CODCLIENTE); 
            }
        }
        return null;
    }

    @Override
    public boolean updateCliente(Cliente cliente) throws SQLException {
        setPreparedStatement(updateCl, cliente);
        updateCl.setString(7, cliente.getCodCl());
        return updateCl.executeUpdate() > 0;
    }

    @Override
    public String getIdCt(String codicefiscale) throws SQLException {
        try (ResultSet rs = idCl.executeQuery("SELECT " + CODCLIENTE + " FROM cliente WHERE codicefiscale = '" + codicefiscale + "'")) {
            if (rs.next()) {
                return rs.getString(CODCLIENTE); // Use constant
            }
        }
        return null;
    }

    @Override
    public Cliente getCtByid(String idCt) throws SQLException {
        try (ResultSet rs = idCl.executeQuery("SELECT nome, cognome FROM cliente WHERE " + CODCLIENTE + " = '" + idCt + "'")) {
            if (rs.next()) {
                return new Cliente(null, rs.getString("nome"), rs.getString("cognome"), null, null, null, null, null, null);
            }
        }
        return null;
    }

    private void setPreparedStatement(PreparedStatement ps, Cliente cliente) throws SQLException {
        ps.setString(1, cliente.getNome());
        ps.setString(2, cliente.getCognome());
        ps.setString(3, cliente.getCodFis());
        ps.setString(4, cliente.getInd());
        ps.setString(5, cliente.getTel());
        ps.setString(6, cliente.getEmail());
    }
}

