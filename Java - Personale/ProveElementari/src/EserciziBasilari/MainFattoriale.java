package EserciziBasilari;

import java.util.Scanner;

public class MainFattoriale {
	
	static Scanner input = new Scanner(System.in); // input
	
	public static int FattorialeRicorsivo(int n) {

        if( n <= 1)
            return 1;
        else
            return n * FattorialeRicorsivo( n - 1 );
        
    } 

    public static int FattorialeIterativo(int n){
        int result = 1;

        for (int i = 1; i <= n; i++) 
            result = result * i;
        
        return result;
    }

	public static void main(String[] args) {
		System.out.println("Inserisci un numero intero : ");
        int numero = input.nextInt();
        if ( numero < 0 ){
            System.out.println("Il numero deve essere positivo!");
            System.exit(0);
        }

        System.out.println("Il fattoriale ricorsivo è : " + FattorialeRicorsivo(numero));
        System.out.println("Il fattoriale iterativo è : " + FattorialeIterativo(numero));
        input.close();
	}

}
