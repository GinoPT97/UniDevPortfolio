package EserciziBasilari;

import java.util.Scanner;

public class MainConfronto {

	static Scanner input = new Scanner(System.in);

	public static void confronto(int x, int y, int z) {
		if(x==y && x==z) {
			System.out.print("tutti uguali");
		} else if(y==x && (y > z || y < z)) {
			System.out.print("z è diverso");
		} else if(y==z && (y > x || z < x)) {
			System.out.print("x è diverso");
		} else if(x==z && (z > y || z < y)) {
			System.out.print("y è diverso");
		} else {
			System.out.print("tutti diversi");
		}
	}

	public static void main(String[] args) {
        int x,y,z;

		System.out.print("Inserire il primo valore :");
		x = input.nextInt();

		System.out.print("Inserire il secondo valore :");
		y = input.nextInt();

		System.out.print("Inserire il terzo valore :");
		z = input.nextInt();

		confronto(x,y,z);

		input.close();
	}

}
