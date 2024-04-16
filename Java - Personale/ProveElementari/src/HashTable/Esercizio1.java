package HashTable;

public class Esercizio1 {

    public static void main(String[] args) {
        // Crea un'istanza di HashTable con una capacità iniziale di 10
        HashTable<String, Integer> hashTable = new HashTable<>(10);

        // Definisci un array di chiavi e un array di valori corrispondenti
        String[] chiavi = {"mela", "banana", "arancia", "uva", "mango", "pera", "ciliegia", "pesca", "ananas", "fragola"};
        Integer[] valori = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};

        // Inserisci le coppie chiave-valore nell'hashtable
        for (int i = 0; i < 5; i++) {
            hashTable.put(chiavi[i], valori[i]);
        }

        // Test 1: Recupera il valore associato alla chiave "mela"
        testGet(hashTable, "mela");

        // Test 2: Recupera il valore associato alla chiave "arancia"
        testGet(hashTable, "arancia");

        // Test 3: Recupera il valore associato alla chiave "banana"
        testGet(hashTable, "banana");

        // Test 4: Recupera il valore associato alla chiave "uva"
        testGet(hashTable, "uva");

        // Test 5: Recupera il valore associato alla chiave "mango"
        testGet(hashTable, "mango");
    }

    // Metodo per eseguire il test e stampare il risultato
    private static void testGet(HashTable<String, Integer> hashTable, String key) {
        Integer value = hashTable.get(key);
        if (value != null) {
            System.out.println("Il valore per la chiave '" + key + "' è: " + value);
        } else {
            System.out.println("La chiave '" + key + "' non è stata trovata nell'hashtable.");
        }
    }
}

