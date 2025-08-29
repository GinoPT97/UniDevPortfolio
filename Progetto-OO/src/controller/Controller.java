package controller;

import java.awt.EventQueue;
import java.awt.Frame;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import dbconfiguration.DBConfiguration;
import dbconfiguration.DBConnection;
import gui.AdminFrame;
import gui.CarrelloFrame;
import gui.DipendenteFrame;
import gui.LoginFrame;
import gui.ModificaClienteFrame;
import gui.ModificaDipendenteFrame;
import gui.ModificaProdottiFrame;
import gui.NuovoClienteFrame;
import gui.NuovoDipendenteFrame;
import gui.NuovoProdottoFrame;
import gui.RicercaFrame;
import gui.StatisticheDipendentiFrame;
import gui.VisioneClienteFrame;
import gui.VisioneDipendentiFrame;
import gui.VisioneOrdineFrame;
import gui.VisioneProdottiFrame;
import model.Articoli;
import model.Cliente;
import model.Dipendente;
import model.Ordine;
import model.Prodotto;
import model.Tessera;

public class Controller {

    // Esempi di istanze modello per riferimento (non usati direttamente nei metodi)
    private Cliente cliente;
    private Dipendente dipendente;
    private Prodotto prodotto;
    private Ordine ordine;
    private Tessera tessera;
    private Articoli articoli;
    // Costanti per le colonne delle tabelle
    private static final String[] CLIENTE_COLUMNS = {"Id Cliente", "Nome", "Cognome", "Codice fiscale", "Email", "Indirizzo", "Telefono", "Id Tessera", "Punti", "Stato Tessera", "Scadenza"};
    private static final String[] DIPENDENTE_COLUMNS = {"Id", "Nome", "Cognome", "Codice fiscale", "Email", "Indirizzo", "Telefono"};
    private static final String[] PRODOTTO_COLUMNS = {"Id", "Nome", "Descrizione", "Prezzo", "Provenienza", "Raccolta", "Mungitura", "Glutine", "Scadenza", "Produzione", "Categoria", "Scorta"};
    private static final String[] ORDINE_COLUMNS = {"Id Ordine", "Data", "Prezzo Totale", "Cliente", "Dipendente"};

    // Costanti applicazione
    private static final String GLUTINE_SI = "Si";
    private static final String GLUTINE_NO = "No";
    private static final String NULL_VALUE = "N/A";

    // Frame references
    public ModificaProdottiFrame modprodf;
    public ModificaDipendenteFrame updipf;
    public ModificaClienteFrame upclf;

    // Classe helper per creare TableModel non editabili
    private static class ReadOnlyTableModel extends DefaultTableModel {
        public ReadOnlyTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Nessuna cella è editabile
        }
    }

    // Table models - Non editabili
    public DefaultTableModel clienteModel = new ReadOnlyTableModel(CLIENTE_COLUMNS, 0);
    public DefaultTableModel dipModel = new ReadOnlyTableModel(DIPENDENTE_COLUMNS, 0);
    public DefaultTableModel prodModel = new ReadOnlyTableModel(PRODOTTO_COLUMNS, 0);
    public DefaultTableModel ordModel = new ReadOnlyTableModel(ORDINE_COLUMNS, 0);

    public String iddip;
    // Dichiarazione dei frame
    private final LoginFrame logf;
    private final NuovoProdottoFrame nprodf;
    private final AdminFrame adminf;
    private final DipendenteFrame dipf;
    private final VisioneDipendentiFrame vdipf;
    private final VisioneProdottiFrame vprodf;
    private final StatisticheDipendentiFrame statdipf;
    private final CarrelloFrame carrf;
    private final VisioneClienteFrame visctf;
    private final NuovoDipendenteFrame ndipf;
    private final NuovoClienteFrame nclf;
    private final VisioneOrdineFrame visordf;
    private final RicercaFrame searchf;
    private DBConnection dbconn;
    private DBConfiguration config = null;
    private Connection connection = null;
    private ClienteJDBC cljdbc = null;
    private DipendenteJDBC dpjdbc = null;
    private ProdottoJDBC prdjdbc = null;
    private OrdiniJDBC ordjdbc = null;
    private TesseraJDBC tsjdbc = null;
    private ArticoliJDBC artjdbc = null;
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

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Controller();
            } catch (SQLException | IOException e) {
                System.err.println("Errore durante l'avvio dell'applicazione: " + e.getMessage());
            }
        });
    }

    // Torna all'ultimo frame visibile
    public void returnToLastFrame() {
        setVisibleFrame(lastFrame, searchf, visordf, vprodf, vdipf, statdipf, visctf, carrf, nprodf, ndipf, nclf, modprodf, upclf, updipf);
    }

    // Gestisce la visibilità dei frame
    private void setVisibleFrame(Frame toShow, Frame... toHide) {
        for (Frame frame : toHide)
			if (frame != null)
				frame.setVisible(false);
        if (toShow != null)
			toShow.setVisible(true);
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
            case 1 -> handleCarrelloContext(x);
            case 2 -> handleDipendenteContext(x);
            case 3 -> handleClienteContext(x);
            case 4 -> handleProdottoContext(x);
            default -> {
            }
        }
    }

    private void handleCarrelloContext(int x) {
        switch (x) {
            case 1 -> setVisibleFrame(carrf, visordf);
            case 2 -> setVisibleFrame(visordf, carrf);
            default -> {
            }
        }
    }

    private void handleDipendenteContext(int x) {
        switch (x) {
            case 1 -> setVisibleFrame(ndipf, vdipf, updipf);
            case 2 -> setVisibleFrame(updipf, vdipf, ndipf);
            case 3 -> setVisibleFrame(vdipf, ndipf, updipf);
            default -> {
            }
        }
    }

    private void handleClienteContext(int x) {
        switch (x) {
            case 1 -> setVisibleFrame(nclf, visctf);
            case 2 -> setVisibleFrame(upclf, visctf);
            case 3 -> setVisibleFrame(visctf, nclf, upclf);
            default -> {
            }
        }
    }

    private void handleProdottoContext(int x) {
        switch (x) {
            case 1 -> setVisibleFrame(nprodf, vprodf);
            case 2 -> setVisibleFrame(modprodf, vprodf);
            case 3 -> setVisibleFrame(vprodf, nprodf, modprodf);
            default -> {
            }
        }
    }

    // Metodo generico per popolare il modello della tabella
    private <T> void populateTable(List<T> items, DefaultTableModel model, Function<T, Object[]> mapper) {
        model.setRowCount(0);
        items.forEach(item -> model.addRow(mapper.apply(item)));
    }

    // Metodo di utilità per aggiornare righe nel modello della tabella
    private void updateTableRow(DefaultTableModel model, String searchValue, Object[] newValues) {
        for (int i = 0; i < model.getRowCount(); i++)
			if (model.getValueAt(i, 0).equals(searchValue)) {
                for (int j = 0; j < newValues.length; j++)
					model.setValueAt(newValues[j], i, j);
                break;
            }
    }

    // Metodo di utilità per creare array di oggetti con checkNull
    private Object[] createRowData(Object... values) {
        Object[] result = new Object[values.length];
        for (int i = 0; i < values.length; i++)
			result[i] = checkNull(values[i]);
        return result;
    }

    // Metodo di utilità per formattare lo status del glutine
    private String formatGlutineStatus(boolean glutine) {
        return glutine ? GLUTINE_SI : GLUTINE_NO;
    }

    public void addToCarrelloModel(Object[] articleData) {
        ordModel.addRow(articleData);
    }

    public void clearCarrelloModel() {
        ordModel.setRowCount(0);
    }

    // Metodo di supporto per verificare se un campo è nullo o vuoto
    private String checkNull(Object value) {
        return (value == null || value.toString().trim().isEmpty()) ? NULL_VALUE : value.toString();
    }

    // Metodo per connettersi al database
    public void connect() throws SQLException {
        try {
            dbconn = DBConnection.getInstance();
            connection = dbconn.getConnection();
            config = new DBConfiguration(connection);
            // Inizializzazione completa del database
            //config.formatTables();
            config.initializeCompleteDatabase();
            cljdbc = new Clienteimpl(connection);
            dpjdbc = new DipendenteImpl(connection);
            prdjdbc = new ProdottoImpl(connection);
            ordjdbc = new OrdiniImpl(connection);
            tsjdbc = new Tesseraimpl(connection);
            artjdbc = new ArticoliImpl(connection);
        } catch (SQLException | dbconfiguration.ConnectionException e) {
            System.err.println("Errore durante la connessione: " + e.getMessage());
        }
    }

    // Verifica se l'ID è presente nel database
    public boolean verifyid(String ID) throws SQLException {
        return dpjdbc.verifyID(ID);
    }

    // Aggiunge un nuovo dipendente al database
    public boolean newdip(String codDipendente, String nome, String cognome, String codFis, String email, String indirizzo, String telefono) throws SQLException {
        dipendente = new Dipendente(codDipendente, nome, cognome, codFis, email, indirizzo, telefono);
        boolean success = dpjdbc.setNewDip(dipendente);

        // Se l'inserimento nel database ha successo, aggiorna anche il model della tabella
        if (success)
			updateDipendenteModelAfterInsert(codDipendente, nome, cognome, codFis, email, indirizzo, telefono);

        return success;
    }

    // Aggiunge un nuovo cliente al database e crea automaticamente la tessera
    public boolean newclt(String codCliente, String nome, String cognome, String codFis, String email, String indirizzo, String telefono) throws SQLException {
        cliente = new Cliente(codCliente, nome, cognome, codFis, email, indirizzo, telefono, null, null);
        boolean clienteSuccess = cljdbc.setNewCt(cliente);

        if (clienteSuccess) {
            updateClienteModelAfterInsert(codCliente, nome, cognome, codFis, email, indirizzo, telefono);
            String codClienteDb = cljdbc.getIdCt(codFis); // Recupera solo tramite codice fiscale
            if (codClienteDb != null)
				try {
                    tsjdbc.newtessera(codClienteDb);
                } catch (SQLException e) {
                    System.err.println("Errore creazione tessera cliente: " + e.getMessage());
                }
        }
        return clienteSuccess;
    }

    // Aggiunge una nuova tessera associata a un cliente tramite codice fiscale
    public boolean nuovatessera(String codFisc) throws SQLException {
        tessera = new Tessera("", 0.0, java.time.LocalDate.now(), java.time.LocalDate.now().plusYears(1), "ATTIVA", cliente);
        return tsjdbc.newtessera(cljdbc.getIdCt(codFisc));
    }


    // Aggiunge un nuovo prodotto al database
    public boolean newprod(String codProdotto, String nome, String descrizione, double prezzo, String luogoProvenienza,
                          java.time.LocalDate dataRaccolta, java.time.LocalDate dataMungitura, boolean glutine, java.time.LocalDate dataScadenza, java.time.LocalDate dataproduzione, String categoria, int scorta) throws SQLException {
        prodotto = new Prodotto(codProdotto, nome, descrizione, prezzo, luogoProvenienza, dataRaccolta, dataMungitura, glutine, dataScadenza, categoria, scorta, dataproduzione);
        boolean success = prdjdbc.setNewProdotto(prodotto);
        // Se l'inserimento nel database ha successo, aggiorna anche il model della tabella
        if (success)
			updateProdottoModelAfterInsert(codProdotto, nome, descrizione, prezzo, luogoProvenienza,
                                         dataRaccolta, dataMungitura, glutine, dataScadenza, categoria, scorta);
        return success;
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
    // Aggiorna le informazioni di un prodotto
    public boolean upprod(String codProdotto, String nome, String descrizione, double prezzo, String luogoProvenienza,
                         java.time.LocalDate dataRaccolta, java.time.LocalDate dataMungitura, boolean glutine, java.time.LocalDate dataScadenza, String categoria, int scorta) throws SQLException {
        Prodotto pe = new Prodotto(
            codProdotto, nome, descrizione, prezzo, luogoProvenienza,
            dataRaccolta,
            dataMungitura,
            glutine,
            dataScadenza,
            categoria, scorta, null
        );
        boolean success = prdjdbc.updateProdotto(pe);
        if (success) updateProdottoModelAfterUpdate(codProdotto, nome, descrizione, prezzo, luogoProvenienza, dataRaccolta, dataMungitura, glutine, dataScadenza, categoria, scorta);
        return success;
    }

    // Aggiorna le informazioni di un dipendente
    public boolean updip(String codDipendente, String nome, String cognome, String codFis, String email, String indirizzo, String telefono) throws SQLException {
        boolean success = dpjdbc.updatedipendente(new Dipendente(codDipendente, nome, cognome, codFis, email, indirizzo, telefono));
        if (success) updateDipendenteModelAfterUpdate(codDipendente, nome, cognome, codFis, email, indirizzo, telefono);
        return success;
    }

    // Aggiorna le informazioni di un cliente
    public boolean upcliente(String codCliente, String nome, String cognome, String codFis, String email, String indirizzo, String telefono) throws SQLException {
        boolean success = cljdbc.updateCliente(new Cliente(codCliente, nome, cognome, codFis, email, indirizzo, telefono, null, null));
        if (success) updateClienteModelAfterUpdate(codCliente, nome, cognome, codFis, email, indirizzo, telefono);
        return success;
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
        boolean success = tsjdbc.updatepunti(codcl, d);
        if (success)
			for (int i = 0; i < clienteModel.getRowCount(); i++)
				if (clienteModel.getValueAt(i, 0).equals(codcl)) {
                    double punti = 0.0;
                    try { punti = Double.parseDouble(clienteModel.getValueAt(i, 8).toString()); } catch (Exception ignored) {}
                    clienteModel.setValueAt(punti + d, i, 8);
                    break;
                }
        return success;
    }

    // Aggiunge un nuovo ordine al database
    public boolean nuovoordine(String codOrdine, Date dataAcquisto, double prezzoTotale, int idCliente, int idDipendente) throws SQLException {
        ordine = new Ordine(codOrdine, dataAcquisto, prezzoTotale, idCliente, idDipendente);
        boolean success = ordjdbc.newordine(ordine);
        return success;
    }

    // Aggiunge nuovi articoli a un ordine
    public boolean newarticoli(String codOrdine, String codProdotto, double prezzo, int numeroArticoli) throws SQLException {
        articoli = new Articoli(codOrdine, codProdotto, prezzo, numeroArticoli, 0); // codCliente non necessario per articoliordine
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
        populateTable(clienti, model, c -> createClientSearchRowData(
                c.getCodCliente(), // Cod Cliente
                c.getNome(), // Nome
                c.getCognome(), // Cognome
                c.getArticoliOrdini() != null ? c.getArticoliOrdini().getCodProdotto() : null, // Categoria
                c.getArticoliOrdini() != null ? c.getArticoliOrdini().getPrezzo() : null, // Punti Categoria
                c.getArticoliOrdini() != null ? c.getArticoliOrdini().getCodCliente() : null, // Spesa Totale
                c.getArticoliOrdini() != null ? c.getArticoliOrdini().getNumeroArticoli() : null // Ordini Categoria
        ));
    }

    // Crea una riga di dati per la ricerca clienti mantenendo i tipi numerici necessari per il corretto funzionamento dei filtri di Swing
    private Object[] createClientSearchRowData(Object codCliente, Object nome, Object cognome,
                                             Object categoria, Object puntiCategoria, Object spesaTotale, Object ordiniCategoria) {
        return new Object[]{
            checkNull(codCliente),      // Colonna 0: Cod Cliente (String)
            checkNull(nome),            // Colonna 1: Nome (String)
            checkNull(cognome),         // Colonna 2: Cognome (String)
            checkNull(categoria),       // Colonna 3: Categoria (String)
            puntiCategoria,             // Colonna 4: Punti Categoria (Double - tipo numerico per filtri)
            spesaTotale,                // Colonna 5: Spesa Totale (Integer - tipo numerico per filtri)
            ordiniCategoria             // Colonna 6: Ordini Categoria (Integer - tipo numerico per filtri)
        };
    }

    // Popola il modello della tabella con i prodotti per categoria
    public void categoriaprodotti(String c, DefaultTableModel model) throws SQLException {
        List<Prodotto> prodotti = prdjdbc.getbycategoria(c);
        populateTable(prodotti, model, p -> createRowData(
                p.getCodProdotto(),
                p.getNome(),
                p.getPrezzo(),
                p.getCategoria(),
                p.getScorta()
        ));
    }

    public void allCliente() throws SQLException {
        List<Cliente> clienti = cljdbc.getAllCt();
        populateTable(clienti, clienteModel, c -> createRowData(
                c.getCodCliente(),
                c.getNome(),
                c.getCognome(),
                c.getCodiceFiscale(),
                c.getEmail(),
                c.getIndirizzo(),
                c.getTelefono(),
                c.getTessera() != null ? c.getTessera().getCodTessera() : null,
                c.getTessera() != null ? c.getTessera().getNumeroPunti() : null,
                c.getTessera() != null ? c.getTessera().getStato() : null,
                c.getTessera() != null && c.getTessera().getDataScadenza() != null ? c.getTessera().getDataScadenza().toString() : null
        ));
    }

    public void allDipendenti() throws SQLException {
        List<Dipendente> dipendenti = dpjdbc.getAllDip();
        populateTable(dipendenti, dipModel, d -> createRowData(
                d.getCodDIP(),
                d.getNome(),
                d.getCognome(),
                d.getCodFis(),
                d.getEmail(),
                d.getInd(),
                d.getTel()
        ));
    }

    public void allOrdini() throws SQLException {
        List<Cliente> clienti = cljdbc.getAllCt();
        List<Dipendente> dipendenti = dpjdbc.getAllDip();
        Map<String, Cliente> clientiMap = clienti.stream().collect(Collectors.toMap(c -> String.valueOf(c.getCodCliente()), c -> c));
        Map<String, Dipendente> dipMap = dipendenti.stream().collect(Collectors.toMap(d -> String.valueOf(d.getCodDIP()), d -> d));

        List<Ordine> ordini = ordjdbc.getallordini();
        populateTable(ordini, ordModel, o -> {
            Cliente ct = clientiMap.get(String.valueOf(o.getIdCliente()));
            Dipendente d = dipMap.get(String.valueOf(o.getIdDipendente()));
            String clienteNome = checkNull(ct != null ? ct.getCognome() + " " + ct.getNome() : null);
            String dipendenteNome = checkNull(d != null ? d.getCognome() + " " + d.getNome() : null);
            return createRowData(o.getCodOrdine(), o.getDataAcquisto(), o.getPrezzoTotale(), clienteNome, dipendenteNome);
        });
    }

    public void allProdotti() throws SQLException {
        List<Prodotto> prodotti = prdjdbc.getallprodotti();
        populateTable(prodotti, prodModel, p -> createRowData(
                p.getCodProdotto(),
                p.getNome(),
                p.getDescrizione(),
                p.getPrezzo(),
                p.getLuogoProvenienza(),
                p.getDataraccolta(),
                p.getDatamungitura(),
                formatGlutineStatus(p.isGlutine()),
                p.getDatascadenza(),
                p.getDataProduzione(),
                p.getCategoria(),
                p.getScorta()
        ));
    }

    // --- Metodi di supporto per l'aggiornamento dei model delle tabelle (CENTRALIZZATI E OTTIMIZZATI) ---
    private void updateDipendenteModelAfterInsert(String codDipendente, String nome, String cognome, String codFis, String email, String indirizzo, String telefono) {
        dipModel.addRow(createRowData(codDipendente, nome, cognome, codFis, email, indirizzo, telefono));
    }
    private void updateClienteModelAfterInsert(String codCliente, String nome, String cognome, String codFis, String email, String indirizzo, String telefono) {
        // Inserisce valori di default per la tessera: punti 0.0, stato "Attiva", data odierna
        clienteModel.addRow(createRowData(
            codCliente, nome, cognome, codFis, email, indirizzo, telefono,
            "", // Id Tessera (da aggiornare se disponibile)
            0.0,
            "ATTIVA",
            java.time.LocalDate.now().toString() // Data odierna
        ));
    }
    private void updateProdottoModelAfterInsert(String codProdotto, String nome, String descrizione, double prezzo, String luogoProvenienza, java.time.LocalDate dataRaccolta, java.time.LocalDate dataMungitura, boolean glutine, java.time.LocalDate dataScadenza, String categoria, int scorta) {
        prodModel.addRow(createRowData(codProdotto, nome, descrizione, prezzo, luogoProvenienza, dataRaccolta, dataMungitura, formatGlutineStatus(glutine), dataScadenza, null, categoria, scorta));
    }
    private void updateDipendenteModelAfterUpdate(String codDipendente, String nome, String cognome, String codFis, String email, String indirizzo, String telefono) {
        updateTableRow(dipModel, codDipendente, new Object[]{codDipendente, nome, cognome, codFis, email, indirizzo, telefono});
    }
    private void updateClienteModelAfterUpdate(String codCliente, String nome, String cognome, String codFis, String email, String indirizzo, String telefono) {
        updateTableRow(clienteModel, codCliente, new Object[]{codCliente, nome, cognome, codFis, email, indirizzo, telefono});
    }
    private void updateProdottoModelAfterUpdate(String codProdotto, String nome, String descrizione, double prezzo, String luogoProvenienza, java.time.LocalDate dataRaccolta, java.time.LocalDate dataMungitura, boolean glutine, java.time.LocalDate dataScadenza, String categoria, int scorta) {
        updateTableRow(prodModel, codProdotto, new Object[]{codProdotto, nome, descrizione, prezzo, luogoProvenienza, dataRaccolta, dataMungitura, formatGlutineStatus(glutine), dataScadenza, null, categoria, scorta});
    }
}