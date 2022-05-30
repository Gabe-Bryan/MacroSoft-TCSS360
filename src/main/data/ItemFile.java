package main.data;
import org.json.*;
import org.json.JSONObject;
import java.util.HashMap;

public class ItemFile {
    private String filePath;
    private String fileName;

    /**
     * Constructor
     */
    public ItemFile() {
        filePath = "";
        fileName = "";
    }

    /**
     * Constructor
     *
     * @param fileName the name of the file
     * @param filePath the path of the file
     */
    public ItemFile(String fileName, String filePath){
        this.fileName = fileName;
        this.filePath = filePath;
    }

    /**
     * Constructor
     *
     * @param json a JSONObject representing the file
     */
    public ItemFile(JSONObject json) throws IllegalArgumentException {
        if (!json.has("filePath"))
            throw new IllegalArgumentException("JSONObject must have a filePath key");
        if (!json.has("fileName"))
            throw new IllegalArgumentException("JSONObject must have a fileName key");

        this.filePath = json.getString("filePath");
        this.fileName = json.getString("fileName");
    }

    /**
     * Returns the path of the file
     *
     * @return the path of the file
     */
    public String getPath(){
        return filePath;
    }

    /**
     * Returns the name of the file
     *
     * @return the name of the file
     */
    public String getName(){
        return fileName;
    }

    /**
     * Returns a JSONObject representing the file
     *
     * @return a JSONObject representing the file
     */
    public JSONObject toJSONObject() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("filePath", filePath);
        map.put("fileName", fileName);
        return new JSONObject(map);
    }

}
