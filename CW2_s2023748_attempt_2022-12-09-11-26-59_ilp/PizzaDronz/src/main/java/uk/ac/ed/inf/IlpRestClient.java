package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class that handles connections to the Rest server, and deserialising the JSON information fetched
 * into different objects
 */
public class IlpRestClient {

    private final URL baseUrl;
    public IlpRestClient(URL baseUrl){
        this.baseUrl = baseUrl;
    }

    /**
     * Deserialises the JSON records at the URL formed from the base url of the ILP rest client and
     * an end point. Deserializes the records into the class passed as the parameter
     * @param fromEndPoint endpoint to add to the base url and deserialize the endpoint
     * @param klass Class to deserialize the records into
     * @return Instances of the class after deserialization
     * @param <T> Type of class passed as parameter
     */
    public <T> T deserialize(String fromEndPoint, Class<T> klass){
        URL finalUrl = null;
        T response = null;
        try{
            finalUrl = new URL(baseUrl.toString() + fromEndPoint);
        } catch(MalformedURLException e){
            e.printStackTrace();
        }

        try{
            response = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(finalUrl, klass);
        } catch(Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
