package GUI;

import java.awt.Frame;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import DBConfiguration.ConnectionException;
import DBConfiguration.DBConfiguration;
import DBConfiguration.DBConnection;
import JDBC.ArticoliJDBC;
import JDBC.ClienteJDBC;
import JDBC.DipendenteJDBC;
import JDBC.OrdiniJDBC;
import JDBC.ProdottoJDBC;
import JDBC.TesseraJDBC;
import JDBCimplementazione.ArticoliImpl;
import JDBCimplementazione.Clienteimpl;
import JDBCimplementazione.Dipendenteimpl;
import JDBCimplementazione.Ordiniimpl;
import JDBCimplementazione.Prodottoimpl;
import JDBCimplementazione.Tesseraimpl;
import Model.Articoli;
import Model.BackgroundPanel;
import Model.Cliente;
import Model.Dipendente;
import Model.Ordine;
import Model.Prodotto;
import Model.Tessera;

public class Controller {
	private LoginFrame logf;
	private NuovoProdottoFrame nprodf;
	private AdminFrame adminf;
	private DipendenteFrame dipf;
	private VisioneDipendentiFrame vdipf;
	public VisioneProdottiFrame vprodf;
	public ModificaProdottiFrame modprodf;
	private StatisticheDipendentiFrame statdipf;
	private PuntiTesseraFrame ptessf;
	private CarrelloFrame carrf;
	private VisioneClienteFrame visctf;
	private NuovoDipendenteFrame ndipf;
	public ModificaDipendenteFrame updipf;
	private NuovoClienteFrame nclf;
	public ModificaClienteFrame upclf;
	private VisioneOrdineFrame visordf;
	public RicercaFrame searchf;
	private DBConnection dbconn;
	private DBConfiguration config = null;
	private Connection connection = null;
	private ClienteJDBC cljdbc = null;
	private DipendenteJDBC dpjdbc = null;
	private ProdottoJDBC prdjdbc = null;
	private OrdiniJDBC ordjdbc = null;
	private TesseraJDBC tsjdbc = null;
	private ArticoliJDBC artjdbc = null;
	public String iddip;
	private DefaultTableModel model;
	private Frame lastFrame; // Variabile per tenere traccia dell'ultimo frame

	public Controller() throws SQLException, IOException {
		logf = new LoginFrame("Login - Ortofrutta", this);
		adminf = new AdminFrame("Admin Area", this);
		dipf = new DipendenteFrame("Dipendente Area", this);
		nprodf = new NuovoProdottoFrame("Nuovo Prodotto", this);
		ndipf = new NuovoDipendenteFrame("Nuovo Dipendente", this);
		nclf = new NuovoClienteFrame("Nuovo Cliente", this);
		vdipf = new VisioneDipendentiFrame("Gestione Dipendenti", this);
		vprodf = new VisioneProdottiFrame("Gestione Prodotti", this);
		visctf = new VisioneClienteFrame("Gestione Clienti", this);
		upclf = new ModificaClienteFrame("Modifica Cliente", this);
		updipf = new ModificaDipendenteFrame("Modifica Dipendente", this);
		modprodf = new ModificaProdottiFrame("Modifica Prodotti", this);
		statdipf = new StatisticheDipendentiFrame("Statistiche Dipendenti", this);
		ptessf = new PuntiTesseraFrame("Punti Tessera", this);
		carrf = new CarrelloFrame("Carrello", this);
		visordf = new VisioneOrdineFrame("Visione Ordini", this);
		searchf = new RicercaFrame("Ricerca Clienti", this);
		logf.setVisible(true);
	}

	public JPanel createBackgroundPanel(String imagePath) {
		return new BackgroundPanel(imagePath);
	}

	public void returnToLastFrame() {
	    // Mostra l'ultimo frame visibile e nasconde searchf e visordf
	    setVisibleFrame(lastFrame, searchf, visordf);
	}

	private void setVisibleFrame(Frame toShow, Frame... toHide) {
	    // Nasconde tutti i frame nella lista toHide
	    for (Frame frame : toHide) {
	        if (frame != null) {
	            frame.setVisible(false);
	        }
	    }
	    // Mostra il frame specificato da toShow
	    if (toShow != null) {
	        toShow.setVisible(true);
	    }
	}

	public void logtoutente(int x) {
	    logf.setVisible(false); // Nasconde il frame di login
	    // Mostra il frame adminf o dipf in base al valore di x
	    setVisibleFrame((x == 1) ? adminf : dipf, (x == 1) ? dipf : adminf);
	}

	public void logout(int x) {
	    // Mostra il frame di login e nasconde adminf o dipf in base al valore di x
	    setVisibleFrame(logf, (x == 1) ? adminf : dipf);
	}

	public void adminAndElem(int x) {
	    lastFrame = adminf; // Salva l'ultimo frame visibile come adminf
	    // Mostra il frame corrispondente a x e nasconde gli altri
	    switch (x) {
	        case 1 -> setVisibleFrame(vdipf, adminf, vprodf, statdipf, visordf);
	        case 2 -> setVisibleFrame(vprodf, adminf, vdipf, statdipf, visordf);
	        case 3 -> setVisibleFrame(statdipf, adminf, vdipf, vprodf, visordf);
	        case 4 -> setVisibleFrame(visordf, adminf, vdipf, vprodf, statdipf);
	        case 5 -> setVisibleFrame(adminf, vdipf, vprodf, statdipf, visordf); // Mostra solo adminf
	    }
	}

	public void searchAndElem(int x) {
	    // Salva l'ultimo frame visibile come adminf o dipf
	    lastFrame = (adminf.isVisible()) ? adminf : dipf;
	    // Mostra il frame di ricerca e nasconde adminf o dipf in base al valore di x
	    switch (x) {
	        case 1 -> setVisibleFrame(searchf, adminf, dipf);
	        case 2 -> setVisibleFrame(adminf, searchf);
	        case 3 -> setVisibleFrame(dipf, searchf);
	    }
	}

	public void visAndCarr(int x) {
	    // Salva l'ultimo frame visibile prima di cambiare
        lastFrame = (adminf.isVisible()) ? adminf : dipf;
	    switch (x) {
	        case 1 -> setVisibleFrame(carrf);
	        case 2 -> setVisibleFrame(visordf, carrf);
	        case 3 -> setVisibleFrame(lastFrame, visordf);
	    }
	}

	public void dipAndElem(int x) {
	    lastFrame = dipf; // Salva l'ultimo frame visibile come dipf
	    // Mostra il frame corrispondente a x e nasconde dipf se presente
	    switch (x) {
	        case 1 -> setVisibleFrame(visctf, dipf);
	        case 2 -> setVisibleFrame(ptessf, dipf);
	        case 3 -> setVisibleFrame(visordf, dipf);
	        case 4 -> setVisibleFrame(dipf, ptessf, visctf);
	    }
	}

	public void visAnddip(int x) {
	    // Mostra il frame corrispondente a x e nasconde gli altri
	    switch (x) {
	        case 1 -> setVisibleFrame(ndipf, vdipf, updipf); // Nuovo dipendente
	        case 2 -> setVisibleFrame(updipf, vdipf, ndipf); // Modifica dipendente
	        case 3 -> setVisibleFrame(vdipf, ndipf, updipf); // Vista dipendenti
	    }
	}

	public void visAndcl(int x) {
	    // Mostra il frame corrispondente a x e nasconde gli altri
	    switch (x) {
	        case 1 -> setVisibleFrame(nclf, visctf);
	        case 2 -> setVisibleFrame(upclf, visctf);
	        case 3 -> setVisibleFrame(visctf, nclf, upclf);
	    }
	}

	public void visAndprod(int x) {
	    // Mostra il frame corrispondente a x e nasconde gli altri
	    switch (x) {
	        case 1 -> setVisibleFrame(nprodf, vprodf);
	        case 2 -> setVisibleFrame(modprodf, vprodf);
	        case 3 -> setVisibleFrame(vprodf, nprodf, modprodf);
	    }
	}


	public static void main(String[] args) throws SQLException, IOException {
		Controller c = new Controller();
	}

	public void connect() throws SQLException {
		try {
			dbconn = DBConnection.getInstance("postgres");
			connection = dbconn.getConnection();
			config = new DBConfiguration(connection);
			// Metodi per la definizione del DB:
			config.createTipologie();
			config.createSequences();
			config.createTableCliente();
			config.createTableDipendente();
			config.createTableOrdine();
			config.createTableProdotto();
			config.createTableTessera();
			config.createTableArticoliOrdine();
			config.FromatTables();
			config.populateDatabase();
			cljdbc = new Clienteimpl(connection);
			dpjdbc = new Dipendenteimpl(connection);
			prdjdbc = new Prodottoimpl(connection);
			ordjdbc = new Ordiniimpl(connection);
			tsjdbc = new Tesseraimpl(connection);
			artjdbc = new ArticoliImpl(connection);
		} catch (SQLException | ConnectionException e) {
			System.out.println("SQLException: " + e.getMessage());
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
        model.setRowCount(0); // Resetta il modello per evitare duplicati
        // Aggiungi righe per ogni cliente trovato
        for (Cliente c : artjdbc.SearchClient()) {
            Object[] pr = { c.getNome(), c.getCognome(), c.getArticoliOrdini().getCategoria(),
                    c.getArticoliOrdini().getNumPunti() };
            model.addRow(pr); // Aggiungi riga al modello
        }
    }

    // Popola il modello della tabella con i prodotti per categoria
    public void categoriaprodotti(String c, DefaultTableModel model) throws SQLException {
        model.setRowCount(0); // Resetta il modello per evitare duplicati
        // Aggiungi righe per ogni prodotto nella categoria specificata
        for (Prodotto p : prdjdbc.getbycategoria(c)) {
            Object[] pr = { p.getCodProd(), p.getNome(), p.getPrezzo(), p.getCategoria(), p.getScorta() };
            model.addRow(pr); // Aggiungi riga al modello
        }
    }

    // Popola il modello della tabella con tutte le tessere
    public void alltessera(DefaultTableModel model) throws SQLException {
        model.setRowCount(0); // Resetta il modello per evitare duplicati
        // Aggiungi righe per ogni tessera
        for (Tessera t : tsjdbc.alltessera()) {
            Object[] pr = { t.getCodTessera(), t.getNPunti() };
            model.addRow(pr); // Aggiungi riga al modello
        }
    }

    // Popola il modello della tabella con tutti i clienti
    public void allcliente(DefaultTableModel model) throws SQLException {
        model.setRowCount(0); // Resetta il modello per evitare duplicati
        // Aggiungi righe per ogni cliente
        for (Cliente c : cljdbc.getAllCt()) {
            Object[] pr = { c.getCodCl(), c.getNome(), c.getCognome(), c.getCodFis(), c.getEmail(), c.getInd(),
                    c.getTel(), c.getTessera().getCodTessera(), c.getTessera().getNPunti() };
            model.addRow(pr); // Aggiungi riga al modello
        }
    }

    // Popola il modello della tabella con tutti i dipendenti
    public void alldipendenti(DefaultTableModel model) throws SQLException {
        model.setRowCount(0); // Resetta il modello per evitare duplicati
        // Aggiungi righe per ogni dipendente
        for (Dipendente d : dpjdbc.getAllDip()) {
            Object[] rowData = { d.getCodDIP(), d.getNome(), d.getCognome(), d.getCodFis(), d.getEmail(), d.getInd(),
                    d.getTel() };
            model.addRow(rowData); // Aggiungi riga al modello
        }
    }

    // Popola il modello della tabella con tutti gli ordini
    public void allordini(DefaultTableModel model) throws SQLException {
        model.setRowCount(0); // Resetta il modello per evitare duplicati
        // Aggiungi righe per ogni ordine
        for (Ordine o : ordjdbc.getallordini()) {
            Cliente ct = cljdbc.getCtByid(o.getIdCliente());
            Dipendente d = dpjdbc.getOneDip(o.getIdDipendente());
            Object[] pr = { o.getCodOrd(), o.getDataAcquisto().toString(), o.getPrezzoTotale(),
                    ct.getCognome() + " " + ct.getNome(), d.getCognome() + " " + d.getNome() };
            model.addRow(pr); // Aggiungi riga al modello
        }
    }

    // Popola il modello della tabella con tutti i prodotti
    public void allprodotti(DefaultTableModel model) throws SQLException {
        model.setRowCount(0); // Resetta il modello per evitare duplicati
        // Aggiungi righe per ogni prodotto
        for (Prodotto p : prdjdbc.getallprodotti()) {
            String glutenStatus = p.isGlutine() ? "Si" : "No"; // Determina se il prodotto contiene glutine
            Object[] pr = { p.getCodProd(), p.getNome(), p.getDescrizione(), p.getPrezzo(), p.getLuogoProv(),
                    p.getDataraccolta(), p.getDatamungitura(), glutenStatus, p.getDatascadenza(), p.getCategoria(),
                    p.getScorta() };
            model.addRow(pr); // Aggiungi riga al modello
        }
    }
}



