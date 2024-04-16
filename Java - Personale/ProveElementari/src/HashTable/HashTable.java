package HashTable;

import java.util.LinkedList;

public class HashTable<K, V> implements HashTableInterface<K, V> {
    private LinkedList<Entry<K, V>>[] table;
    private int capacity;
    private int size;

    public HashTable(int capacity) {
        this.capacity = capacity;
        this.table = new LinkedList[capacity];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        // Gestione delle collisioni
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        // Se non c'è una collisione, aggiungiamo l'elemento
        table[index].add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        if (table[index] != null) {
            for (Entry<K, V> entry : table[index]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        return null;
    }

    @Override
    public void remove(K key) {
        int index = hash(key);
        if (table[index] != null) {
            table[index].removeIf(entry -> entry.key.equals(key));
            size--;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private int hash(K key) {
        // Implementazione della funzione di hash appropriata
        // Potrebbe essere diversa a seconda delle esigenze
        return Math.abs(key.hashCode() % capacity);
    }

    private static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}


