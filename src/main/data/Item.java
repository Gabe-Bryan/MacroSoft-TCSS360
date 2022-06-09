package main.data;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Item {
    private Database database;
    private String name;
    private ArrayList<ItemFile> files;

    private ArrayList<String> tags;

    private String description;

    /**
     * Constructor
     *
     * @param name the name of the item
     */
    public Item(Database database, String name) {
        this(database, name, null, "");
    }

    /**
     * @param database database this item exists in
     * @param name name of the item
     * @param tags tags attatched to this item
     * @param description description of the item
     */
    public Item(Database database, String name, String[] tags, String description){
        this.database = database;
        this.name = name;
        this.files = new ArrayList<ItemFile>();
        if(tags == null) {
            this.tags = new ArrayList<String>();
        }
        else {
            this.tags = new ArrayList<String>();
            for (String tag : tags)
                this.tags.add(tag);
        }
        this.description = description;

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

        this.files = new ArrayList<ItemFile>();
        JSONArray filesArray = json.getJSONArray("files");
        for (int i = 0; i < filesArray.length(); i++) {
            JSONObject obj = filesArray.getJSONObject(i);
            this.files.add(new ItemFile(obj));
        }

        this.tags = new ArrayList<String>();
        if (json.has("tags")) {
            JSONArray tagsArray = json.getJSONArray("tags");
            for (int i = 0; i < tagsArray.length(); i++) {
                JSONObject obj = filesArray.getJSONObject(i);
                this.tags.add(obj.toString());
            }
        }

        if (json.has("description")) {
            this.description = json.getString("description");
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

    public String getDescription() {return this.description;}

    public String[] getTags() {
        return (String[])this.tags.toArray(new String[this.tags.size()]);
    }

    public void addTag(String tag) {
        if (this.database.hasTag(tag))
            this.tags.add(tag.toLowerCase());
    }

    public void removeTag(String tag) {
        this.tags.remove(tag.toLowerCase());
    }

    public boolean hasTag(String tag) {
        for (String str : this.tags) {
            if (str.equalsIgnoreCase(tag))
                return true;
        }

        return false;
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

        this.files.add(file);
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

        this.files.remove(file);
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
        return (ItemFile[]) this.files.toArray(new ItemFile[this.files.size()]);
    }

    /**
     * Returns the file with a given name
     *
     * @param name the name of the ItemFile to retrieve
     * @return an ItemFile with the given name, or null if such ItemFile does not exist
     */
    public ItemFile getFile(String name) {
        for (ItemFile f : this.files) {
            if (f.getName().equals(name))
                return f;
        }

        return null;
    }

    public boolean hasFilePath(String path){
        for (ItemFile f : this.files){
            if(f.getPath().equals(path)){
                return true;
            }
        }
        return false;
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
        File filesDir = new File(this.database.getWorkingDirectory() + "/files/");
        if (!filesDir.exists())
            filesDir.mkdir();

        File localDir = new File(this.database.getWorkingDirectory() + "/files/" + name + "/");
        if (!localDir.exists())
            localDir.mkdir();

        String localDirStr = localDir.getAbsolutePath();

        for (int i = 0; i < this.files.size(); i++) {
            ItemFile itemFile = this.files.get(i);
            File srcFile = new File(itemFile.getPath());
            String cachedPath = localDirStr + "/" + FilenameUtils.getName(srcFile.getPath());
            Files.copy(srcFile.toPath(), (new File(cachedPath).toPath()), StandardCopyOption.REPLACE_EXISTING);
            ItemFile cachedItemFile = new ItemFile(itemFile.getName(), (new File(cachedPath).toPath()).toString());
            this.files.set(i, cachedItemFile);
        }
    }

    /**
     * Returns a JSONObject representing the item
     *
     * @return a JSONObject representing the item
     */
    public JSONObject toJSONObject() {
        JSONArray filesArray = new JSONArray();
        for (ItemFile file : this.files) {
            filesArray.put(file.toJSONObject());
        }

        JSONObject object = new JSONObject();
        object.put("name", this.name);
        object.put("files", filesArray);
        object.put("tags", this.tags);
        object.put("description", this.description);

        return object;
    }

    public String toString(){
        return this.name;
    }
}