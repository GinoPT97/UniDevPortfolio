/*
 * Questa classe contiene il programma per provare le classi del pacchetto banca.
 * Versione 5.2
 */
 
import banca.*;

public class TestBanca {

  public static void main(String[] args) {
    Banca banca = new Banca();
    Cliente cliente;
    
    //
    // Creazione dei clienti e dei loro conti
    //
    
    banca.addCliente("Carla", "Rossi");
    cliente = banca.getCliente(0);
    cliente.addConto(new LibrettoRisparmio(500.00, 0.05));
    cliente.addConto(new ContoCorrente(200.00, 400.00));

    banca.addCliente("Anna", "Bruni");
    cliente = banca.getCliente(1);
    cliente.addConto(new ContoCorrente(200.00));

    banca.addCliente("Raul", "Falchi");
    cliente = banca.getCliente(2);
    cliente.addConto(new ContoCorrente(200.00));
    cliente.addConto(new LibrettoRisparmio(1500.00, 0.05));

    banca.addCliente("Vale", "Bova");
    cliente = banca.getCliente(3);
    cliente.addConto(banca.getCliente(2).getConto(0));
    cliente.addConto(new LibrettoRisparmio(150.00, 0.05));
    
    //
    // Genera un rapporto
    //
    for (int i=0; i < banca.getNumClienti(); i++) {
      cliente = banca.getCliente(i);
      
      System.out.println();
      System.out.println("Cliente: " +
        cliente.getNome() + " " +
        cliente.getCognome());
      
      for (int j=0; j < cliente.getNumConti(); j++) {
        Conto conto = cliente.getConto(j);
        
        // Passo 1: Determinare il tipo di conto
        /*** usare l'operatore instanceof per controllare il tipo di conto
        **** e stampare "Libretto Risparmio" oppure "Conto Corrente"
        ***/
        
        // Passo 2: Stampare il saldo del conto
      
      }
    }
  }
}

