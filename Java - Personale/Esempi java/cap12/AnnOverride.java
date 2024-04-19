package com.pellegrinoprincipe;

class Base
{
    public void M1() {}
}

class Derived extends Base
{
    // qui nessun errore perché il metodo M1  è presente nella sua classe base
    @Override
    public void M1() {}
}

class Derived2 extends Base
{
    // qui errore perché il metodo M non è presente nella sua classe base
    @Override
    public void M() {}
}

public class AnnOverride
{
    public static void main(String[] args) {}
}
