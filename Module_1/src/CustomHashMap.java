import java.util.LinkedList;

public class CustomHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 6;
    private LinkedList<KeyValuePair<K, V>>[] buckets;
    private int size;

    public CustomHashMap() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    class KeyValuePair<K, V> {
        public K key;
        public V value;

        public KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    public int hashFunction(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public void put(K key, V value) {
        int hashcode = hashFunction(key);
        int index = hashcode % buckets.length;
        LinkedList<KeyValuePair<K, V>> bucket = buckets[index];
        for (KeyValuePair<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }
        bucket.add(new KeyValuePair<>(key, value));
        size++;
    }

    public V remove(K key) {
        int hashcode = hashFunction(key);
        int index = hashcode % buckets.length;
        LinkedList<KeyValuePair<K, V>> bucket = buckets[index];
        for (KeyValuePair<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                bucket.remove(entry);
                size--;
                return entry.getValue();
            }
        }
        return null;
    }

    public V get(K key) {
        int hashcode = hashFunction(key);
        int index = hashcode % buckets.length;
        LinkedList<KeyValuePair<K, V>> bucket = buckets[index];
        for (KeyValuePair<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return size;
    }
}
