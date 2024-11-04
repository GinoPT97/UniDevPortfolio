package biblioteca;

class Testo {
  private String titolo;
  private int anno;
  private Tomo tomo[];

  Testo( String titolo, int anno, String[] colloc ){
    if( colloc.length == 0 ) throw new Error( titolo+": almeno un tomo!");
    this.titolo = titolo;
    this.anno   = anno;
    tomo = new Tomo[ colloc.length ];
    for( int i = 0; i < colloc.length; i++ )
      tomo[i] = new Tomo( colloc[i] );
  }

  public String toString(){
    String s = "Testo[" + titolo + ";" + anno + ";" + tomo[0].getColl();
    for( int i=1; i<tomo.length; i++ )
      s += "-" + tomo[i].getColl() ;
    return s+"]";
  }
}

class Tomo {
  private String collocazione;

  Tomo( String collocazione ){
    this.collocazione = collocazione;
  }

  public String getColl(){
    return collocazione;
  }
}

class TomoRivista extends Tomo {
  private Autore autore[];

  TomoRivista( String collocazione, Autore autore[] ){
    super( collocazione );
    if( autore.length == 0 ) throw new Error("zero autori!");
    this.autore = autore;
  }
}

class Libro extends Testo {
  private Autore autore[];

  Libro( String titolo, int anno, String coll[], Autore autore[] ){
    super( titolo, anno, coll );
    if( autore.length == 0 ) throw new Error("zero autori!");
    this.autore = autore;
  }

  public String toString(){
    String s = super.toString() + " by " + autore[0];
    for( int i=1; i<autore.length; i++ )
      s += ", "+autore[i];
    return s;
  }
}

class Rivista extends Testo {
  // bisognerebbe permettere di aggiungere tomi periodicamente
  private TomoRivista tomo[];

  Rivista( String titolo, int anno, String coll[], Autore autore[][] ){
    // cosi' si duplicano i tomi
    super( titolo, anno, coll );
    if( coll.length != autore.length ) throw new Error("mismatch!");
    tomo = new TomoRivista[ coll.length ];
    for( int i=0; i<coll.length; i++ ) {
      if( autore[i].length == 0 ) throw new Error("zero autori!");
      tomo[i] = new TomoRivista( coll[i], autore[i] );
    }
  }
}
/*
Per non duplicare i tomi dovrei fare un costruttore privato in Testo che non inizializza i tomi; poi costruire i tomi di tipo TomoRivista.  Anche ulteriori metodi per aggiungere tomi dovrebbero crearli di tipo TomoRivista.
Eventuali metodi per ottenere i tomi dovrebbero restituire oggetti di tipo Tomo nella superclasse e oggetti di tipo TomoRivista nella sottoclasse. Il dynamic method dispatch garantisce il buon funzionamento.
Questo e' compatibile sia con due dichiarazioni di tomo[] entrambe private e con tipi diversi, sia con una singola dichiarazione protected. Il difetto della prima versione e' che alcuni metodi potrebbero usare la versione super (non inizializzata/mantenuta); il difetto della seconda e' che bisogna fare alcuni downcast espliciti.
*/


