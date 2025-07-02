package daoimplementation;

import daointerface.ClienteJDBC;
import java.sql.*;
import java.util.ArrayList;
import model.Cliente;
import model.Tessera;

public class Clienteimpl implements ClienteJDBC {
    private static final String CODCLIENTE = "codcliente"; 
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
        String query = "SELECT c.codcliente, c.nome, c.cognome, c.codicefiscale, c.email, c.indirizzo, c.telefono, " +
                       "t.codtessera, t.numeropunti " +
                       "FROM cliente c " +
                       "LEFT JOIN tessera t ON c.codcliente = t.codcliente " +
                       "ORDER BY c.cognome DESC";
        
        try (ResultSet rs = getAllCt.executeQuery(query)) {
            while (rs.next()) {
                Tessera tessera = null;
                String codTessera = rs.getString("codtessera");
                if (codTessera != null) {
                    tessera = new Tessera(codTessera, rs.getInt("numeropunti"), null);
                }
                
                clienti.add(new Cliente(
                        rs.getString(CODCLIENTE),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codicefiscale"),
                        rs.getString("email"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"),
                        tessera,
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
        // Il codice cliente nel database è INTEGER, quindi convertiamo da String
        updateCl.setInt(7, Integer.parseInt(cliente.getCodCl()));
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

