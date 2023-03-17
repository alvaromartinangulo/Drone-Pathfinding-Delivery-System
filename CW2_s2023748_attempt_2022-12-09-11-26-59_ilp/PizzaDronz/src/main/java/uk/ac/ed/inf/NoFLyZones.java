package uk.ac.ed.inf;

import java.net.URL;

/**
 * Class of the zones that the drone cannot fly through. Extends area to use the methods from LngLat
 */
public class NoFLyZones extends Area{
    public NoFLyZones(double[][] coordinates) {
        super(coordinates);
    }

    /**
     * Get the no fly zones from the rest server by deserialising the noFlyZones endpoint.
     * @param baseUrl to form the base of the url and add the endpoint to it
     * @return an array of areas consisiting of the no fly zones
     */
    public static Area[] getNoFlyFromRestServer(URL baseUrl){
        Area[] areas = new IlpRestClient(baseUrl).deserialize("noFlyZones" , Area[].class);
        return areas;
    }
}
