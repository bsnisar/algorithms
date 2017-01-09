package alg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HashTableTest {

    HashTable<Integer, Object> table;

    @BeforeEach
    public void init() {
        table = new HashTable<>();
    }

    @Test
    void addIncSize() {
        table.put(10, 10);
        table.put(11, 10);
        assertEquals(table.size(), 2);
    }

    @Test
    void addSameNotIncSize() {
        table.put(10, 10);
        table.put(10, 11);
        assertEquals(table.size(), 1);
    }

    @Test
    void addSameReplace() {
        assertNull(table.put(10, 10));
        assertEquals(table.put(10, 11), 11);
    }
}
