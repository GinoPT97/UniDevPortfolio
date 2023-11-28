package Esercitazioni;

import java.util.Scanner;

public class MinimoVettore {
	
	// Funzione per trovare il valore minimo di un vettore
    public static int trovaMinimo(int[] array) {
        // Assicurarsi che il vettore non sia vuoto
        if (array.length == 0) {
            throw new IllegalArgumentException("Il vettore è vuoto");
        }

        // Inizializzare il minimo con il primo elemento del vettore
        int minimo = array[0];

        // Scorrere gli elementi del vettore
        for (int i = 1; i < array.length; i++) {
            // Se l'elemento corrente è minore del valore minimo attuale, aggiornare il minimo
            if (array[i] < minimo) {
                minimo = array[i];
            }
        }

        // Restituire il valore minimo trovato
        return minimo;
    }

	public static void main(String[] args) {
        // Creare uno scanner per leggere l'input da tastiera
        Scanner scanner = new Scanner(System.in);

        // Chiedere all'utente la lunghezza del vettore
        System.out.print("Inserisci la lunghezza del vettore: ");
        int lunghezza = scanner.nextInt();

        // Dichiarare e inizializzare il vettore con la lunghezza fornita
        int[] vettore = new int[lunghezza];

        // Chiedere all'utente di inserire i valori del vettore
        for (int i = 0; i < lunghezza; i++) {
            System.out.print("Inserisci il valore per l'indice " + i + ": ");
            vettore[i] = scanner.nextInt();
        }

        // Chiudere lo scanner dopo aver letto tutti gli input
        scanner.close();

        // Chiamata alla funzione per trovare il minimo
        int minimo = trovaMinimo(vettore);

        // Stampare il risultato
        System.out.println("Il valore minimo nel vettore è: " + minimo);
    }
}
