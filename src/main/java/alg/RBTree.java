package alg;

import java.util.*;

import static alg.RBTree.Color.BLACK;
import static alg.RBTree.Color.RED;


public class RBTree<K, V> {

    enum Color {
        RED, BLACK
    }

    private final Comparator<K> comparator;
    private int size;

    Node<K, V> root;

    public RBTree(Comparator<K> comparator) {
        this.comparator = comparator;
    }


    static final class Node<K, V> {
        final K key;
        V value;

        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;

        Color color = RED;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return color + "(" + key + "[" + value + "])";
        }
    }


    public K remove(K elem) {
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Set<K> keysSet() {
        return new AbstractSet<K>() {
            @Override
            public boolean contains(Object o) {
                return RBTree.this.contains(o);
            }

            @Override
            public Iterator<K> iterator() {
                return new KeyIterator();
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    public void add(K key, V value) {
        Node<K, V> node = insertNode(key, value);
        if (node != null) {
            rebalanceTree(node);
        }
    }

    public boolean contains(Object key) {
        Node<K, V> node = root;
        while (node != null) {
            @SuppressWarnings("unchecked")
            int compare = comparator.compare((K) key, node.key);
            if (compare == 0) {
                return true;
            } else if (compare > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        return false;
    }

    /**
     * Insert node into tree if possible.
     *
     * @param key   node key
     * @param value node value
     */
    final Node<K, V> insertNode(K key, V value) {
        Node<K, V> parent = null, head = root;
        int compare = 1;
        while (head != null) {
            parent = head;
            compare = comparator.compare(key, head.key);

            if (compare > 0) {
                head = head.right;
            } else if (compare < 0) {
                head = head.left;
            } else {
                // Override value for already exists key, no re-balancing required.
                head.value = value;
                return null;
            }
        }

        size++;
        Node<K, V> node = new Node<>(key, value);
        node.parent = parent;
        if (parent == null) {
            root = node;
        } else if (compare > 0) {
            parent.right = node;
        } else {
            parent.left = node;
        }

        // insert new node, need to re-balance tree
        return node;
    }

    /*
     * Let K will be inserted element, P be K's parent.
     * R(..) - red node,
     * B(..) - black node
     *
     * Case 1:
     * In the first case, the node we've inserted is red, it's parent is red, and it's parent sibling is red.
     * This mean that P has red child that violates the red property. This can be fixed to recoloring
     * node's grandparent with the coloring of the inserted node's parent and its parent's sibling.
     *
     * This case might need to continue to be fixed up through the root of the tree,
     * though, because the inserted node's grandparent may have a parent who is red.
     *
     * Action:
     *     B(G)              R(G)
     *    /    \            /    \
     *  R(P)  R(S)   ->   B(P)   B(S)
     *     \                \
     *    R(K)              R(K)
     *
     * Case 2:
     *
     * Case 2 is a case that will be transformed into case 3.
     * The parent P is red but the uncle U is black. Case 2 uses a left rotation because the nodes
     * involved are rotated counter-clockwise.
     *
     *    B(G)                              B(G)
     *   /                                 /
     * R(P)        ROTATE_LEFT(P)      R(K)
     *   \                             /
     *   R(K)                       R(P)
     *
     * Case 3:
     * ROTATE_RIGHT(G), and then switch colors of G and P.
     *
     *        B(G)                B(P)
     *       /    \              /    \
     *     R(P)  B(S)   ->     R(K)  R(G)
     *    /                              \
     *  R(K)                            B(S)
     *
     * Case 2 and 3 represent situation when we consider left subtree of grandparent.
     * Other cases are mirrored them (consider right subtree)
     */
    private void rebalanceTree(Node<K, V> inserted) {
        Node<K, V> node = inserted;
        // Traverse tree from inserted to root.
        // Check case where inserted node is rend, and its parent (P) also is red.
        // This mean that P has red child that violates the red property.
        while (hasColor(parentOf(node), RED) && node != root) {

            // In order to handle this double-red situation,
            // we will need to consider the color of G's other child, that is, P's sibling, S.

            Node<K, V> parent = parentOf(node);
            Node<K, V> grandParent = parentOf(parent);
            // consider left subtree
            if (parent == leftOf(grandParent)) {
                Node<K, V> uncle = rightOf(grandParent);

                if (hasColor(uncle, RED)) {
                    // this is Case 1
                    paintTo(parent, BLACK);
                    paintTo(uncle, BLACK);
                    paintTo(grandParent, RED);
                    node = grandParent;
                } else {
                    if (node == rightOf(parent)) {
                        // This is Case 2
                        node = parent;
                        rotateLeft(node);
                        // reassign after rotation
                        parent = parentOf(node);
                        grandParent = parentOf(parent);
                    }
                    paintTo(parent, BLACK);
                    paintTo(grandParent, RED);
                    rotateRight(grandParent);
                }
                // consider right subtree
            } else {
                Node<K, V> uncle = leftOf(grandParent);
                if (hasColor(uncle, RED)) {
                    // Case 1
                    paintTo(node.parent, BLACK);
                    paintTo(uncle, BLACK);
                    paintTo(grandParent, RED);
                    node = grandParent;
                } else {
                    if (node == leftOf(parent)) {
                        // Case 2
                        node = node.parent;
                        rotateRight(node);
                        // reassign after rotation
                        parent = parentOf(node);
                        grandParent = parentOf(parent);
                    }
                    //Case 3
                    paintTo(parentOf(node), BLACK);
                    paintTo(grandParent, RED);
                    rotateLeft(grandParent);
                }
            }
        }
        root.color = BLACK;
    }

    //        |                             |
    //       {P}      ROTATE-RIGHT(P)      {K}
    //      /   \          ->            /    \
    //    {K}   {s}                    {l}    {P}
    //   /  \         ROTATE-LEFT(K)         /   \
    // {l}  {r}             <-             {r}   {s}
    final void rotateRight(Node<K, V> node) {
        if (node == null) {
            return;
        }

        Node<K, V> sibling = node.left;
        Node<K, V> parent = node.parent;

        node.left = sibling.right;
        if (sibling.right != null) {
            sibling.right.parent = node;
        }

        sibling.parent = parent;
        if (parent == null) {
            root = sibling;
        } else {
            if (parent.left == node) {
                parent.left = sibling;
            } else {
                parent.right = sibling;
            }
        }
        sibling.right = node;
        node.parent = sibling;
    }

    final void rotateLeft(Node<K, V> node) {
        if (node == null) {
            return;
        }
        Node<K, V> sibling = node.right;
        Node<K, V> parent = node.parent;

        node.right = sibling.left;
        if (sibling.left != null) {
            sibling.left.parent = node;
        }

        sibling.parent = parent;
        if (parent == null) {
            root = sibling;
        } else {
            if (parent.left == node) {
                parent.left = sibling;
            } else {
                parent.right = sibling;
            }
        }
        sibling.left = node;
        node.parent = sibling;
    }

    private static <K, V> boolean hasColor(Node<K, V> node, Color color) {
        return node != null && node.color == color;
    }

    private static <K, V> void paintTo(Node<K, V> parent, Color color) {
        if (parent != null) {
            parent.color = color;
        }
    }

    private static <K, V> Node<K, V> parentOf(Node<K, V> node) {
        return node != null ? node.parent : null;
    }

    private static <K, V> Node<K, V> rightOf(Node<K, V> node) {
        return node != null ? node.right : null;
    }

    private static <K, V> Node<K, V> leftOf(Node<K, V> node) {
        return node != null ? node.left : null;
    }


    private abstract class NodeInOrderIterator<T> implements Iterator<T> {
        private Node<K, V> next;

        private NodeInOrderIterator() {
            next = root;
            if (next == null) {
                return;
            }
            while (next.left != null) {
                next = next.left;
            }
        }

        final Node<K, V> nextNode() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<K, V> current = next;
            // if you can walk right, walk right, then fully left.
            // otherwise, walk up until you come from left.
            if (next.right != null) {
                next = next.right;

                while (next.left != null)
                    next = next.left;

                return current;
            } else {
                while (true) {
                    Node<K, V> parent = next.parent;
                    if (parent == null) {
                        next = null;
                        return current;
                    }

                    if (parent.left == next) {
                        next = parent;
                        return current;
                    }
                    next = parent;
                }
            }
        }

        @Override
        public final boolean hasNext() {
            return next != null;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class KeyIterator extends NodeInOrderIterator<K> implements Iterator<K> {
        @Override
        public K next() {
            Node<K, V> node = nextNode();
            return node.key;
        }
    }
}