package DAOImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DAOInterface.ClienteJDBC;
import Model.Cliente;
import Model.Tessera;

public class Clienteimpl implements ClienteJDBC {
    private Connection connection;
    private PreparedStatement setNewCt, cercaCl, updateCl;
    private Statement getAllCt, idCl;
    // Costruttore
    public Clienteimpl(Connection connection) throws SQLException {
        this.connection = connection;
        getAllCt = connection.createStatement();
        cercaCl = connection.prepareStatement(
                "SELECT codcliente FROM cliente WHERE nome = ? AND cognome = ? AND codicefiscale = ?");
        setNewCt = connection.prepareStatement("INSERT INTO cliente VALUES (nextval('SCodCliente'), ?, ?, ?, ?, ?, ?)");
        updateCl = connection.prepareStatement(
                "UPDATE cliente SET nome = ?, cognome = ?, codicefiscale = ?, indirizzo = ?, telefono = ?, email = ? WHERE codcliente = ?");
        idCl = connection.createStatement();
    }

    @Override
    public boolean setNewCt(Cliente cliente) throws SQLException {
        // Evita la duplicazione di codice, popola la query con i dati del cliente
        setPreparedStatement(setNewCt, cliente);
        return setNewCt.executeUpdate() > 0;
    }

    @Override
    public ArrayList<Cliente> getAllCt() throws SQLException {
        ArrayList<Cliente> clienti = new ArrayList<>();
        try (ResultSet rs = getAllCt.executeQuery(
                "SELECT * FROM tessera AS T JOIN cliente AS C ON T.codcliente = C.codcliente ORDER BY C.cognome DESC")) {
            while (rs.next()) {
                clienti.add(new Cliente(
                        rs.getString("codcliente"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codicefiscale"),
                        rs.getString("email"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"),
                        new Tessera(rs.getString("codtessera"), rs.getInt("numeropunti"), null),
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
				return rs.getString("codcliente");
			} else {
				return null;
			}
        }
    }

    @Override
    public boolean updateCliente(Cliente cliente) throws SQLException {
        setPreparedStatement(updateCl, cliente);
        updateCl.setString(7, cliente.getCodCl());
        return updateCl.executeUpdate() > 0;
    }

    @Override
    public String getIdCt(String codicefiscale) throws SQLException {
        try (ResultSet rs = idCl.executeQuery("SELECT codcliente FROM cliente WHERE codicefiscale = '" + codicefiscale + "'")) {
            return rs.next() ? rs.getString("codcliente") : null;
        }
    }

    @Override
    public Cliente getCtByid(String idCt) throws SQLException {
        try (ResultSet rs = idCl.executeQuery("SELECT nome, cognome FROM cliente WHERE codcliente = '" + idCt + "'")) {
            return rs.next() ? new Cliente(null, rs.getString("nome"), rs.getString("cognome"), null, null, null, null, null, null) : null;
        }
    }

    // Metodo di supporto per evitare codice ripetitivo nella preparazione delle query
    private void setPreparedStatement(PreparedStatement ps, Cliente cliente) throws SQLException {
        ps.setString(1, cliente.getNome());
        ps.setString(2, cliente.getCognome());
        ps.setString(3, cliente.getCodFis());
        ps.setString(4, cliente.getInd());
        ps.setString(5, cliente.getTel());
        ps.setString(6, cliente.getEmail());
    }
}

