package com.pellegrinoprincipe;

class StackGeneric<E>
{
    private final int size;
    private int top;
    private E[] elems;

    public StackGeneric() { this(5); }

    public StackGeneric(int nr)
    {
        size = nr == 0 ? 5 : nr;
        top = -1; // stack inizialmente vuoto
        elems = (E[]) new Object[size];
    }
    public void push(E value) // mette un valore nello stack
    {
        if (top == size - 1)
            System.out.println("Lo stack e' pieno!");
        else
            elems[++top] = value;
    }
    public E pop() // estrae un valore dallo stack
    {
        if (top == -1)
        {
            System.out.println("Lo stack e' vuoto!");
            return null;
        }
        else
            return elems[top--];
    }
}
public class StackGenericClient
{
    public static void main(String[] args)
    {
        Double d[] = { 11.1, 11.2, 8.6 };
        Integer i[] = { 12, 13, 5 };
        Character c[] = { 'a', 'b', 'z' };

        StackGeneric<Double> sd = new StackGeneric<>(3);
        StackGeneric<Integer> si = new StackGeneric<>(3);
        StackGeneric<Character> sc = new StackGeneric<>(3);

        // test push
        for (double e : d)
            sd.push(e);
        for (int e : i)
            si.push(e);
        for (char e : c)
            sc.push(e);
     
        // test pop
        System.out.print("Valori Double: ");
        for (int nr = 0; nr < 3; nr++)
        {
            Double d_tmp = sd.pop();
            System.out.print(d_tmp + " ");
        }

        System.out.print(" | Valori Integer: ");
        for (int nr = 0; nr < 3; nr++)
        {
            Integer i_tmp = si.pop();
            System.out.print(i_tmp + " ");
        }
        
        System.out.print(" | Valori Character: ");
        for (int nr = 0; nr < 3; nr++)
        {
            Character c_tmp = sc.pop();
            System.out.print(c_tmp + " ");
        }
    }
}

