package EserciziIntermedi;

import java.util.Scanner;

public class RadiceQuadrata {

	    public static void main(String[] args) {

	        Scanner scanner = new Scanner(System.in);

	        try {
	            System.out.print("Inserisci un numero per calcolarne la radice quadrata: ");
	            double numero = scanner.nextDouble();

	            if (numero < 0) {
	                throw new IllegalArgumentException("Il numero deve essere non negativo.");
	            }

	            double radiceQuadrata = Math.sqrt(numero);
	            System.out.println("La radice quadrata di " + numero + " è: " + radiceQuadrata);

	        } catch (IllegalArgumentException e) {
	            System.err.println("Errore: " + e.getMessage());
	        } catch (Exception e) {
	            System.err.println("Errore: Inserisci un numero valido.");
	        } finally {
	            scanner.close();
	        }
	    }
}
