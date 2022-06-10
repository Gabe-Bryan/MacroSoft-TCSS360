package main.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter for the database object
 *
 *
 */
public class DatabaseFilter {

    /**
     * names of the filter
     */
    private ArrayList<String> names;

    /**
     * tags of the filter
     */
    private ArrayList<String> tags;

    private boolean fuzzySearchEnabled;

    /**
     * Constructor
     */
    public DatabaseFilter() {
        names = new ArrayList<String>();
        names = new ArrayList<String>();
        fuzzySearchEnabled = false;
    }

    /**
     * add name to the filter
     * @param name the name that wishes to put into the filter
     */
    public void addNameFilter(String name) {
        names.add(name);
    }

    /**
     * add tag to the filter
     * @param tag the tag that wishes to put into the filter
     */
    public void addTagFilter(String tag) {
        tags.add(tag);
    }

    /**
     * set the status for fuzzy search
     * @param fuzzySearchEnabled true to enable, otherwise false
     */
    public void setFuzzySearchEnabled(boolean fuzzySearchEnabled) {
        this.fuzzySearchEnabled =  fuzzySearchEnabled;
    }

    /**
     * getter for name filters
     * @return name filter
     */
    public String[] getNameFilters() {
        return (String[])names.toArray(new String[names.size()]);
    }

    /**
     * getter for tag filter
     * @return tag filter
     */
    public String[] getTagFilters() {
        return (String[])tags.toArray(new String[names.size()]);
    }

    /**
     * get the status of fuzzysearch
     * @return status of fuzzysearch
     */
    public boolean getFuzzySearchEnabled() {
        return fuzzySearchEnabled;
    }
}
