package GUI;

import DAO.*;
import DBConfig.*;
import DBEntities.*;
import DAOImplementations.*;
import Exceptions.ConnectionException;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;


public class Controller {

    private FrameAggiungiDipendenti addDipendenteFrame;
    private FrameAggiungiProdotti addProdottiFrame;
    private FrameModificaDipendenti changeDipendenteFrame;
    private FrameModificaProdotti changeProdottoFrame;
    private Login loginFrame;
    private FrameGestioneProdotti prodottiFrame;
    private FrameGestioneDipendenti dipendentiFrame;
    private FrameAdminHome adminFrame;
    private FrameUserHome userFrame;
    private ClienteDAO clDAO = null;
    private DipendenteDAO dpDAO = null;
    private TesseraDAO tsDAO = null;
    private OrdineDAO orDAO = null;
    private ProdottoDAO prDAO = null;
    private DBConnection dbconn = null;
    private Connection connection = null;


    //Getter per ottenere quali finestre richiamano il metodo backFrame()

    public FrameGestioneProdotti getProdottiFrame() {
        return prodottiFrame;
    }
    public FrameModificaProdotti getChangeProdottoFrame() { return changeProdottoFrame; }
    public FrameAggiungiDipendenti getAddDipendenteFrame() {
        return addDipendenteFrame;
    }
    public FrameAggiungiProdotti getAddProdottiFrame() {
        return addProdottiFrame;
    }
    public FrameModificaDipendenti getChangeDipendenteFrame() { return changeDipendenteFrame; }
    public FrameGestioneDipendenti getDipendentiFrame() {
        return dipendentiFrame;
    }
    public FrameAdminHome getAdminFrame() {
        return adminFrame;
    }


    public Connection getConnection() {
        return connection;
    }

    public Controller() throws SQLException {
        loginFrame = new Login("Login", this);
        loginFrame.setVisible(true);
        adminFrame = new FrameAdminHome("Home", this);
        dipendentiFrame = new FrameGestioneDipendenti("Personale", this);
        prodottiFrame = new FrameGestioneProdotti("Articoli", this);
    }

    public static void main(String[] args) throws SQLException {
        Controller c = new Controller();
    }

    //Funzione che inizializza la connessione al database e esegue i vari script contenuti nella classe DBBuilder

    public void startConnection() {
        try {
            dbconn = DBConnection.getInstance("postgres");
            connection = dbconn.getConnection();
            DBBuilder builder = new DBBuilder(connection);
            //Metodi per la definizione del DB:
            builder.createTableCLIENTE();
            builder.createTableDIPENDENTE();
            builder.createTableTESSERA();
            builder.createTableORDINE();
            builder.createTypeTIPOLOGIA();
            builder.createTablePRODOTTO();
            builder.createTableARTICOLIORDINE();
            builder.createSequences();
            builder.createTriggerUpdateScorta();
            builder.createFunctionUpdateScorta();
            builder.createTriggerSelectPrezzo();
            builder.createFunctionSelectPrezzo();
            builder.createTriggerUpdatePrezzo();
            builder.createFunctionUpdatePrezzo();
            builder.createTriggerUpdateDeletePrezzo();
            builder.createFunctionUpdateDeletePrezzo();
            builder.createTriggerCheckScorta();
            builder.createFunctionCheckScorta();
            builder.createTriggerUpdatePunti();
            builder.createFunctionUpdatePunti();
            /*builder.createTriggerRestoreScorta();
            builder.createFunctionRestoreScorta();
            builder.createTriggerUpdateDeletePunti();
            builder.createFunctionUpdateDeletePunti();
            */
            clDAO = new ClienteDAOPostgreImpl(connection);
            dpDAO = new DipendenteDAOPostgreImpl(connection);
            tsDAO = new TesseraDAOPostgreImpl(connection);
            orDAO = new OrdineDAOPostgreImpl(connection);
            prDAO = new ProdottoDAOPostgreImpl(connection);
        }
        catch (SQLException | ConnectionException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }
    //Funzione che controlla lo username di un dipendente

    public boolean checkID(String ID) throws SQLException {
        return (dpDAO.checkDipendenteID(ID));
    }

    //Funzione che permette l'accesso al software dopo un login corretto

    public void login(String user) throws SQLException {
        if (user.equals("Gestore")) {
            loginFrame.setVisible(false);
            //adminFrame.setVisible(true);
            //dipendentiFrame = new FrameGestioneDipendenti("Personale", this);
            //dipendentiFrame.setVisible(true);
            //adminFrame = new FrameAdminHome("Home", this);
            adminFrame.setVisible(true);
        }
        if (user.equals("Dipendente")) {
            //prodottiFrame = new FrameGestioneProdotti("Articoli", this);
            prodottiFrame.setVisible(true);
            //fillProductTable();
        }
    }

    //Funzione che riporta il software alla schermata di login

    public void logout(JFrame frame) {
            frame.setVisible(false);
            loginFrame.clearFields();
            loginFrame.setVisible(true);
    }

    /* PROBABILMENTE DA RIMUOVERE
    public ArrayList<Prodotto> fillProductTable() {
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
        prodotti = prDAO.getAllProdotti();
        for (int i = 0; i < prodotti.size(); i++)
            System.out.println(prodotti.get(i).toString());
        //return(prDAO.getAllProdotti());
        return null;
        }
   */


    //Funzioni che mostrano le finestre corrispondenti

    public void showFrameGestioneProdotti() {
        adminFrame.setVisible(false);
        prodottiFrame.setVisible(true);
    }

    public void showAddDipendenteFrame() throws SQLException {
        addDipendenteFrame = new FrameAggiungiDipendenti("Aggiungi Dipendente", this);
        System.out.println(addDipendenteFrame);
        addDipendenteFrame.setVisible(true);
    }
    public void showFrameAggiungiProdotti() {
        addProdottiFrame = new FrameAggiungiProdotti("Aggiungi Prodotto", this);
        addProdottiFrame.setVisible(true);
    }
    public void showChangeDipendenteFrame(String c, String n, String ln, String cf, String a, String p, String e) throws SQLException {
        changeDipendenteFrame = new FrameModificaDipendenti("Modifica Dipendente", this, c, n, ln, cf, a, p, e);
        changeDipendenteFrame.setVisible(true);
    }
    public void showDipendentiFrame() {
        adminFrame.setVisible(false);
        dipendentiFrame.setVisible(true);
    }

    public void showchangeProdottoFrame(String c, String n, String d, float pr, String pl,
                                         java.util.Date cd, java.util.Date md, Boolean g, java.util.Date ed,
                                         String cg, int s) {
        changeProdottoFrame = new FrameModificaProdotti("Modifica Prodotto", this, c, n, d, pr, pl, cd, md, g, ed, cg, s);
        changeProdottoFrame.setVisible(true);
    }


    //Funzione che permette di tornare alla finestra precedente tramite il tasto indietro

    public void backFrame(JFrame frame, JFrame roleFrame) {
        frame.setVisible(false);
        roleFrame.setVisible(true);
    }

    //Funzione che chiude la finestra corrente

    public void closeFrame(JFrame frame) {
        frame.setVisible(false);
    }


    //Funzione che recupera la lista delle province

    public ArrayList<String> getProvince() throws SQLException {
        Statement getProvince;
        ArrayList<String> province = new ArrayList<String>();
        getProvince = connection.createStatement();
        ResultSet rs = getProvince.executeQuery("SELECT DISTINCT Provincia FROM COMUNI ORDER BY Provincia ASC");
        while(rs.next())
            province.add(rs.getString("Provincia"));
        return province;
    }

    //Funzione che in base alla provincia selezionata restituisce i comuni dal DB (funzionale alla generazione del codice fiscale)

    public ArrayList<String> getComuniByProvincia(String provincia) throws SQLException {
        Statement getComuni;
        ArrayList<String> comuni = new ArrayList<String>();
        getComuni = connection.createStatement();
        ResultSet rs = getComuni.executeQuery("SELECT Comune FROM COMUNI WHERE Provincia = '" + provincia +"'");
        while (rs.next())
            comuni.add(rs.getString("Comune"));
        return comuni;
    }

    //Funzione che in base al comune selezionato restituisce il relativo codice catastale dal DB (funzionale alla generazione del codice fiscale)

    public String getCodiceCatastaleByComune(String comune) throws SQLException {
        Statement getCodiceCatastale;
        String codice = null;
        getCodiceCatastale = connection.createStatement();
        ResultSet rs = getCodiceCatastale.executeQuery("SELECT CodiceCatastale FROM Comuni WHERE Comune = '" + comune + "';");
        while (rs.next())
            codice = rs.getString("CodiceCatastale");
        return(codice);
    }

    //Funzioni per interagire col DB

    public boolean addDipendente(Dipendente d) throws SQLException {
        return(dpDAO.insertDipendente(d));
    }

    public boolean addProdotto(Prodotto p) throws SQLException {
        return(prDAO.insertProdotto(p));
    }

    public boolean changeDipendente(String id, String n, String ln, String cf, String a, String p, String e) throws SQLException {
        return (dpDAO.updateDipendente(id, n, ln, cf, a, p, e));
    }

    public boolean changeProdotto(String id, String n, String d, float pr, String pl, Date cd, Date md, Boolean g, Date ed, String c, int s) throws SQLException {
        return (prDAO.updateProdotto(id, n, d, pr, pl, cd, md, g, ed, c, s));
    }
}
