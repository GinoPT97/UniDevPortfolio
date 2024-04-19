package com.pellegrinoprincipe;

class AClass // classe base
{
    public void printMe(){}
}
class AClass_child_1 extends AClass // classe figlia di AClass
{
    public void printMe(){ System.out.println("child 1"); }
}
class AClass_child_2 extends AClass // classe figlia di AClass
{
    public void printMe(){ System.out.println("child 2"); }
}

public class ArgomentoImmodificabile
{
    public static void aMethod(AClass a_class)
    {
        // cambiamo il riferimento del parametro che punterà ad AClass_child_2 senza che questo
        // cambierà il riferimento dell'argomento
        a_class = new AClass_child_2();
    }

    public static void main(String[] args)
    {
        AClass an_object = new AClass_child_1();
        aMethod(an_object); // passiamo un oggetto che ha un riferimento a AClass_child_1
        an_object.printMe(); // qui an_object punterà sempre all'oggetto della classe A_Class_child_1
    }
}
