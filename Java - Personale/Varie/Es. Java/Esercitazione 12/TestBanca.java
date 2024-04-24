
import banca.dominio.*;
import banca.rapporti.RapportoClienti;

public class TestBanca {

  public static void main(String[] args) {
    Banca   banca = Banca.getBanca();
    Cliente cliente;
    RapportoClienti rapporto = new RapportoClienti();

    // Crea alcuni clienti ed i loro conti
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
    
    // Generara un rapporto
    rapporto.generaRapporto();
  }
}
