package Entita;

public class Cliente {
       private String codcliente;
       private String nome;
       private String cognome;
       private String codicefiscale;
       private String email;
       private String indirizzo;
       private String telefono;
       private Tessera tessera;
       private Articoli articoliordini;

       public Cliente(String codcliente, String nome,String cognome, String codicefiscale, String email, String indirizzo, String telefono, Tessera tessera, Articoli articoliordini) {
    	   this.codcliente = codcliente;
    	   this.nome = nome;
    	   this.cognome = cognome;
    	   this.codicefiscale = codicefiscale;
    	   this.email = email;
    	   this.indirizzo = indirizzo;
    	   this.telefono = telefono;
    	   this.tessera = tessera;
    	   this.articoliordini =articoliordini;
       }

       public String getCodCl() {
        	 return codcliente;
         }

         public void setCodCL(String codcliente) {
        	 this.codcliente = codcliente;
         }

       public String getNome() {
      	 return nome;
       }

       public void setNome(String nome) {
      	 this.nome = nome;
       }

       public String getCognome() {
      	 return cognome;
       }

       public void setcognome(String cognome) {
      	 this.cognome = cognome;
       }

       public String getCodFis() {
      	 return codicefiscale;
       }

       public void setCodFis(String codicefiscale) {
      	 this.codicefiscale = codicefiscale;
       }
       public String getEmail() {
      	 return email;
       }

       public void setEmail(String email) {
      	 this.email = email;
       }

       public String getInd() {
      	 return indirizzo;
       }

       public void setInd(String indirizzo) {
      	 this.indirizzo = indirizzo;
       }

       public String getTel() {
      	 return telefono;
       }

       public void setTel(String telefono) {
      	 this.telefono = telefono;
       }

       public Tessera getTessera() {
         	 return tessera;
          }

       public void setTessera(Tessera tessera) {
         	 this.tessera = tessera;
          }

       public Articoli getArticoliOrdini() {
    	    return articoliordini;
    	}

    	public void setArticoliOrdini(Articoli articoliordini) {
    	    this.articoliordini = articoliordini;
    	}

       @Override
	public String toString() {
    	    return codcliente + " " + nome + " " + cognome + " " + codicefiscale + " " + email + " " + indirizzo + " " + telefono;
    	}
}