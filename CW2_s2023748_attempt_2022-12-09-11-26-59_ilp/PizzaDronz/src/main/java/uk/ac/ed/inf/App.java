package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import static uk.ac.ed.inf.InputValidation.ISO8061;
import static uk.ac.ed.inf.InputValidation.baseUrlBackup;

public class App {
    public static void main(String[] args) {
        //Validate input
        if (!(InputValidation.isValidInput(args))){
            System.err.println("Please provide a valid input and run the program");
            System.exit(1);
        }
        //Set attributes from input
        Date date = null;
        URL baseUrl;
        String seed;
        try{
            date = ISO8061.stringToDate(args[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            baseUrl = new URL(InputValidation.appendSlashIfNeeded(args[1]));
        } catch (MalformedURLException e) {
            System.err.println("Provided URL not valid, using backup as base URL");
            baseUrl = baseUrlBackup;
        }
        seed = args[2];
        //Fetch instances from rest server
        Order[] orders = Order.getOrdersFromServer(baseUrl, ISO8061.dateToString(date));
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(baseUrl);
        Area[] noFlyZones = NoFLyZones.getNoFlyFromRestServer(baseUrl);
        Area centralArea = CentralArea.getCentralAreaFromRestServer(baseUrl);
        //Only look at valid orders to find paths
        ArrayList<Order> validOrders = OrderUtil.getSortedValidOrdersFromOrders(orders, ISO8061, restaurants, date);
        Drone drone = new Drone();
        ArrayList<RouteNode> allNodes = drone.deliverOrders(validOrders, centralArea, noFlyZones);
        //Create files from the routes
        FileManager.createFlightPathFile(allNodes, date);
        FileManager.createDeliveriesFile(orders, date);
        FileManager.createDroneFile(allNodes, date);
    }
}