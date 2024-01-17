package Varie;

import java.util.Scanner;

public class LancioDadi {

    // Attributi
    final int numerolanci = 10000;
    int[] lanci = new int [numerolanci];



    public LancioDadi()
    {
        // lanci[] = new int [numeroLanci];
        //lanci = new int [numerolanci];   //vettori

    }

    public void lanciaDado()
    {
        for (int i=0; i<numerolanci; ++i)
        {
            lanci[i] = 1 + (int)((7-1) * Math.random());  //genera un numero casuale fra 1 e 6
        }
    }

    public int contaNumero(int numeroDaContare)
    {
        int i, cont = 0;

        for (i=0; i<numerolanci; ++i)
        {
            if (lanci[i]==numeroDaContare)
            {
                cont = cont + 1;
            }
        }
        return cont;
    }

    public static void main (String[] args)
    {
        String scelta;
        int num1, num2, num3, num4, num5, num6;

        Scanner in = new Scanner(System.in);

        /*System.out.println("Quante volte vuoi lanciare il dado?");
        numeroLanci =  in.nextInt();*/
        LancioDadi ld = new LancioDadi();

        do
        {
            ld.lanciaDado();
            num1 = ld.contaNumero(1);
            num2 = ld.contaNumero(2);
            num3 = ld.contaNumero(3);
            num4 = ld.contaNumero(4);
            num5 = ld.contaNumero(5);
            num6 = ld.contaNumero(6);

            System.out.println("Il numero 1 è uscito "+num1+" volte");
            System.out.println("Il numero 2 è uscito "+num2+" volte");
            System.out.println("Il numero 3 è uscito "+num3+" volte");
            System.out.println("Il numero 4 è uscito "+num4+" volte");
            System.out.println("Il numero 5 è uscito "+num5+" volte");
            System.out.println("Il numero 6 è uscito "+num6+" volte");

            System.out.println("Vuoi giocare ancora? (s/n)");
            scelta = in.next();
        } while(scelta.equals("s")  || scelta.equals("S"));
    }
}