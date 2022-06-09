package main.data;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

class ItemFileTest {
    @Test
    public void testGetters() {
        String name = "test name";
        String path = "test path";

        ItemFile file = new ItemFile(name, path);

        assertEquals(file.getName(), name);
        assertEquals(file.getPath(), path);
    }

    @Test
    public void testToString() {
        String name = "test name";
        String path = "test path";

        ItemFile file = new ItemFile(name, path);

        assetEquals(file.toString(), name);
    }

}