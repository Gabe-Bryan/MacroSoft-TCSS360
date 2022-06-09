package main.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private final String test_dir = "/test_dir/";
    private final Database test_db = new Database(test_dir);

    @Test
    public void testToString() {
        String testName = "Test Name";

        Item item = new Item(test_db, testName);

        assertEquals(item.toString(), testName);
    }

    @Test
    public void testGetters() {
        String testName = "Test Name";
        String testDescription = "Test Description";
        String[] testTags = { "tagA", "tagB" };

        Item item = new Item(test_db, testName, testTags, testDescription);

        assertEquals(item.getName(), testName);
        assertEquals(item.getDescription(), testDescription);
        assertEquals(item.getTags().length, testTags.length);
        for (int i = 0; i < item.getTags().length; i++)
            assertEquals(item.getTags()[i], testTags[i]);
    }

    @Test
    public void testAddFile() {
        String testName = "Test Name";

        Item item = new Item(test_db, testName);

        ItemFile testFile = new ItemFile("name", "path");

        assertEquals(item.getFiles().length, 0);

        item.addFile(testFile);
        ItemFile[] files = item.getFiles();
        assertEquals(files.length, 1);
        assertEquals(files[0], testFile);

        item.removeFile(testFile);
        assertEquals(item.getFiles().length, 0);
    }

    @Test
    public void testRemoveFile() {
        String testName = "Test Name";

        Item item = new Item(test_db, testName);

        ItemFile testFile = new ItemFile("name", "path");

        assertEquals(item.getFiles().length, 0);

        item.addFile(testFile);
        assertEquals(item.getFiles().length, 1);
        item.removeFile(testFile);
        assertEquals(item.getFiles().length, 0);

        item.addFile(testFile);
        assertEquals(item.getFiles().length, 1);
        item.removeFile(testFile.getName());
        assertEquals(item.getFiles().length, 0);


        item.addFile(testFile);
        assertEquals(item.getFiles().length, 1);
        item.removeFile("Some Other File Name");
        assertEquals(item.getFiles().length, 1);
        item.removeFile(testFile.getName());
        assertEquals(item.getFiles().length, 0);
    }
}