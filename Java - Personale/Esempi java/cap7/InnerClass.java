package com.pellegrinoprincipe;

class Host
{
    private String show1 = "Valore di 'a' della classe LC definita nel metodo local della classe Host = ";
    private String show2 = "Valore di 'abc' del metodo local della classe Host mostrato dal metodo show\n"
            + "della classe LC definita nel metodo Local della classe Host = ";
    private int outer_x = 100;

    void doIt()
    {
        Guest inner = new Guest(); // istanza classe annidata
        inner.display();
    }

    void local()
    {
        final int abc = 11;

        // definizione di una classe locale ad una funzione
        class LC
        {
            int a;
            LC() {a = 1000;}
            void show() {System.out.println(show1 + a + "\n" + show2 + abc + "\n");}
        }

        LC a_local = new LC();
        a_local.show();
    }

    // DEFINIZIONE classe ANNIDATA
    class Guest
    {
        private String show1 = "Valore 'outer_x' di Host mostrato dal metodo display della classe"
                + " Guest ad esso annidata = ";

        void display()
        {
            System.out.print(show1 + outer_x + "\n");
        }
    }
}

public class InnerClass
{
    public static void main(String args[])
    {
        Host outer = new Host();
        outer.doIt();
        outer.local();
    }
}
