package EserciziIntermedi;

import java.util.Scanner;

public class MediaArrayEccezione {
	
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Chiediamo all'utente di inserire la lunghezza dell'array
            System.out.print("Inserisci la lunghezza dell'array: ");
            int lunghezza = scanner.nextInt();

            // Creiamo un array di dimensione specificata dall'utente
            int[] numeri = new int[lunghezza];

            // Chiediamo all'utente di inserire gli elementi dell'array
            System.out.println("Inserisci gli elementi dell'array:");
            for (int i = 0; i < lunghezza; i++) {
                System.out.print("Elemento " + (i + 1) + ": ");
                numeri[i] = scanner.nextInt();
            }

            // Calcoliamo la media degli elementi dell'array
            int somma = 0;
            for (int numero : numeri) {
                somma += numero;
            }
            double media = (double) somma / lunghezza;

            // Stampiamo la media calcolata
            System.out.println("La media degli elementi dell'array è: " + media);

        } catch (ArithmeticException e) {
            // Se viene generata un'eccezione ArithmeticException,
            // gestiamo l'errore relativo a una divisione per zero (nel calcolo della media)
            System.err.println("Errore: La lunghezza dell'array non può essere zero.");

        } catch (Exception e) {
            // Se viene generata un'eccezione generica,
            // gestiamo l'errore relativo a input non valido
            System.err.println("Errore: Inserisci una lunghezza e elementi validi.");

        } finally {
            // Chiudiamo lo scanner per evitare leak di risorse
            scanner.close();
        }
    }
}
