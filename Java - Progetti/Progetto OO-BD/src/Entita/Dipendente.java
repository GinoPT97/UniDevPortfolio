package Entita;

public class Dipendente {
	private String coddipendente;
    private String nome;
    private String cognome;
    private String codicefiscale;
    private String email;
    private String indirizzo;
    private String telefono;
    
    public Dipendente(String coddipendente, String nome,String cognome, String codicefiscale, String email, String indirizzo, String telefono) {
 	   this.coddipendente = coddipendente;
 	   this.nome = nome;
 	   this.cognome = cognome;
 	   this.codicefiscale = codicefiscale;
 	   this.email = email;
 	   this.indirizzo = indirizzo;
 	   this.telefono = telefono;
    }
    
    public String getCodDIP() {
     	 return coddipendente;
      } 
      
      public void setCodDIP(String coddipendente) {
     	 this.coddipendente = coddipendente;
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
    
    public String toString() {
       return coddipendente + " " + nome + " " + cognome + " " + codicefiscale + " " + email + " " + indirizzo + " " + telefono + " ";
    }
}
