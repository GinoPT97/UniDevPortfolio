package EserciziBasilari;

import java.util.Scanner;

public class MainEquals {
	
	static Scanner setter = new Scanner(System.in);
	
	 public static void VerificaEquals(String str1, String str2) {
		 if(str1.equals(str2))
	         System.out.println("La stringa " + str1 + " è uguale a " + str2);
	      else
	         System.out.println("La stringa " + str1 + " è diversa da " + str2);
	 }

	public static void main(String[] args) {
		  
		  System.out.println("Inserisci la prima stringa : ");
	      String str1 = setter.nextLine();

	      System.out.println("Inserisci la seconda stringa : ");
	      String str2 = setter.nextLine();

	      VerificaEquals(str1, str2);

	      setter.close();
	      
	}

}
