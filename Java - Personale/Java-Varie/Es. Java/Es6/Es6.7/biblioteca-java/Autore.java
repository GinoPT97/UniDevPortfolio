package biblioteca;

class Autore {
  private String nome;
  private String cognome;

  Autore( String nome, String cognome ){
    this.nome = nome;
    this.cognome = cognome;
  }

  public String toString() {
    return nome+" "+cognome;
  }
}
