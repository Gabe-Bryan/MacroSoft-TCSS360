package main.data;

import java.io.IOException;
import java.util.*;
import java.io.File;
import org.json.*;
import java.io.*;

public class Database {
    private String workingDir;
    private ArrayList<Item> items;

    /**
     * Constructor
     *
     * @param workingDir the working directory of the database
     */
    public Database(String workingDir) {
        this.workingDir = workingDir;
        items = new ArrayList<Item>();
    }

    /**
     * Constructor
     *
     * @param json a JSON object that represents the database
     */
    public Database(JSONObject json) throws IllegalArgumentException {
        if (!json.has("workingDir"))
            throw new IllegalArgumentException("JSONObject must have a workingDir key");
        if (!json.has("items"))
            throw new IllegalArgumentException("JSONObject must have a items key");

        this.workingDir = json.getString("workingDir");

        JSONArray itemsArray = json.getJSONArray("items");
        items = new ArrayList<Item>();
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemObj = itemsArray.getJSONObject(i);
            items.add(createItem(itemObj));
        }
    }

    /**
     * Returns the working directory string
     *
     * @return the working directory
     */
    public String getWorkingDirectory() {
        return workingDir;
    }

    /**
     * Adds an item to the database
     *
     * @param item the item that is being added
     */
    private void addItem(Item item) throws IllegalArgumentException {
        if (hasItem(item.getName()))
            throw new IllegalArgumentException("Item with same name already exists");

        items.add(item);
    }

    /**
     * Creates a new item
     *
     * @param name the name of the item to be created
     */
    public Item createItem(String name) {
        Item item = new Item(this, name);
        addItem(item);
        return item;
    }

    /**
     * Creates a new item
     *
     * @param json a JSONObject representing the item
     */
    public Item createItem(JSONObject json) {
        Item item = new Item(this, json);
        addItem(item);
        return item;
    }

    /**
     * Returns an item contains within the database
     *
     * @param name the name of the item to be retrieved
     * @return the retrieved item, or null if the item does not exist
     */
    public Item getItem(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Returns whether or not the item is contained within the database
     *
     * @param name the name of the item that is being checked for existence
     * @return true if the item is exists, false if otherwise
     */
    public boolean hasItem(String name) {
        return getItem(name) != null;
    }

    /**
     * Returns the items in the database
     *
     * @return an array of items
     */
    public Item[] getItems() {
        return (Item[])items.toArray();
    }

    /**
     * Returns the database represented with a JSONObject
     *
     * @return a JASONObject
     */
    public JSONObject toJSONObject() {
        List<JSONObject> itemsArray = new ArrayList<JSONObject>();
        for (Item item : items)
            itemsArray.add(item.toJSONObject());
        JSONObject obj = new JSONObject();
        obj.put("workingDir", workingDir);
        obj.put("items", new JSONArray(itemsArray));
        return obj;
    }

    /**
     * Saves the database JSON file
     */
    public void save() throws IOException {
        File dir = new File(workingDir);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }

        File dbFile = new File(workingDir + "/database.json");
        if (dbFile.exists()) {
            System.out.println("dbFile exists");
        }
        else
            System.out.println("dbFile does not exist");

        JSONObject json = toJSONObject();

        FileWriter fw = new FileWriter(dbFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(json.toString(2));
        bw.close();
    }

    /**
     * Caches all the files locally
     */
    public void cacheAllFiles() throws IOException {
        for (Item item : items) {
            item.cacheFiles();
        }
    }
}
