package HashTable;

public interface HashTableInterface<K, V> {
    // Inserisce una coppia chiave-valore nella hashtable
    void put(K key, V value);

    // Restituisce il valore associato alla chiave specificata, o null se la chiave non è presente nella hashtable
    V get(K key);

    // Rimuove la coppia chiave-valore associata alla chiave specificata dalla hashtable
    void remove(K key);

    // Restituisce il numero di coppie chiave-valore presenti nella hashtable
    int size();
}

