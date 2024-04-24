package biblioteca;

public class BibTest {
  public static void main( String[] a ){
     try{
	Testo t0 = new Testo( "Slam", 2010, new String[] {} );
     }
     catch( Error e ) {
	System.out.println( e );
     };
	Testo t1 = new Testo( "Slam", 2010, new String[] {"H1"} );
	Testo t2 = new Testo( "La Divina Commedia", 1910,
                               new String[] {"D1","D2","D3"} );
	System.out.println( t2 );

	Autore dante = new Autore("Dante","Alighieri");
	Libro divina = new Libro("La Divina Commedia", 1910,
				new String[] {"D1","D2","D3"}, 
				new Autore[] {dante} );
	System.out.println( divina );

  }
}
