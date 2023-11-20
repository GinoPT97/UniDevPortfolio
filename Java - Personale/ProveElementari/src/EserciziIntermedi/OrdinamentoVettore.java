package EserciziIntermedi;

import java.util.Scanner;

public class OrdinamentoVettore {
	
	// Importa la classe Scanner dal package java.util per consentire l'input da tastiera
	static Scanner input = new Scanner(System.in);

	// Definisci una funzione di scambio che scambia due elementi in un array dato gli indici
	public static void swap(int[] array, int i, int j) {
	    int temp = array[i];
	    array[i] = array[j];
	    array[j] = temp;
	}

	// Implementa la funzione di ordinamento che utilizza l'algoritmo di ordinamento a bolle
	public static void Ordinamento(int[] vettore) {
	    for (int i = 1; i < vettore.length; i++) {
	        for (int j = 0; j < i; j++) {
	            // Controlla se l'elemento corrente è minore dell'elemento in posizione j
	            if (vettore[i] < vettore[j]) {
	                // Chiama la funzione di scambio per scambiare gli elementi
	                swap(vettore, i, j);
	            }
	        }
	    }
	}

	// Metodo principale (main) del programma
	public static void main(String[] args) {
	    // Dichiarazione e inizializzazione di un array di interi di dimensione 10
	    int[] vettore = new int[10];

	    // Ciclo per l'input da tastiera degli elementi dell'array
	    for (int i = 0; i < vettore.length; i++) {
	        System.out.print("Inserisci il valore dell'elemento " + i + ": ");
	        vettore[i] = input.nextInt();
	    }

	    // Chiamata alla funzione di ordinamento per ordinare l'array in modo crescente
	    Ordinamento(vettore);

	    // Stampa gli elementi dell'array ordinato
	    for (int i = 0; i < vettore.length; i++) 
	        System.out.println(vettore[i]);
	}
}
