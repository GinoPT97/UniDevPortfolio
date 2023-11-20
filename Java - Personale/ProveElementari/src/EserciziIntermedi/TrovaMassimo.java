package EserciziIntermedi;

import java.util.Scanner;

public class TrovaMassimo {
	
	// Funzione per trovare il massimo degli elementi in un array di interi
    public static int trovaMassimo(int[] arr) {
        if (arr.length == 0) {
            // Restituisci un valore sentinella (es. Integer.MIN_VALUE) se l'array è vuoto
            return Integer.MIN_VALUE;
        }

        int massimo = arr[0];

        // Itera attraverso gli elementi dell'array per trovare il massimo
        for (int elemento : arr) {
            if (elemento > massimo) {
                massimo = elemento;
            }
        }

        // Restituisci il massimo trovato
        return massimo;
    }
    
    public static void main(String[] args) {
        // Crea un oggetto Scanner per leggere l'input da tastiera
        Scanner scanner = new Scanner(System.in);

        // Chiedi all'utente la lunghezza dell'array
        System.out.print("Inserisci la lunghezza dell'array: ");
        int lunghezza = scanner.nextInt();

        // Crea un array di lunghezza specificata dall'utente
        int[] array = new int[lunghezza];

        // Chiedi all'utente di inserire gli elementi dell'array
        for (int i = 0; i < lunghezza; i++) {
            System.out.print("Inserisci l'elemento " + (i + 1) + ": ");
            array[i] = scanner.nextInt();
        }

        // Chiama la funzione per trovare il massimo degli elementi
        int massimo = trovaMassimo(array);

        // Stampare il risultato
        System.out.println("Il valore massimo dell'array è: " + massimo);

        // Chiudi lo scanner per evitare memory leaks
        scanner.close();
    }
}
