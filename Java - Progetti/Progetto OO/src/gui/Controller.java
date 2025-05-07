package gui;

import java.awt.EventQueue;
import java.awt.Frame;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import daoimplementation.ArticoliImpl;
import daoimplementation.Clienteimpl;
import daoimplementation.DipendenteImpl;
import daoimplementation.OrdiniImpl;
import daoimplementation.ProdottoImpl;
import daoimplementation.Tesseraimpl;
import daointerface.ArticoliJDBC;
import daointerface.ClienteJDBC;
import daointerface.DipendenteJDBC;
import daointerface.OrdiniJDBC;
import daointerface.ProdottoJDBC;
import daointerface.TesseraJDBC;
import dbconfiguration.ConnectionException;
import dbconfiguration.DBConfiguration;
import dbconfiguration.DBConnection;
import model.Articoli;
import model.Cliente;
import model.Dipendente;
import model.ImagePanel;
import model.Ordine;
import model.Prodotto;

import java.awt.Image;

public class Controller {
    // Dichiarazione dei frame
    private LoginFrame logf;
    private NuovoProdottoFrame nprodf;
    private AdminFrame adminf;
    private DipendenteFrame dipf;
    private VisioneDipendentiFrame vdipf;
    private VisioneProdottiFrame vprodf;
    public ModificaProdottiFrame modprodf;
    private StatisticheDipendentiFrame statdipf;
    private CarrelloFrame carrf;
    private VisioneClienteFrame visctf;
    private NuovoDipendenteFrame ndipf;
    public ModificaDipendenteFrame updipf;
    private NuovoClienteFrame nclf;
    public ModificaClienteFrame upclf;
    private VisioneOrdineFrame visordf;
    private RicercaFrame searchf;
    private DBConnection dbconn;
    private DBConfiguration config = null;
    private Connection connection = null;
    private ClienteJDBC cljdbc = null;
    private DipendenteJDBC dpjdbc = null;
    private ProdottoJDBC prdjdbc = null;
    private OrdiniJDBC ordjdbc = null;
    private TesseraJDBC tsjdbc = null;
    private ArticoliJDBC artjdbc = null;
    public DefaultTableModel clienteModel = new DefaultTableModel(new Object[]{"Id Cliente", "Nome", "Cognome", "Codice fiscale", "Email", "Indirizzo", "Telefono", "Id Tessera", "Punti"}, 0);
    public DefaultTableModel dipModel = new DefaultTableModel(new Object[]{"Id", "Nome", "Cognome", "Codice fiscale", "Email", "Indirizzo", "Telefono"}, 0);
    public DefaultTableModel prodModel = new DefaultTableModel(new Object[]{"Id", "Nome", "Descrizione", "Prezzo", "Provenienza", "Raccolta", "Mungitura", "Glutine", "Scadenza", "Categoria", "Scorta"}, 0);
    public DefaultTableModel ordModel = new DefaultTableModel(new Object[]{"Id Ordine", "Data", "Prezzo Totale", "Cliente", "Dipendente"}, 0);
    public String iddip;
    private Frame lastFrame; // Variabile per tenere traccia dell'ultimo frame

    public Controller() throws SQLException, IOException {
        // Inizializzazione dei frame
        logf = new LoginFrame("Green Market Point - Login", this);
        adminf = new AdminFrame("Green Market Point - Admin Point", this);
        dipf = new DipendenteFrame("Green Market Point - Dipendente Point", this);
        nprodf = new NuovoProdottoFrame("Green Market Point - Nuovo Prodotto", this);
        ndipf = new NuovoDipendenteFrame("Green Market Point - Nuovo Dipendente", this);
        nclf = new NuovoClienteFrame("Green Market Point - Nuovo Cliente", this);
        vdipf = new VisioneDipendentiFrame("Green Market Point - Gestione Dipendenti", this);
        vprodf = new VisioneProdottiFrame("Green Market Point - Gestione Prodotti", this);
        visctf = new VisioneClienteFrame("Green Market Point - Gestione Clienti", this);
        upclf = new ModificaClienteFrame("Green Market Point - Modifica Cliente", this);
        updipf = new ModificaDipendenteFrame("Green Market Point - Modifica Dipendente", this);
        modprodf = new ModificaProdottiFrame("Green Market Point - Modifica Prodotti", this);
        statdipf = new StatisticheDipendentiFrame("Green Market Point - Statistiche Dipendenti", this);
        carrf = new CarrelloFrame("Green Market Point - Carrello", this);
        visordf = new VisioneOrdineFrame("Green Market Point - Visione Ordini", this);
        searchf = new RicercaFrame("Green Market Point - Ricerca Clienti", this);
        logf.setVisible(true);
    }

    // Crea un pannello con un'immagine
    public JPanel createImagePanel(String imagePath) {
        Image image = new ImageIcon(ImagePanel.class.getResource(imagePath)).getImage();
        return new ImagePanel(image);
    }

    // Torna all'ultimo frame visibile
    public void returnToLastFrame() {
        setVisibleFrame(lastFrame, searchf, visordf, vprodf, vdipf, statdipf, visctf, carrf, nprodf, ndipf, nclf, modprodf, upclf, updipf);
    }

    // Gestisce la visibilità dei frame
    private void setVisibleFrame(Frame toShow, Frame... toHide) {
        for (Frame frame : toHide) {
            if (frame != null) {
                frame.setVisible(false);
            }
        }
        if (toShow != null) {
            toShow.setVisible(true);
        }
    }

    // Cambia il frame visibile in base al tipo di logout
    public void logout(int x) {
        setVisibleFrame(logf, (x == 1) ? adminf : dipf);
    }

    // Mostra il frame appropriato in base al tipo di utente
    public void logtoutente(int x) {
        logf.setVisible(false);
        setVisibleFrame((x == 1) ? adminf : dipf, (x == 1) ? dipf : adminf);
    }

    // Gestisce la visibilità dei frame per l'admin
    public void adminAndElem(int x) {
        lastFrame = adminf; // Imposta il frame admin come ultimo frame
        switch (x) {
            case 1 -> setVisibleFrame(vdipf, adminf, vprodf, statdipf, visordf); // Mostra Visione Dipendenti
            case 2 -> setVisibleFrame(vprodf, adminf, vdipf, statdipf, visordf); // Mostra Visione Prodotti
            case 3 -> setVisibleFrame(statdipf, adminf, vdipf, vprodf, visordf); // Mostra Statistiche
            case 4 -> setVisibleFrame(visordf, adminf, vdipf, vprodf, statdipf); // Mostra Visione Ordini
            case 5 -> returnToLastFrame(); // Torna all'ultimo frame salvato
            case 6 -> setVisibleFrame(searchf, adminf, dipf); // Mostra ricerca
            default -> throw new IllegalArgumentException("Valore di x non valido: " + x);
        }
    }

    public void dipAndElem(int x) {
        lastFrame = dipf; // Imposta il frame dipendente come ultimo frame
        switch (x) {
            case 1 -> setVisibleFrame(visctf, dipf); // Mostra Visione Clienti
            case 2 -> setVisibleFrame(vprodf, dipf); // Mostra Visione Prodotti
            case 3 -> setVisibleFrame(visordf, dipf); // Mostra Visione Ordini
            case 4 -> returnToLastFrame(); // Torna all'ultimo frame salvato
            case 5 -> setVisibleFrame(searchf, dipf, adminf); // Mostra ricerca
            default -> throw new IllegalArgumentException("Valore di x non valido: " + x);
        }
    }

    public void visAndElem(int context, int x) {
        switch (context) {
            case 1: // Carrello
                switch (x) {
                    case 1 -> setVisibleFrame(carrf, visordf); // Mostra il frame carrello
                    case 2 -> setVisibleFrame(visordf, carrf); // Mostra il frame ordine e nasconde carrello
                }
                break;

            case 2: // Dipendente
                switch (x) {
                    case 1 -> setVisibleFrame(ndipf, vdipf, updipf); // Nuovo dipendente
                    case 2 -> setVisibleFrame(updipf, vdipf, ndipf); // Modifica dipendente
                    case 3 -> setVisibleFrame(vdipf, ndipf, updipf); // Vista dipendenti
                }
                break;

            case 3: // Cliente
                switch (x) {
                    case 1 -> setVisibleFrame(nclf, visctf); // Nuovo cliente
                    case 2 -> setVisibleFrame(upclf, visctf); // Modifica cliente
                    case 3 -> setVisibleFrame(visctf, nclf, upclf); // Vista clienti
                }
                break;

            case 4: // Prodotto
                switch (x) {
                    case 1 -> setVisibleFrame(nprodf, vprodf); // Nuovo prodotto
                    case 2 -> setVisibleFrame(modprodf, vprodf); // Modifica prodotto
                    case 3 -> setVisibleFrame(vprodf, nprodf, modprodf); // Vista prodotti
                }
                break;

            default:
                break;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Controller();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Metodo generico per popolare il modello della tabella
    private <T> void populateTable(List<T> items, DefaultTableModel model, Function<T, Object[]> mapper) {
        model.setRowCount(0);
        items.forEach(item -> model.addRow(mapper.apply(item)));
    }

    // Metodo di supporto per verificare se un campo è nullo o vuoto
    private String checkNull(Object value) {
        return (value == null || value.toString().trim().isEmpty()) ? "N/A" : value.toString();
    }

    public void connect() throws SQLException {
        try {
            dbconn = DBConnection.getInstance();
            connection = dbconn.getConnection();
            config = new DBConfiguration(connection);
            // Metodi per la definizione del DB:
            config.createTipologie();
            config.createTableCliente();
            config.createTableDipendente();
            config.createTableOrdine();
            config.createTableProdotto();
            config.createTableTessera();
            config.createTableArticoliOrdine();
            //config.FromatTables();
            config.populateDatabase();
            cljdbc = new Clienteimpl(connection);
            dpjdbc = new DipendenteImpl(connection);
            prdjdbc = new ProdottoImpl(connection);
            ordjdbc = new OrdiniImpl(connection);
            tsjdbc = new Tesseraimpl(connection);
            artjdbc = new ArticoliImpl(connection);
        } catch (SQLException | ConnectionException e) {
            e.printStackTrace();
        }
    }

    // Verifica se l'ID è presente nel database
    public boolean verifyid(String ID) throws SQLException {
        return dpjdbc.verifyID(ID);
    }

    // Aggiunge un nuovo dipendente al database
    public boolean newdip(Dipendente dip) throws SQLException {
        return dpjdbc.setNewDip(dip);
    }

    // Aggiunge un nuovo cliente al database
    public boolean newclt(Cliente ct) throws SQLException {
        return cljdbc.setNewCt(ct);
    }

    // Aggiunge un nuovo prodotto al database
    public boolean newprod(Prodotto pe) throws SQLException {
        return prdjdbc.setNewProdotto(pe);
    }

    // Restituisce i punti associati a un codice tessera
    public String punti(String codt) throws SQLException {
        return tsjdbc.getpuntit(codt);
    }

    // Restituisce l'introito dei dipendenti per un intervallo di date
    public List<String> introitidip(Date di, Date df) throws SQLException {
        return dpjdbc.getDipIntroiti(di, df);
    }

    // Restituisce le vendite dei dipendenti per un intervallo di date
    public List<String> venditedip(Date di, Date df) throws SQLException {
        return dpjdbc.getDipVendite(di, df);
    }

    // Aggiunge una nuova tessera associata a un cliente
    public boolean nuovatessera(String a, String b, String c) throws SQLException {
        // Recupera il cliente con i dati forniti e crea una nuova tessera
        return tsjdbc.newtessera(cljdbc.getCtByNCCF(a, b, c));
    }

    // Aggiorna le informazioni di un prodotto
    public boolean upprod(Prodotto pe) throws SQLException {
        return prdjdbc.updateProdotto(pe);
    }

    // Aggiorna le informazioni di un dipendente
    public boolean updip(Dipendente de) throws SQLException {
        return dpjdbc.updatedipendente(de);
    }

    // Aggiorna le informazioni di un cliente
    public boolean upcliente(Cliente ce) throws SQLException {
        return cljdbc.updateCliente(ce);
    }

    // Restituisce l'ID del cliente associato a un codice fiscale
    public String getct(String codfisc) throws SQLException {
        return cljdbc.getIdCt(codfisc);
    }

    // Aggiorna le scorte di un prodotto
    public boolean upscorte(int x, String s) throws SQLException {
        return prdjdbc.updateScorte(x, s);
    }

    // Aggiorna i punti associati a un cliente
    public boolean uppunti(String codcl, double d) throws SQLException {
        return tsjdbc.updatepunti(codcl, d);
    }

    // Aggiunge un nuovo ordine al database
    public boolean nuovoordine(Ordine ordine) throws SQLException {
        return ordjdbc.newordine(ordine);
    }

    // Aggiunge nuovi articoli a un ordine
    public boolean newarticoli(Articoli articoli) throws SQLException {
        return artjdbc.newordine(articoli);
    }

    // Restituisce la data dell'ultimo ordine
    public String OldDate() throws SQLException {
        return ordjdbc.getOldDate();
    }

    // Restituisce il codice dell'ordine corrente
    public String CurrOrd() throws SQLException {
        return ordjdbc.getCurrentCod();
    }

    // Popola il modello della tabella con i dati dei clienti
    public void ClientSearch(DefaultTableModel model) throws SQLException {
        List<Cliente> clienti = artjdbc.searchClient();
        populateTable(clienti, model, c -> new Object[]{
                c.getNome(),
                c.getCognome(),
                c.getArticoliOrdini().getCategoria(),
                c.getArticoliOrdini().getNumPunti()
        });
    }

    // Popola il modello della tabella con i prodotti per categoria
    public void categoriaprodotti(String c, DefaultTableModel model) throws SQLException {
        List<Prodotto> prodotti = prdjdbc.getbycategoria(c);
        populateTable(prodotti, model, p -> new Object[]{
                p.getCodProd(),
                p.getNome(),
                p.getPrezzo(),
                p.getCategoria(),
                p.getScorta()
        });
    }

    public void allCliente() throws SQLException {
        List<Cliente> clienti = cljdbc.getAllCt();
        populateTable(clienti, clienteModel, c -> new Object[]{
            checkNull(c.getCodCl()),
            checkNull(c.getNome()),
            checkNull(c.getCognome()),
            checkNull(c.getCodFis()),
            checkNull(c.getEmail()),
            checkNull(c.getInd()),
            checkNull(c.getTel()),
            checkNull(c.getTessera() != null ? c.getTessera().getCodTessera() : null),
            checkNull(c.getTessera() != null ? c.getTessera().getNPunti() : null)
        });
    }

    public void allDipendenti() throws SQLException {
        List<Dipendente> dipendenti = dpjdbc.getAllDip();
        populateTable(dipendenti, dipModel, d -> new Object[]{
            checkNull(d.getCodDIP()),
            checkNull(d.getNome()),
            checkNull(d.getCognome()),
            checkNull(d.getCodFis()),
            checkNull(d.getEmail()),
            checkNull(d.getInd()),
            checkNull(d.getTel())
        });
    }

    public void allOrdini() throws SQLException {
        List<Ordine> ordini = ordjdbc.getallordini();
        populateTable(ordini, ordModel, o -> {
            String clienteNome = "N/A";
            String dipendenteNome = "N/A";

            try {
                Dipendente d = dpjdbc.getOneDip(String.valueOf(o.getIdDipendente()));
                Cliente ct = cljdbc.getCtByid(String.valueOf(o.getIdCliente()));
                dipendenteNome = checkNull(d != null ? d.getCognome() + " " + d.getNome() : null);
                clienteNome = checkNull(ct != null ? ct.getCognome() + " " + ct.getNome() : null);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return new Object[]{
                checkNull(o.getCodOrd()),
                checkNull(o.getDataAcquisto()),
                checkNull(o.getPrezzoTotale()),
                clienteNome,
                dipendenteNome
            };
        });
    }

    public void allProdotti() throws SQLException {
        List<Prodotto> prodotti = prdjdbc.getallprodotti();
        populateTable(prodotti, prodModel, p -> {
            String glutenStatus = p.isGlutine() ? "Si" : "No";

            return new Object[]{
                checkNull(p.getCodProd()),
                checkNull(p.getNome()),
                checkNull(p.getDescrizione()),
                checkNull(p.getPrezzo()),
                checkNull(p.getLuogoProv()),
                checkNull(p.getDataraccolta()),
                checkNull(p.getDatamungitura()),
                glutenStatus,
                checkNull(p.getDatascadenza()),
                checkNull(p.getCategoria()),
                checkNull(p.getScorta())
            };
        });
    }
}