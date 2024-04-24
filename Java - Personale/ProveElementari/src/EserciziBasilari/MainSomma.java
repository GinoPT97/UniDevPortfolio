package EserciziBasilari;

import java.util.Scanner;

public class MainSomma {

	static Scanner risposta = new Scanner(System.in);

	public static int somma (int addendo1, int addendo2) {
        return addendo1 + addendo2;
    }

    public static String somma(String addendo1, String addendo2) {
     return addendo1 + addendo2;
    }

	public static void main(String[] args) {

        System.out.println("Cosa vuoi sommare? 1-numeri....2-stringhe");


        int input = risposta.nextInt();

        if( input == 1)  {
            System.out.println("Inserisci il primo numero");
            int num1=risposta.nextInt();

            System.out.println("Inserisci il secondo numero");
            int num2=risposta.nextInt();

            System.out.println("Il risultato è: "+ somma(num1,num2));
        }
        else  {
            risposta.nextLine();
            System.out.println("Inserisci la prima stringa");
           String str1 = risposta.nextLine();

            System.out.println("Inserisci la seconda stringa");
           String str2 =risposta.nextLine();

            System.out.println("Il risultato è : "+ somma(str1,str2));


        }
        risposta.close();
	}

}
