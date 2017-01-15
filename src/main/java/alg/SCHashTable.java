package alg;

import java.lang.reflect.Array;

/**
 * Separate Chaining Hash Table.
 */
public class SCHashTable<K, V> {

    @SuppressWarnings("unchecked")
    Node<K, V>[] buckets = (Node<K, V>[]) Array.newInstance(Node.class, 10);

    private int size;

    private float threshold = buckets.length;

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V val) {
            this.key = key;
            this.value = val;
        }
    }

    public int size() {
        return size;
    }

    /**
     * Add (Key, Value) and return old value, it it was associated with given key previously.
     *
     * @param key key
     * @param val value
     * @return old value or {@code null}.
     */
    public V add(K key, V val) {
        V old = null;
        int hash = hash(key);
        int idx = index(hash, buckets.length);
        Node<K, V> node = buckets[idx], last = node;
        while (node != null && old == null) {
            old = tryReplaceValue(node, key, val);
            last = node;
            node = node.next;
        }

        if (old == null) {
            size++;
            addMapping(idx, last, buckets, new Node<>(key, val));
        }

        if (needRehash()) {
            // System.out.println("start rehash");
            doRehash();
        }
        return old;
    }

    private V tryReplaceValue(Node<K, V> node, K key, V v) {
        if (keyEqualTo(node, key)) {
            V tmp = node.value;
            node.value = v;
            return tmp;
        }
        return null;
    }

    private boolean hasNext(Node<K, V> node) {
        return node != null && node.next != null;
    }

    private boolean keyEqualTo(Node<K, V> bucket, K key) {
        return bucket.key.equals(key);
    }

    /*
    * The code masks off the sign bit (to turn the 32-bit integer into a 31-bit nonnegative integer)
    * and then computing the remainder when dividing by M, as in modular hashing.
    */
    private int index(int hash, int bucketsSize) {
        return (hash & 0x7fffffff) % bucketsSize;
    }

    /*
     * A hash procedure must be deterministic—meaning
     * that for a given input value it must always generate the same hash value.
     */
    private int hash(K key) {
        return key.hashCode();
    }

    private void addMapping(int idx, Node<K, V> bucket, Node<K, V>[] buckets, Node<K, V> newNode) {
        if (bucket == null) {
            buckets[idx] = newNode;
        } else {
            bucket.next = newNode;
        }
    }

    @SuppressWarnings("unchecked")
    private void doRehash() {
        Node<K, V>[] newBuckets = (Node<K, V>[]) Array.newInstance(Node.class, buckets.length * 2);
        for (Node<K, V> bucket : buckets) {
            if (bucket == null) {
                continue;
            }

            do {
                Node<K, V> next = bucket.next;
                bucket.next = null;

                int hash = hash(bucket.key);
                int idx = index(hash, newBuckets.length);
                Node<K, V> newBucket = newBuckets[idx];

                while (hasNext(newBucket))
                    newBucket = newBucket.next;

                addMapping(idx, newBucket, newBuckets, bucket);

                bucket = next;
            } while (bucket != null);
        }

        buckets = newBuckets;
        threshold = buckets.length * 0.75f;
    }

    private boolean needRehash() {
        return size > threshold;
    }
}
