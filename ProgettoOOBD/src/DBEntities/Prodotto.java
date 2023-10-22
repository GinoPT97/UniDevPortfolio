package DBEntities;

import java.util.*;

public class Prodotto {

    private String codiceProdotto;
    private String categoria;
    private String nome;
    private String descrizione;
    private float prezzo;
    private String luogoProvenienza;
    private Date dataMungitura;
    private Date dataRaccolta;
    private Date dataScadenza;
    private Boolean glutine;
    private int scorta;

    public Prodotto(String codiceProdotto, String nome, String descrizione, float prezzo, String luogoProvenienza, Date dataRaccolta, Date dataMungitura, Date dataScadenza, Boolean glutine, String categoria, int scorta) {
        this.categoria = categoria;
        this.codiceProdotto = codiceProdotto;
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.luogoProvenienza = luogoProvenienza;
        this.dataMungitura = dataMungitura;
        this.dataRaccolta = dataRaccolta;
        this.dataScadenza = dataScadenza;
        this.glutine = glutine;
        this.scorta = scorta;
    }

    public String getCodiceProdotto() { return codiceProdotto; }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public String getLuogoProvenienza() {
        return luogoProvenienza;
    }

    public void setLuogoProvenienza(String luogoProvenienza) {
        this.luogoProvenienza = luogoProvenienza;
    }

    public Date getDataMungitura() {
        return dataMungitura;
    }

    public void setDataMungitura(Date dataMungitura) {
        this.dataMungitura = dataMungitura;
    }

    public Date getDataRaccolta() {
        return dataRaccolta;
    }

    public void setDataRaccolta(Date dataRaccolta) {
        this.dataRaccolta = dataRaccolta;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public Boolean getGlutine() {
        return glutine;
    }

    public void setGlutine(Boolean glutine) {
        this.glutine = glutine;
    }

    public int getScorta() {
        return scorta;
    }

    public void setScorta(int scorta) {
        this.scorta = scorta;
    }

    @Override
    public String toString() {
        return("CodiceProdotto: " + codiceProdotto +
                "\nNome: " + nome +
                "\nDescrizione: " + descrizione +
                "\nPrezzo: " + prezzo +
                "\nLuogo di Provenienza: " + luogoProvenienza +
                "\nData di Raccolta: " + dataRaccolta +
                "\nData di Mungitura: " + dataMungitura +
                "\nData di Scadenza: " + dataScadenza +
                "\nGlutine: " + glutine +
                "\nCategoria: " + categoria +
                "\nScorta: " + scorta);
    }
}
