package main.data;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private final string test_dir = "/test_dir/";

    public DatabaseTest() {

    }

    @Test
    public void constructor() {
        Database db = new Database(test_dir);
        assertEquals(db.getItems().length, 0);
        assertEquals(db.getWorkingDirectory(), test_dir);
    }

    @Test
    public void createItem() {
        Database db = new Database(test_dir);

        Item item = db.createItem("myItem");
        assert(db.hasItem("myItem"));
        assertEquals(item, db.getItem("myItem"));
        assertEquals(db.getItems().length, 1);
    }

}