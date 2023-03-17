package uk.ac.ed.inf;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static uk.ac.ed.inf.InputValidation.ISO8061;

/**
 * Class to deal with file creation as outputs of the programs
 */
public final class FileManager {
    private FileManager(){
    }

    /**
     * Creates the file flightpath-xxxx-xx-xx.json for a given date. Creates the moves from the routes and serialises
     * the moves from the Move class into JSON format
     * @param allNodes arraylist of all the nodes calculated by the drone
     * @param date date for the file creation
     */
    public static void createFlightPathFile(ArrayList<RouteNode> allNodes, Date date){
        ArrayList<Move> moves = MovementUtil.createMovesFromRouteNodes(allNodes);
        JSONObject flightpath = new JSONObject();
        JSONArray writeMoves = new JSONArray();
        for(Move move: moves){
            JSONObject currMove = new JSONObject();
            currMove.put("orderNo", move.getOrderNo());
            currMove.put("fromLongitude", move.getFromLongitude());
            currMove.put("fromLatitude", move.getFromLatitude());
            currMove.put("angle", move.getAngle());
            currMove.put("toLongitude", move.getToLongitude());
            currMove.put("toLatitude", move.getToLatitude());
            currMove.put("ticksSinceStartOfCalculation", move.getTicksSinceStartOfCalculation());
            writeMoves.add(currMove);
        }
        createFileFromJSON(writeMoves, "flightpath", date, false);
    }

    /**
     * Creates the file deliveries-xxxx-xx-xx.json for a given date. Creates deliveries from the order for the date.
     * These Delivery objects are serialised into JSON format for writing to the files.
     * @param orders array of orders to create deliveries from
     * @param date date for the file creation
     */
    public static void createDeliveriesFile(Order[] orders, Date date){
        ArrayList<Delivery> deliveries = OrderUtil.createDeliveriesFromOrders(new ArrayList<Order>(List.of(orders)));
        JSONObject deliveriesObj = new JSONObject();
        JSONArray deliveriesArr = new JSONArray();
        for (Delivery delivery: deliveries){
            JSONObject currDelivery = new JSONObject();
            currDelivery.put("orderNo", delivery.getOrderNo());
            currDelivery.put("outcome", delivery.getOutcome());
            currDelivery.put("costInPence", delivery.getCostInPence());
            deliveriesArr.add(currDelivery);
        }
        deliveriesObj.put("deliveries", deliveriesArr);
        System.out.println(deliveriesObj);
        createFileFromJSON(deliveriesArr, "deliveries", date, false);
    }

    /**
     * Creates the file drone-xxxx-xx-xx.geojson for a given date. Gets all the coordinates from all the nodes, to create
     * a line. Formats it as a feature collection so it can be seen in geojson format.
     * @param allNodes arraylist of all the nodes calculated by the drone
     * @param date date for the file creation
     */
    public static void createDroneFile(ArrayList<RouteNode> allNodes, Date date){
        JSONObject featureCollection = new JSONObject();
        featureCollection.put("type", "FeatureCollection");
        JSONArray features = new JSONArray();
        JSONObject feature = new JSONObject();
        feature.put("type", "Feature");
        JSONObject properties = new JSONObject();
        JSONObject geometry = new JSONObject();
        geometry.put("type", "LineString");
        JSONArray coordinates = new JSONArray();
        for (RouteNode node: allNodes){
            JSONArray coordinate = new JSONArray();
            coordinate.add(node.getCurrent().lng());
            coordinate.add(node.getCurrent().lat());
            coordinates.add(coordinate);
        }
        geometry.put("coordinates", coordinates);
        feature.put("geometry", geometry);
        feature.put("properties", properties);
        features.add(feature);
        featureCollection.put("features", features);
        try {
            FileWriter file = new FileWriter("drone" + "-" + ISO8061.dateToString(date) + ".geojson");
            file.write(featureCollection.toJSONString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Used to create files given json or geojson objects, and if it is a geojson or json file.
     * @param object to write to the file
     * @param fileName string to represent the filename
     * @param date to add after the file name
     * @param asGeoJSON boolean to create the file as a geojson or json
     */
    private static void createFileFromJSON(JSONArray object, String fileName, Date date, Boolean asGeoJSON){
        try{
            FileWriter file;
            if(asGeoJSON){
                file = new FileWriter(fileName + "-" + ISO8061.dateToString(date) + ".geojson");
            }
            else{
                file = new FileWriter(fileName + "-" + ISO8061.dateToString(date) + ".json");
            }
            file.write(object.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
