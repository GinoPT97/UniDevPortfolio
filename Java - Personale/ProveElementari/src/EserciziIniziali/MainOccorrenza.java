package EserciziIniziali;

import java.util.Scanner;

public class MainOccorrenza {

	static Scanner setter = new Scanner(System.in);

	 public static int occorrenze(String st,String s1){
		 int sum=0,i=0,x;
		     do{
		       x=st.indexOf(s1,i);
		       if(x!=-1){ sum++; i=x+1; }
		     }while(x!=-1);
		   return sum;
     }

	public static void main(String[] args) {
		String st,s1;

		 System.out.print("frase:");
		 st = setter.nextLine();

		 System.out.print("sottostringa da cercare:");
		 s1 = setter.nextLine();

		 System.out.print("Il numero di occorrenze sono :");
		 System.out.println(occorrenze(st,s1));
		 setter.close();
	}

}
