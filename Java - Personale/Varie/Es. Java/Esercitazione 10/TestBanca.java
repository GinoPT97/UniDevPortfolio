/*
 * Questa classe contiene il programma per provare le classi del pacchetto banca.
 * Versione 4
 */
 
import banca.*;

public class TestBanca {

  public static void main(String[] args) {
    Banca banca = new Banca();
    
    banca.addCliente("Carla", "Rossi");
    banca.addCliente("Anna", "Bruni");
    banca.addCliente("Raul", "Falchi");
    banca.addCliente("Vale", "Bova");
    
    for (int i=0; i < banca.getNumClienti(); i++) {
      Cliente cliente = banca.getCliente(i);
      
      System.out.println("Cliente n. " + (i+1) + ": "
        + cliente.getCognome() + " " + cliente.getNome());
    }
  }
}

