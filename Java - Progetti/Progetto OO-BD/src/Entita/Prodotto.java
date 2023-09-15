package Entita;

import java.awt.Component;
import java.util.*;

public class Prodotto {
	     private String codprodotto;
         private String nome;
         private String descrizione;
         private String luogoprov;
         private double prezzo;
         private Date datamungitura;
         private Date datascadenza;
         private Date dataraccolta;
         private boolean glutine;
         private String categoria;
         private int scorta;
         
         public Prodotto(String codprodotto,String nome,String descrizione,double prezzo,String luogoprov,Date dataraccolta,Date datamungitura,boolean glutine,Date datascadenza,String categoria,int scorta) {
        	 this.codprodotto = codprodotto;
        	 this.nome = nome;
        	 this.descrizione = descrizione;
        	 this.luogoprov = luogoprov;
        	 this.prezzo = prezzo;
        	 this.glutine = glutine;
        	 this.datamungitura = datamungitura;
        	 this.dataraccolta = dataraccolta;
        	 this.datascadenza = datascadenza;
        	 this.categoria = categoria;
        	 this.scorta = scorta;
         }

		public String getNome() {
        	 return nome;
         } 
         
         public void setNome(String nome) {
        	 this.nome = nome;
         }
         
         public String getCodProd() {
        	 return codprodotto;
         }
         
         public void setCodProd(String codprodotto) {
        	 this.codprodotto = codprodotto;
         }
         
         public String getDscrizione() {
        	 return descrizione;
         }
         
         public void setDescrizione(String descrizione) {
        	 this.descrizione = descrizione;
         }
         
         public String getLuogoProv() {
        	 return luogoprov;
         }
         
         public void setLuogoProv(String luogoprov) {
        	 this.luogoprov = luogoprov;
         }
         
         public Date getDatamung() {
        	 return datamungitura;
         }
         
         public void setDatamung(Date datamungitura) {
        	 this.datamungitura = datamungitura;
         }
         
         public Date getDatascad() {
        	 return datascadenza;
         }
         
         public void setDatascad(Date datascadenza) {
        	 this.datascadenza = datascadenza;
         }
         
         public Date getDataracc() {
        	 return dataraccolta;
         }
         
         public void setDataracc(Date dataraccolta) {
        	 this.dataraccolta = dataraccolta;
         }
         
         public double getPrezzo() {
        	 return prezzo;
         }
         
         public void setPrezzo(double prezzo) {
        	 this.prezzo = prezzo;
         }
         
         public int getScorta() {
        	 return scorta;
         }
         
         public void setScorta(int scorta) {
        	 this.scorta = scorta;
         }
         
         public boolean getGlutine() {
        	 return glutine;
         }
         
         public void setGlutine(boolean glutine) {
        	 this.glutine = glutine;
         }
         
         public String getCategoria() {
             return  categoria;
         }
         
         public void setCategoria(String categoria) {
             this.categoria = categoria;
         }
         
         public String toString() {
			return (codprodotto + " " + nome + " " + descrizione + " " + prezzo + " " + luogoprov + " " + dataraccolta + " " + datamungitura + " " + glutine + " " + datascadenza + " " + categoria + " " + scorta + " "); 
         }
}
