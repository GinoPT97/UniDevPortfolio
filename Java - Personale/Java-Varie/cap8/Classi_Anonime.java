package com.pellegrinoprincipe;

public class Classi_Anonime
{
    public static void doShape(TwoDShape s)  // mostra l'area e il perimetro di un oggetto TwoDShape
    {
        int a, p;
        a = s.area();
        p = s.perimeter();
        System.out.println("Area: " + a + " Perimetro: " + p);
    }

    public static void doEmployee(Employee e)  // mostra quanto guadagna un Employee
    {
        System.out.println(e + " vorrebbe guadagnare " + e.earning() + "€");
    }

    public static void main(String args[])
    {
        doShape(new TwoDShape() // classe anonima che implementa l'interfaccia TwoDShape
        {
            public int area() { return 0; }
            public int perimeter() { return 0; }
            public void draw() { System.out.println("Draw...X"); }
        });
        class A_Shape implements TwoDShape // metodo alternativo con l'uso di una classe locale
        {
            public int area() { return 1; }
            public int perimeter() { return 1; }
            public void draw() { System.out.println("Draw...Y"); }
        }
        A_Shape i = new A_Shape();
        doShape(i);

        doEmployee(new Employee("Rino", "Principe") // classe anonima che eredita dalla classe Employee
        {
            public int earning() { return 40000;}
        });
        class An_Employee extends Employee // metodo alternativo con l'uso di una classe locale
        {
            public An_Employee(String nome, String cognome) {  super(nome, cognome); }
            public int earning() { return 60000; }
        }
        An_Employee e = new An_Employee("Rino", "Principe");
        doEmployee(e);
    }
}
