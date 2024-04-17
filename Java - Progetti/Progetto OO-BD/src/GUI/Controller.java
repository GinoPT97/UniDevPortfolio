package GUI;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import DBConfiguration.ConnectionException;
import DBConfiguration.DBConfiguration;
import DBConfiguration.DBConnection;
import Entita.Articoli;
import Entita.Cliente;
import Entita.Dipendente;
import Entita.Ordine;
import Entita.Prodotto;
import Entita.Tessera;
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

	public Controller() throws SQLException {
		logf = new LoginFrame("Login - Ortofrutta", this);
		logf.setVisible(true);
		adminf = new AdminFrame("Admin Area", this);
		dipf = new DipendenteFrame("Dipendente Area", this);
		nprodf = new NuovoProdottoFrame("Nuovo Prodotto", this);
		ndipf = new NuovoDipendenteFrame("Nuovo Dipendente", this);
		nclf = new NuovoClienteFrame("Nuovo Cliente", this);
		vdipf = new VisioneDipendentiFrame("Gestione Dipendenti", this);
		vprodf = new VisioneProdottiFrame("Gestione Prodotti", this);
		visctf = new VisioneClienteFrame("Gestione Clienti", this);
		upclf = new ModificaClienteFrame("Modifica Cliente",this);
		updipf = new ModificaDipendenteFrame("Modifica Dipendente",this);
		modprodf = new ModificaProdottiFrame("Modifica Prodotti", this);
		statdipf = new StatisticheDipendentiFrame("Statistiche Dipendenti",this);
		ptessf = new PuntiTesseraFrame("Punti Tessera", this);
		carrf = new CarrelloFrame("Carrello", this);
	    visordf = new VisioneOrdineFrame("Visione Ordini",this);
	    searchf = new RicercaFrame("Ricerca Clienti",this);
	}

	public void logtoutente(int x) {
		logf.setVisible(false);
		if(x==1) {
			adminf.setVisible(true);
		}
		if(x==2) {
			dipf.setVisible(true);
		}
	}

	public void logout(int x) {
		if(x==1) {
			adminf.setVisible(false);
		}
		if(x==2) {
			dipf.setVisible(false);
		}
		logf.setVisible(true);
	}

	public void adminAndElem(int x) {
		adminf.setVisible(false);
		if (x==1) {
			vdipf.setVisible(true);
		}
		if (x==2) {
			vprodf.setVisible(true);
		}
		if (x==3) {
			statdipf.setVisible(true);
		}
		if (x==4) {
			visordf.setVisible(true);
			visordf.x = 3;
		}
		if (x==5) {
			vdipf.setVisible(false);
			vprodf.setVisible(false);
			statdipf.setVisible(false);
			visordf.setVisible(false);
			adminf.setVisible(true);
		}
	}

	public void searchAndElem(int x) {
		if(x==1) {
			adminf.setVisible(false);
			dipf.setVisible(false);
			searchf.setVisible(true);
		} else {
			searchf.setVisible(false);
			if(x==2) {
				adminf.setVisible(true);
			}
			if(x==3) {
				dipf.setVisible(true);
			}
		}
	}

	public void visAndCarr(int x) {
		if (x==1) {
			carrf.setVisible(true);
		}
		if (x==2) {
			carrf.setVisible(false);
			visordf.setVisible(true);
		}
		if (x==3) {
			visordf.setVisible(false);
			adminf.setVisible(true);
		}else if(x==4) {
			visordf.setVisible(false);
			dipf.setVisible(true);
		}

	}

	public void dipAndElem(int x) {
		dipf.setVisible(false);
		if (x==1) {
			visctf.setVisible(true);
		}
		if (x==2) {
			ptessf.setVisible(true);
		}
		if (x==3) {
			visordf.setVisible(true);
			visordf.x = 4;
		}
		if (x==4) {
			dipf.setVisible(true);
			ptessf.setVisible(false);
			visctf.setVisible(false);
		}
	}

	public void visAnddip(int x) {
		vdipf.setVisible(false);
		if (x==1) {
			ndipf.setVisible(true);
		}
		if (x==2) {
			updipf.setVisible(true);
		}
		if (x==3) {
			vdipf.setVisible(true);
			ndipf.setVisible(false);
			updipf.setVisible(false);
		}
	}

	public void visAndcl(int x) {
		visctf.setVisible(false);
		if (x==1) {
			nclf.setVisible(true);
		}
		if (x==2) {
			upclf.setVisible(true);
		}
		if (x==3) {
			upclf.setVisible(false);
			nclf.setVisible(false);
			visctf.setVisible(true);
		}
	}

	public void visAndprod(int x) {
		vprodf.setVisible(false);
		if (x==1) {
			nprodf.setVisible(true);
		}
		if (x==2) {
			modprodf.setVisible(true);
		}
		if (x==3) {
			modprodf.setVisible(false);
			nprodf.setVisible(false);
			vprodf.setVisible(true);
		}
	}

	public static void main(String[] args) throws SQLException{
		Controller c = new Controller();
	}

	public void connect() throws SQLException{
        try {
		    dbconn = DBConnection.getInstance("postgres");
            connection = dbconn.getConnection();
            config = new DBConfiguration(connection);
  	        //Metodi per la definizione del DB:
            config.createTipologie();
	        config.createSequences();
			config.createTableCliente();
			config.createTableDipendente();
	        config.createTableOrdine();
	        config.createTableProdotto();
	        config.createTableTessera();
	        config.createTableArticoliOrdine();
	        cljdbc = new Clienteimpl(connection);
	        dpjdbc = new Dipendenteimpl(connection);
	        prdjdbc = new Prodottoimpl(connection);
	        ordjdbc = new Ordiniimpl(connection);
	        tsjdbc = new Tesseraimpl(connection);
	        artjdbc = new ArticoliImpl(connection);
            }
	        catch (SQLException | ConnectionException e)
	        {
	            System.out.println("SQLException: "+ e.getMessage());
	            e.printStackTrace();
	        }
	  }

	public boolean verifyid(String ID) throws SQLException{
		return (dpjdbc.verifyID(ID));
	}

	public boolean newdip(Dipendente dip) throws SQLException {
		return (dpjdbc.setNewDip(dip));
	}

	public boolean newclt(Cliente ct) throws SQLException {
		return (cljdbc.setNewCt(ct));
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

	public boolean nuovatessera(String a,String b, String c) throws SQLException {
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

	public boolean upscorte(int x,String s) throws SQLException {
		return prdjdbc.updateScorte(x, s);
	}

	public boolean uppunti(String codcl,double d) throws SQLException {
		return tsjdbc.updatepunti(codcl, d);
	}

	public boolean nuovoordine(Ordine ordine) throws SQLException {
		return ordjdbc.newordine(ordine);
	}

	public boolean newarticoli(Articoli articoli) throws SQLException {
		return artjdbc.newordine(articoli);
	}

	public String OldDate() throws SQLException{
		return ordjdbc.getOldDate();
	}

	public String CurrOrd() throws SQLException{
		return ordjdbc.getCurrentCod();
	}

	public void ClientSearch(DefaultTableModel model) throws SQLException {
		for(Cliente c : artjdbc.SearchClient()) {
			Object[] pr = {c.getNome(),c.getCognome(), c.getArticoliOrdini().getCategoria(),c.getArticoliOrdini().getNumPunti()};
			model.insertRow(0, pr);
		}
	}

	public void alltessera(DefaultTableModel model) throws SQLException {
		for(Tessera t : tsjdbc.alltessera()) {
			Object[] pr = {t.getCodT(),t.getNPunti()};
			model.insertRow(0, pr);
		}
	}

	public void allcliente(DefaultTableModel model) throws SQLException {
		for(Cliente c : cljdbc.getAllCt()) {
			  Object[] pr = {c.getCodCl(),c.getNome(),c.getCognome(),c.getCodFis(),c.getEmail(),c.getInd(),c.getTel(),c.getTessera().getCodT(),c.getTessera().getNPunti()};
			  model.insertRow(0, pr);
		}
	}

	public void categoriaprodotti(String c,DefaultTableModel model) throws SQLException {
		for(Prodotto p : prdjdbc.getbycategoria(c)) {
			Object[] pr = {p.getCodProd(),p.getNome(),p.getPrezzo(),p.getCategoria(),p.getScorta()};
			model.insertRow(0, pr);
		}
	}

	public void alldipendenti(DefaultTableModel model) throws SQLException {
		for(Dipendente d : dpjdbc.getAllDip()) {
			Object[] pr = {d.getCodDIP(),d.getNome(),d.getCognome(),d.getCodFis(),d.getEmail(),d.getInd(),d.getTel()};
			model.insertRow(0, pr);
		}
	}

	public void allordini(DefaultTableModel model) throws SQLException {
		for(Ordine o : ordjdbc.getallordini()) {
			Cliente ct = cljdbc.getCtByid(o.getIdCt());
			Dipendente d = dpjdbc.getOneDip(o.getIdDip());
			Object[] pr = {o.getCodOrd(),o.getAcquisto().toString(),o.getPrezzoT(),ct.getCognome()+" "+ct.getNome(), d.getCognome()+" "+d.getNome()};
			model.insertRow(0, pr);
		}
	}

	public void allprodotti(DefaultTableModel model) throws SQLException {
		String a = "No";
		for(Prodotto p : prdjdbc.getallprodotti()) {
			if(p.getGlutine()) {
				a = "Si";
			}
			Object[] pr = {p.getCodProd(),p.getNome(),p.getDscrizione(),p.getPrezzo(),p.getLuogoProv(),p.getDataracc(),p.getDatamung(),a,p.getDatascad(),p.getCategoria(),p.getScorta()};
			model.insertRow(0, pr);
		}
	}
}