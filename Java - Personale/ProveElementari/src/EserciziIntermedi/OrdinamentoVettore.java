package EserciziIntermedi;

import java.util.Scanner;

public class OrdinamentoVettore {
	
	static Scanner input = new Scanner(System.in);
	
	public static void swap(int a,int b) {
		int c = a;
		a = b;
		b = c;
	}
	
	public static void Ordinamento(int[] vettore) {
		for(int i = 0; i< vettore.length; i++)
			for(int j = 1 ; j< vettore.length; j++)
				if(vettore[i]<vettore[j])
					swap(vettore[i],vettore[j]);
		
		for (int i = 0; i < vettore.length; i++) 
			  System.out.println(vettore[i]);
	}

	public static void main(String[] args) {
		int[] vettore = new int[10];
		for (int i = 0; i < vettore.length; i++) {
			  System.out.print("Inserisci il valore dell'elemento " + i + ": ");
			  vettore[i] = input.nextInt();
			}
		
		Ordinamento(vettore);
	}

}
