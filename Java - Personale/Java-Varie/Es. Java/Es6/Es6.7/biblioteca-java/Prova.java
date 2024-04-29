package biblioteca;

public class Prova {

  public static void main( String[] arg ) {
	A a;
	B b = new B();
	b.set("B");
	a = b;
	System.out.println( a.get() );
	System.out.println( b.get() );
	b.set(new Integer(4));
	System.out.println( a.get() );
//	System.out.println( b.get() );

	a.y = new Integer( 3 );
	b.y = "C";
	System.out.println( b.y );
	System.out.println( b.y );

  }
}

class A {
  protected Object x;
  Object y;

  public void set( Object v ){ x=v; }

  public Object get(){ return x; }
}

class B extends A {
//  protected String x;
  String y;

  public void set( String v ){ x=v; }

  public String get(){ return (String) x; }
}

