package alg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RBTreeTest {
    RBTree<Integer, Object> tree;

    @BeforeEach
    public void init() {
        tree = new RBTree<>(COMPARATOR);
    }

    private static final int VALUE = 1;

    private static final Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    };

    @Test
    public void sizeOfEmptyIsZero() {
        assertEquals(0, tree.size());
    }

    @Test
    public void newTreeIsEmpty() {
        assertEquals(true, tree.isEmpty());
    }

    @Test
    public void addElemAndSizeInc() {
        tree.add(1, VALUE);
        tree.add(50, VALUE);
        tree.add(3, VALUE);
        tree.add(18, VALUE);
        assertEquals(4, tree.size());
    }

    @Test
    public void excludeDuplicateAdds() {
        tree.add(50, VALUE);
        tree.add(50, VALUE);
        tree.add(1, VALUE);
        tree.add(50, VALUE);
        assertEquals(2, tree.size());
    }

    @Test
    public void keysAreOrdered() {
        tree.add(12, VALUE);
        tree.add(10, VALUE);
        tree.add(4, VALUE);
        tree.add(42, VALUE);
        tree.add(11, VALUE);
        tree.add(55, VALUE);

        List<Integer> expectedOrder = Arrays.asList(4, 10, 11, 12, 42, 55);
        List<Integer> treeKeysOrdered = new ArrayList<>();
        for (Integer key : tree.keysSet()) {
            treeKeysOrdered.add(key);
        }

        assertEquals(expectedOrder, treeKeysOrdered);
    }

    @Test
    public void cgeckRootSubtreeAfterInsertions() {
        tree.add(8, VALUE);
        tree.add(18, VALUE);
        tree.add(5, VALUE);
        tree.add(15, VALUE);
        tree.add(17, VALUE);
        tree.add(18, VALUE);
        tree.add(25, VALUE);
        tree.add(40, VALUE);

        assertEquals(Integer.valueOf(8), tree.root.key);
        assertEquals(Integer.valueOf(5), tree.root.left.key);
        assertEquals(Integer.valueOf(17), tree.root.right.key);

        assertEquals(RBTree.Color.BLACK, tree.root.color, "root is BLACK");
        assertEquals(RBTree.Color.RED, tree.root.right.color, "right is RED");
        assertEquals(RBTree.Color.BLACK, tree.root.left.color, "left is BLACK");
    }
}