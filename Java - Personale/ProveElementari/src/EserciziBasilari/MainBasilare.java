package EserciziBasilari;

import java.util.Scanner;

public class MainBasilare {
	
	static Scanner input = new Scanner(System.in); // input
	static Scanner risposta = new Scanner(System.in);
	
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
    
    //esercizio per il fattoriale sia iterativo che ricorsivo
    
	public static void MainFattoriale() {
		
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
	
	 public static String reverseString(String str){

	        if( str.length() == 0 )
	            return str;
	        else
	           return str.charAt(str.length()-1)  + reverseString( str.substring(0, str.length() - 1) );
	        
	 }
	 
	 //esercizio che presa una stringa la restituisce inversa (palindroma)
	 
	 public static void MainReverseString() {
		 
	        System.out.println("Inserisci una frase : ");
	        String stringa = input.nextLine();

	        System.out.println("La stringa inversa è " + reverseString(stringa));

	        input.close();
	    
	 }
	 
	 public static void VerificaEquals(String str1, String str2) {
		 if(str1.equals(str2))
	         System.out.println("La stringa " + str1 + " è uguale a " + str2);
	      else
	         System.out.println("La stringa " + str1 + " è diversa da " + str2);
	 }
	 

	 //esercizio che verifica se due stringhe sono uguali o meno
	 
	 public static void MainEquals() {
	        
	      System.out.println("Inserisci la prima stringa");
	      String str1 = risposta.nextLine();

	      System.out.println("Inserisci la seconda stringa");
	      String str2 =risposta.nextLine();

	      VerificaEquals(str1, str2);

	      risposta.close();

	 }
	 
	 public static int somma (int addendo1, int addendo2) {
	        return addendo1 + addendo2;
	    }

	 public static String somma(String addendo1, String addendo2) {
	     return addendo1 + addendo2;
	}
	 
	 //Esercizio che somma due valori che possono essere interi o stringhe
	 
	 public static void MainSomma() {
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

            System.out.println("Il risultato è: "+ somma(str1,str2));
            

        }
        risposta.close();
	}
	
	 //Esercizio che presi tre valori li confronta e vede se sono tutti uguali o tutti diversi
	 
	 public static void confronto() {
		
		int x,y,z;
		
		System.out.print("Inserire il primo valore :");
		x = input.nextInt();
		
		System.out.print("Inserire il secondo valore :");
		y = input.nextInt();
		
		System.out.print("Inserire il terzo valore :");
		z = input.nextInt();
		
		input.close();
		
		if(x==y && x==z) System.out.print("tutti uguali");
		else if(y==x && (y > z || y < z)) System.out.print("z è diverso");
		else if(y==z && (y > x || z < x)) System.out.print("x è diverso");
		else if(x==z && (z > y || z < y)) System.out.print("y è diverso");
		else System.out.print("tutti diversi");
	}
	
	 public static int occorrenze(String st,String s1){
		 int sum=0,i=0,x;
		     do{
		       x=st.indexOf(s1,i);
		       if(x!=-1){ sum++; i=x+1; }
		     }while(x!=-1);
		   return sum;
		 } 
	 
	 //Esercizio che prevede il calcolo delle occorrenze di una stringa in una frase
	 
	 public static void MainOccorrenze() {
		 String st,s1;
		 
		 System.out.print("frase:");
		 st=input.nextLine();
		 
		 System.out.print("sottostringa da cercare:");
		 s1=input.nextLine();
		 
		 System.out.print("Il numero di occorrenze sono :");
		 System.out.println(occorrenze(st,s1));
		 input.close();
	 }
	 
	 
	 
	 public static void AllMain() {
		    //MainFattoriale();
			//MainReverseString();
			//MainEquals();
			//MainSomma();
			//confronto();
			//MainOccorrenze();
	 }
	
	public static void main(String[] args) {
		AllMain();
	}

}
