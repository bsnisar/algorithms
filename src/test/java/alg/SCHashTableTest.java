package alg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    void addWithRehash() {
        for (int i = 0; i < 150; i++) {
            table.add(i, 10);
        }

        assertEquals(150, table.size());
    }
}
