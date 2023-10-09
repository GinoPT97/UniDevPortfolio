package EserciziBasilari;

import java.util.Scanner;

public class MainReverseString {
	
	static Scanner input = new Scanner(System.in); 
	
	public static String reverseString(String str){

        if( str.length() == 0 )
            return str;
        else
           return str.charAt(str.length()-1)  + reverseString( str.substring(0, str.length() - 1) );
        
    }

	public static void main(String[] args) {
		System.out.println("Inserisci una frase : ");
        String stringa = input.nextLine();

        System.out.println("La stringa inversa è " + reverseString(stringa));

        input.close();
	}

}
