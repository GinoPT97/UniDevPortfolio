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
import JDBCimpl.ArticoliImpl;
import JDBCimpl.Clienteimpl;
import JDBCimpl.Dipendenteimpl;
import JDBCimpl.Ordiniimpl;
import JDBCimpl.Prodottoimpl;
import JDBCimpl.Tesseraimpl;
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

	private void setVisibleFrame(Frame toShow, Frame... toHide) {
		for (Frame frame : toHide) {
			frame.setVisible(false);
		}
		toShow.setVisible(true);
	}

	public void logtoutente(int x) {
		logf.setVisible(false);
		if (x == 1) {
			setVisibleFrame(adminf, dipf);
		} else if (x == 2) {
			setVisibleFrame(dipf, adminf);
		}
	}

	public void logout(int x) {
		if (x == 1) {
			setVisibleFrame(logf, adminf);
		} else if (x == 2) {
			setVisibleFrame(logf, dipf);
		}
	}

	public void adminAndElem(int x) {
		lastFrame = adminf;
		switch (x) {
		case 1 -> setVisibleFrame(vdipf, adminf);
		case 2 -> setVisibleFrame(vprodf, adminf);
		case 3 -> setVisibleFrame(statdipf, adminf);
		case 4 -> setVisibleFrame(visordf, adminf);
		case 5 -> setVisibleFrame(adminf, vdipf, vprodf, statdipf, visordf);
		}
	}

	public void searchAndElem(int x) {
		if (x == 1) {
			lastFrame = (adminf.isVisible()) ? adminf : dipf;
			setVisibleFrame(searchf, adminf, dipf);
		} else if (x == 2) {
			setVisibleFrame(adminf, searchf);
		} else if (x == 3) {
			setVisibleFrame(dipf, searchf);
		}
	}

	public void visAndCarr(int x) {
		switch (x) {
		case 1 -> setVisibleFrame(carrf);
		case 2 -> setVisibleFrame(visordf, carrf);
		case 3 -> {
			lastFrame = adminf;
			setVisibleFrame(adminf, visordf);
		}
		case 4 -> {
			lastFrame = dipf;
			setVisibleFrame(dipf, visordf);
		}
		}
	}

	public void dipAndElem(int x) {
		lastFrame = dipf;
		switch (x) {
		case 1 -> setVisibleFrame(visctf, dipf);
		case 2 -> setVisibleFrame(ptessf, dipf);
		case 3 -> setVisibleFrame(visordf, dipf);
		case 4 -> setVisibleFrame(dipf, ptessf, visctf);
		}
	}

	public void visAnddip(int x) {
		switch (x) {
		case 1 -> setVisibleFrame(ndipf, vdipf);
		case 2 -> setVisibleFrame(updipf, vdipf);
		case 3 -> setVisibleFrame(vdipf, ndipf, updipf);
		}
	}

	public void visAndcl(int x) {
		switch (x) {
		case 1 -> setVisibleFrame(nclf, visctf);
		case 2 -> setVisibleFrame(upclf, visctf);
		case 3 -> setVisibleFrame(visctf, nclf, upclf);
		}
	}

	public void visAndprod(int x) {
		switch (x) {
		case 1 -> setVisibleFrame(nprodf, vprodf);
		case 2 -> setVisibleFrame(modprodf, vprodf);
		case 3 -> setVisibleFrame(vprodf, nprodf, modprodf);
		}
	}

	public void returnToLastFrame() {
		setVisibleFrame(lastFrame, visordf, searchf);
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

	public boolean verifyid(String ID) throws SQLException {
		return dpjdbc.verifyID(ID);
	}

	public boolean newdip(Dipendente dip) throws SQLException {
		return dpjdbc.setNewDip(dip);
	}

	public boolean newclt(Cliente ct) throws SQLException {
		return cljdbc.setNewCt(ct);
	}

	public boolean newprod(Prodotto pe) throws SQLException {
		return prdjdbc.setNewProdotto(pe);
	}

	public String punti(String codt) throws SQLException {
		return tsjdbc.getpuntit(codt);
	}

	public List<String> introitidip(Date di, Date df) throws SQLException {
		return dpjdbc.getDipIntroiti(di, df);
	}

	public List<String> venditedip(Date di, Date df) throws SQLException {
		return dpjdbc.getDipVendite(di, df);
	}

	public boolean nuovatessera(String a, String b, String c) throws SQLException {
		return tsjdbc.newtessera(cljdbc.getCtByNCCF(a, b, c));
	}

	public boolean upprod(Prodotto pe) throws SQLException {
		return prdjdbc.updateProdotto(pe);
	}

	public boolean updip(Dipendente de) throws SQLException {
		return dpjdbc.updatedipendente(de);
	}

	public boolean upcliente(Cliente ce) throws SQLException {
		return cljdbc.updateCliente(ce);
	}

	public String getct(String codfisc) throws SQLException {
		return cljdbc.getIdCt(codfisc);
	}

	public boolean upscorte(int x, String s) throws SQLException {
		return prdjdbc.updateScorte(x, s);
	}

	public boolean uppunti(String codcl, double d) throws SQLException {
		return tsjdbc.updatepunti(codcl, d);
	}

	public boolean nuovoordine(Ordine ordine) throws SQLException {
		return ordjdbc.newordine(ordine);
	}

	public boolean newarticoli(Articoli articoli) throws SQLException {
		return artjdbc.newordine(articoli);
	}

	public String OldDate() throws SQLException {
		return ordjdbc.getOldDate();
	}

	public String CurrOrd() throws SQLException {
		return ordjdbc.getCurrentCod();
	}

	public void ClientSearch(DefaultTableModel model) throws SQLException {
		model.setRowCount(0); // Resetta il modello per evitare duplicati
		for (Cliente c : artjdbc.SearchClient()) {
			Object[] pr = { c.getNome(), c.getCognome(), c.getArticoliOrdini().getCategoria(),
					c.getArticoliOrdini().getNumPunti() };
			model.addRow(pr);
		}
	}

	public void categoriaprodotti(String c, DefaultTableModel model) throws SQLException {
		model.setRowCount(0); // Resetta il modello per evitare duplicati
		for (Prodotto p : prdjdbc.getbycategoria(c)) {
			Object[] pr = { p.getCodProd(), p.getNome(), p.getPrezzo(), p.getCategoria(), p.getScorta() };
			model.insertRow(0, pr);
		}
	}

	public void alltessera(DefaultTableModel model) throws SQLException {
		model.setRowCount(0); // Resetta il modello per evitare duplicati
		for (Tessera t : tsjdbc.alltessera()) {
			Object[] pr = { t.getCodTessera(), t.getNPunti() };
			model.insertRow(0, pr);
		}
	}

	public void allcliente(DefaultTableModel model) throws SQLException {
		model.setRowCount(0); // Resetta il modello per evitare duplicati
		for (Cliente c : cljdbc.getAllCt()) {
			Object[] pr = { c.getCodCl(), c.getNome(), c.getCognome(), c.getCodFis(), c.getEmail(), c.getInd(),
					c.getTel(), c.getTessera().getCodTessera(), c.getTessera().getNPunti() };
			model.insertRow(0, pr);
		}
	}

	public void alldipendenti(DefaultTableModel model) throws SQLException {
		model.setRowCount(0); // Resetta il modello per evitare duplicati
		for (Dipendente d : dpjdbc.getAllDip()) {
			Object[] pr = { d.getCodDIP(), d.getNome(), d.getCognome(), d.getCodFis(), d.getEmail(), d.getInd(),
					d.getTel() };
			model.insertRow(0, pr);
		}
	}

	public void allordini(DefaultTableModel model) throws SQLException {
		model.setRowCount(0); // Resetta il modello per evitare duplicati
		for (Ordine o : ordjdbc.getallordini()) {
			Cliente ct = cljdbc.getCtByid(o.getIdCliente());
			Dipendente d = dpjdbc.getOneDip(o.getIdDipendente());
			Object[] pr = { o.getCodOrd(), o.getDataAcquisto().toString(), o.getPrezzoTotale(),
					ct.getCognome() + " " + ct.getNome(), d.getCognome() + " " + d.getNome() };
			model.insertRow(0, pr);
		}
	}

	public void allprodotti(DefaultTableModel model) throws SQLException {
		model.setRowCount(0); // Resetta il modello per evitare duplicati
		String a = "No";
		for (Prodotto p : prdjdbc.getallprodotti()) {
			if (p.isGlutine()) {
				a = "Si";
			}
			Object[] pr = { p.getCodProd(), p.getNome(), p.getDescrizione(), p.getPrezzo(), p.getLuogoProv(),
					p.getDataraccolta(), p.getDatamungitura(), a, p.getDatascadenza(), p.getCategoria(), p.getScorta() };
			model.insertRow(0, pr);
		}
	}
}
