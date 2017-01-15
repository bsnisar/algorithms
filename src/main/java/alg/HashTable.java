package alg;

public class HashTable<K,V> {

    private Node<K, V>[] buckets;

    private int size;

    private float threashhold = 70f;

   private static class Node<K,V> {
       K key;
       V value;
       Node<K,V> next;

       Node(K key, V val) {
            this.key = key;
           this.value = val;
       }
   }

    V add(K key, V v) {
        int hash = hash(key);
        int idx = index(hash, buckets.length);
        Node<K,V> newNode = new Node<>(key, v);
        Node<K, V> bucket = buckets[idx];
        if (bucket != null) {

            if (keyEqualTo(bucket, key)) {
                bucket.value = v;
                return null;
            }
        }

        size++;
        return null;
    }

    private boolean keyEqualTo(Node<K, V> bucket, K key) {
        return bucket.key.equals(key);
    }

    private int index(int hash, int bucketsSize) {
        return hash % bucketsSize;
    }

    private int hash(K key) {
        return 0;
    }

    int size() {
        return 0;
    }
}
