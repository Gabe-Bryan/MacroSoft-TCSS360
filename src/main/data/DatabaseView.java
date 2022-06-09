package main.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * DatabaseView class
 *
 *
 * @author Alex Larsen
 * @version Spring 2022
 */
public class DatabaseView {
    private Database database;
    private DatabaseFilter filter;

    private ArrayList<Item> items;

    /**
     * Constructor
     * @param database the database of the application
     * @param filter the filter of the database
     */
    public DatabaseView(Database database, DatabaseFilter filter) {
        this.database = database;
        this.filter = filter;
    }

    /**
     * initialize the member items by getting the item list from
     * the database and set the filter
     */
    private void retrieve() {
        items = new ArrayList<Item>(Arrays.asList(database.getItems()));

        if (filter.getFuzzySearchEnabled())
            filterByName();
        else
            filterByNameFuzzy();

        filterByTags();
    }

    /**
     * filter the DatabaseFilter by using name
     */
    private void filterByName() {
        String[] nameFilters = filter.getNameFilters();
        if (items.isEmpty())
            return;

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            for (String str : nameFilters) {
                if (item.getName().equalsIgnoreCase(str) == false) {
                    items.remove(i);
                    i = 0;
                    continue;
                }
            }
        }
    }

    /**
     * filter the databasefilter by using fuzzy name
     */
    private void filterByNameFuzzy() {
        String[] nameFilters = filter.getNameFilters();
        if (items.isEmpty())
            return;

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            for (String str : nameFilters) {
                if (
                        item.getName().equalsIgnoreCase(str) == false
                        && item.getName().contains(str) == false
                ) {
                    items.remove(i);
                    i = 0;
                    continue;
                }
            }
        }
    }

    /**
     * filter the databasefilter using tag
     */
    private void filterByTags() {
        String[] tagFilters = filter.getTagFilters();
        if (items.isEmpty())
            return;

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            for (String str : tagFilters) {
                if (item.hasTag(str) == false) {
                    items.remove(i);
                    i = 0;
                    continue;
                }
            }
        }
    }

    /**
     * getter of the item
     * @return
     */
    public Item[] getItems() {
        retrieve();

        return (Item[])items.toArray(new Item[items.size()]);
    }
}
