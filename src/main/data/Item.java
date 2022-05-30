package main.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import org.json.*;
import org.apache.commons.io.FilenameUtils;

public class Item {
    private Database database;
    private String name;
    private ArrayList<ItemFile> files;

    /**
     * Constructor
     *
     * @param name the name of the item
     */
    public Item(Database database, String name) {
        this.database = database;
        this.name = name;
        files = new ArrayList<ItemFile>();
    }

    /**
     * Constructor
     *
     * @param json a JSON object that represents the item
     */
    public Item(Database database, JSONObject json) throws IllegalArgumentException {
        this.database = database;

        if (!json.has("name"))
            throw new IllegalArgumentException("JSONObject must have a name key");
        if (!json.has("files"))
            throw new IllegalArgumentException("JSONObject must have a files key");

        this.name = json.getString("name");

        files = new ArrayList<ItemFile>();
        JSONArray filesArray = json.getJSONArray("files");
        for (int i = 0; i < filesArray.length(); i++) {
            JSONObject obj = filesArray.getJSONObject(i);
            files.add(new ItemFile(obj));
        }
    }

    /**
     * Returns the name of the item
     *
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an ItemFile to the item
     *
     * @param file the ItemFile to add
     * @param cache when set to true, the file is cached locally, which should be the default option
     */
    public void addFile(ItemFile file, boolean cache) throws IllegalArgumentException {
        if (hasFile(file.getName()))
            throw new IllegalArgumentException("File already exists with same name");

        if (cache) {

        }

        files.add(file);
    }

    /**
     * Adds an ItemFile to the item
     *
     * @param file the ItemFile to add
     */
    public void addFile(ItemFile file) throws IllegalArgumentException {
        addFile(file, true);
    }

    /**
     * Removes an ItemFile from the item
     *
     * @param file the ItemFile to remove
     */
    public void removeFile(ItemFile file) {
        if (file == null)
            return;

        files.remove(file);
    }

    /**
     * Removes an ItemFile from the item
     *
     * @param name the name of the ItemFile to remove
     */
    public void removeFile(String name) {
        removeFile(getFile(name));
    }

    /**
     * Returns the files in the item
     *
     * @return a ItemFile array of the files contained within the object
     */
    public ItemFile[] getFiles() {
        return (ItemFile[])files.toArray();
    }

    /**
     * Returns the file with a given name
     *
     * @param name the name of the ItemFile to retrieve
     * @return an ItemFile with the given name, or null if such ItemFile does not exist
     */
    public ItemFile getFile(String name) {
        for (ItemFile f : files) {
            if (f.getName().equals(name))
                return f;
        }

        return null;
    }

    /**
     * Checks whether or not an ItemFile exists within the item
     *
     * @return true if the ItemFile exists, false otherwise
     */
    public boolean hasFile(String name) {
        return getFile(name) != null;
    }

    /**
     * Caches the files locally
     */
    public void cacheFiles() throws IOException {
        File filesDir = new File(database.getWorkingDirectory() + "/files/");
        if (!filesDir.exists())
            filesDir.mkdir();

        File localDir = new File(database.getWorkingDirectory() + "/files/" + name + "/");
        if (!localDir.exists())
            localDir.mkdir();

        String localDirStr = localDir.getAbsolutePath();

        for (int i = 0; i < files.size(); i++) {
            ItemFile itemFile = files.get(i);
            File srcFile = new File(itemFile.getPath());
            String cachedPath = localDirStr + "/" + FilenameUtils.getName(srcFile.getPath());
            Files.copy(srcFile.toPath(), (new File(cachedPath).toPath()), StandardCopyOption.REPLACE_EXISTING);
            ItemFile cachedItemFile = new ItemFile(itemFile.getName(), (new File(cachedPath).toPath()).toString());
            files.set(i, cachedItemFile);
        }
    }

    /**
     * Returns a JSONObject representing the item
     *
     * @return a JSONObject representing the item
     */
    public JSONObject toJSONObject() {
        JSONArray filesArray = new JSONArray();
        for (ItemFile file : files) {
            filesArray.put(file.toJSONObject());
        }

        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("files", filesArray);

        return object;
    }
}
