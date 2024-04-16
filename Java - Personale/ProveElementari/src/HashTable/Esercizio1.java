package HashTable;

public class Esercizio1 {

  public static void main(String[] args) {
    // Crea un'istanza di HashTable con una capacità iniziale di 10
    HashTable<String, Integer> hashTable = new HashTable<>(10);

    // Definisci un array di chiavi e un array di valori corrispondenti
    String[] chiavi = {"mela", "banana", "arancia", "uva", "mango"}; 
    Integer[] valori = {10, 20, 30, 40, 50};

    // Inserisci le coppie chiave-valore nell'hashtable
    for (int i = 0; i < chiavi.length; i++) {
      hashTable.put(chiavi[i], valori[i]);
    }

    // Recupera il valore associato alla chiave "mela"
    String chiave = "mela"; // Sostituisci con la chiave desiderata
    Integer valore = hashTable.get(chiave);

    // Controlla se la chiave esiste e stampa il valore corrispondente
    if (valore != null) {
      System.out.println("Il valore per la chiave '" + chiave + "' è: " + valore);
    } else {
      System.out.println("La chiave '" + chiave + "' non è stata trovata nell'hashtable.");
    }
  }
}
