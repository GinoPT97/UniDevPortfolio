package HashTable;

import java.util.LinkedList;

public class HashTable2<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    private static final int DEFAULT_CAPACITY = 10;
    private LinkedList<Entry<K, V>>[] table;

    private int size;

    public HashTable2() {
        this(DEFAULT_CAPACITY);
    }

    public HashTable2(int capacity) {
        this.table = new LinkedList[capacity];
        this.size = 0;
    }

    public V get(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> list = table[index];
        if (list != null)
			for (Entry<K, V> entry : list)
				if (entry.key.equals(key))
					return entry.value;
        return null;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public void put(K key, V value) {
        int index = hash(key);
        if (table[index] == null)
			table[index] = new LinkedList<>();

        // Controlla se la chiave esiste già e aggiorna il valore
        for (Entry<K, V> entry : table[index])
			if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }

        // Se la chiave non esiste, aggiungila al bucket
        table[index].add(new Entry<>(key, value));
        size++;
    }

    public void remove(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> list = table[index];
        if (list != null) {
            list.removeIf(entry -> entry.key.equals(key));
            size--;
        }
    }

    public int size() {
        return size;
    }
}
