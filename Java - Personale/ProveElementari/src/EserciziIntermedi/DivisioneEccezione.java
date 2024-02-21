package EserciziIntermedi;

import java.util.Scanner;

public class DivisioneEccezione {

	public static void main(String[] args) {

		// Creiamo uno scanner per leggere input da tastiera
		Scanner scanner = new Scanner(System.in);

		try {
		    // Chiediamo all'utente di inserire il numeratore
		    System.out.print("Inserisci il numeratore: ");
		    int numeratore = scanner.nextInt();

		    // Chiediamo all'utente di inserire il denominatore
		    System.out.print("Inserisci il denominatore: ");
		    int denominatore = scanner.nextInt();

		    // Effettuiamo la divisione
		    int risultato = numeratore / denominatore;

		    // Stampiamo il risultato della divisione
		    System.out.println("Il risultato della divisione è: " + risultato);

		} catch (ArithmeticException e) {
		    // Se viene generata un'ArithmeticException (divisione per zero), gestiamo l'eccezione qui
		    System.err.println("Errore: Divisione per zero non consentita.");

		} catch (Exception e) {
		    // Se viene generata un'eccezione generica, gestiamo l'eccezione qui
		    System.err.println("Errore: Inserisci numeri validi.");

		} finally {
		    // Il blocco finally viene sempre eseguito, indipendentemente dall'occorrenza di eccezioni o meno
		    // Chiudiamo lo scanner per evitare leak di risorse
		    scanner.close();
		}

    }
}
