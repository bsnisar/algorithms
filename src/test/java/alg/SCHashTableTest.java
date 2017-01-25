package alg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class SCHashTableTest {

    private SCHashTable<Integer, Object> table;

    @BeforeEach
    public void init() {
        table = new SCHashTable<>();
    }

    @Test
    void addIncSize() {
        table.add(10, 10);
        table.add(11, 10);
        assertEquals(table.size(), 2);
    }

    @Test
    void addSameNotIncSize() {
        table.add(10, 10);
        table.add(10, 11);
        assertEquals(table.size(), 1);
    }

    @Test
    void addSameReplace() {
        assertNull(table.add(10, 10));
        assertEquals(10, table.add(10, 11));
    }

    @Test
    public void addWithRehash() {
        for (int i = 0; i < 150; i++) {
            table.add(i, 10);
        }

        assertEquals(150, table.size());
    }

    @Test
    public void addAndTestForContains() {
        table.add(120, 10);
        table.add(1000, 10);
        table.add(-749, 10);

        assertTrue(table.hasKey(1000));
        assertTrue(table.hasKey(-749));
        assertFalse(table.hasKey(10001));
        assertFalse(table.hasKey(50));
    }
}
