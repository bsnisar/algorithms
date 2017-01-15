package alg;


public interface Tree<K,V> {

    int size();

    /**
     * Insert new node into tree.
     * @param key key
     * @param val value.
     */
    void add(K key, V val);

}
