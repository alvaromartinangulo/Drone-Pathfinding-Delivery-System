package uk.ac.ed.inf;

import java.net.URL;


/**
 * Singleton class to ensure only one connection is done to the REST server through runtime to fetch the central area
 * coordinates
 */
public class CentralArea extends Area {

    public CentralArea(LngLat[] points) {
        super(points);
    }

    /**
     * Gets the central area from the rest server centralArea end point
     * @param baseUrl of the rest server
     * @return Area instance of the central area
     */
    public static Area getCentralAreaFromRestServer(URL baseUrl){
        LngLat[] points = new IlpRestClient(baseUrl).deserialize("centralArea", LngLat[].class);
        return new CentralArea(points);
    }
}