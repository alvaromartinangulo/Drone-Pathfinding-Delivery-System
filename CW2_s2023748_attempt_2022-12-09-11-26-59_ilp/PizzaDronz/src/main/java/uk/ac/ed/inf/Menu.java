package uk.ac.ed.inf;

/**
 * Menu class used for deserialization of Restaurants at the REST server, and storing menus as objects.
 */
public class Menu {
    private String name;
    private int priceInPence;

    public Menu() {
        super();
    }

    public String getName() {
        return name;
    }

    public int getPriceInPence() {
        return priceInPence;
    }
}
