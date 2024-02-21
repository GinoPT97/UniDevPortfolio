package EserciziIntermedi;

import java.util.Scanner;

public class CalcolaMedia {

	// Metodo per calcolare la media degli elementi in un array
    private static double calcolaMedia(int[] array) {
        int somma = 0;

        // Calcola la somma degli elementi nell'array
        for (int valore : array) {
            somma += valore;
        }

        // Calcola la media
        double media = (double) somma / array.length;

        return media;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Chiedi all'utente di inserire la lunghezza dell'array
        System.out.print("Inserisci la lunghezza dell'array: ");
        int lunghezza = scanner.nextInt();

        // Dichiarazione dell'array
        int[] array = new int[lunghezza];

        // Chiedi all'utente di inserire i valori dell'array
        System.out.println("Inserisci i valori dell'array:");

        for (int i = 0; i < lunghezza; i++) {
            System.out.print("Elemento " + (i + 1) + ": ");
            array[i] = scanner.nextInt();
        }

        // Calcola la media degli elementi nell'array
        double media = calcolaMedia(array);

        // Stampare il risultato
        System.out.println("La media degli elementi dell'array è: " + media);

        // Chiudi lo scanner
        scanner.close();
    }
}
