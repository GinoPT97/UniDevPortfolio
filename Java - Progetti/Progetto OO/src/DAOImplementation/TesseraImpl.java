package DAOImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DAOInterface.TesseraJDBC;
import Model.Cliente;
import Model.Tessera;

public class TesseraImpl implements TesseraJDBC {

    private PreparedStatement newtesseraStmt;
    private PreparedStatement getpuntitStmt;
    private PreparedStatement alltesseraStmt;
    private PreparedStatement uppuntiStmt;

    public TesseraImpl(Connection connection) throws SQLException {
        // Preparazione delle query
        newtesseraStmt = connection.prepareStatement("INSERT INTO tessera (codtessera, numeropunti, codcliente) VALUES (SCodTessera.NEXTVAL, ?, ?)");
        getpuntitStmt = connection.prepareStatement("SELECT numeropunti FROM tessera WHERE SCodTessera = ?");
        alltesseraStmt = connection.prepareStatement("SELECT * FROM tessera AS T JOIN cliente AS C ON T.codcliente = C.codcliente ORDER BY C.cognome DESC");
        uppuntiStmt = connection.prepareStatement("UPDATE tessera SET numeropunti = numeropunti + ? WHERE codcliente = ?");
    }

    @Override
    public boolean newtessera(String codcl) throws SQLException {
        newtesseraStmt.setDouble(1, 0.00);  // Tessera inizialmente con 0 punti
        newtesseraStmt.setString(2, codcl); // Codice cliente associato
        return newtesseraStmt.executeUpdate() > 0;  // Restituisce true se è stata inserita una riga
    }

    @Override
    public String getpuntit(String codt) throws SQLException {
        getpuntitStmt.setString(1, codt);
        try (ResultSet rs = getpuntitStmt.executeQuery()) {
            if (rs.next()) {
                return String.valueOf(rs.getDouble("numeropunti"));  // Converto il valore in String
            }
        }
        return null;  // Se non ci sono risultati
    }

    @Override
    public ArrayList<Tessera> alltessera() throws SQLException {
        ArrayList<Tessera> tessere = new ArrayList<>();
        try (ResultSet rs = alltesseraStmt.executeQuery()) {
            while (rs.next()) {
                // Creo e aggiungo una nuova Tessera all'elenco
                tessere.add(new Tessera(
                    rs.getString("codtessera"),
                    rs.getInt("numeropunti"),
                    new Cliente(
                        null,
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codicefiscale"),
                        rs.getString("email"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono"),
                        null,
                        null
                    )
                ));
            }
        }
        return tessere;
    }

    @Override
    public boolean updatepunti(String codcl, double x) throws SQLException {
        double punti = (x * 10) / 100; // Calcolo dei punti in base al 10% dell'importo x
        uppuntiStmt.setFloat(1, (float) punti); // Imposto i punti da aggiungere
        uppuntiStmt.setString(2, codcl); // Imposto il codice cliente
        return uppuntiStmt.executeUpdate() > 0; // Restituisce true se l'aggiornamento è andato a buon fine
    }
}


