package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

/**
 * Class to represent restaurants and deserialize them from the REST service.
 */
public class Restaurant {
    private final String name;
    private final double longitude;
    private final double latitude;
    private final Menu[] menu;
    private final LngLat location;

    @JsonCreator
    public Restaurant(@JsonProperty("name") String name, @JsonProperty("longitude") double longitude, @JsonProperty("latitude")
                      double latitude, @JsonProperty("menu") Menu[] menu) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.menu = menu;
        this.location = new LngLat(this.longitude, this.latitude);
    }

    /**
     * Static method to retrieve an array of restaurants from the REST service, and deserialize them into restaurant
     * objects.
     *
     * @param baseUrl Address of the REST service with the restaurant JSON data
     * @return an array of deserialized restaurant objects.
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL baseUrl) {
        return new IlpRestClient(baseUrl).deserialize("restaurants" , Restaurant[].class);
    }
    public Menu[] getMenu() {
        return menu;
    }

    public LngLat getLocation() {
        return location;
    }

}
