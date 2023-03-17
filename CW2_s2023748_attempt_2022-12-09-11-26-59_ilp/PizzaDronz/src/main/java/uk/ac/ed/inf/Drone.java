package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Collections;

public class Drone {
    public static final int MAX_MOVES = 2000;
    public static final double MOVE_DISTANCE = 0.00015;
    private Boolean isLastOrder;
    private int remainingMoves;
    public Drone(){
        this.remainingMoves = MAX_MOVES;
        this.isLastOrder = false;
    }

    /**
     * Main algorithm of the drone. Runs the A star implementation to find all the paths for the orders.
     * Ensures that it never runs out of moves by identifying when the last order is being delivered.
     * @param orders arraylist of valid orders to deliver for that day
     * @param centralArea area representing the central area
     * @param noFlyZones arraylist of areas representing the zones where the drone cannot fly through.
     * @return an arraylist of routenodes representing all the points that the drone visits to deliver all the orders
     * before running out of moves.
     */
    public ArrayList<RouteNode> deliverOrders(ArrayList<Order> orders, Area centralArea, Area[] noFlyZones){
        ArrayList<RouteNode> allNodes = new ArrayList<>();
        for (Order order: orders){
            if(isLastOrder){
                break;
            }
            ArrayList<RouteNode> pathToRestaurant = MovementUtil.calculateRoute(noFlyZones, centralArea, MovementUtil.APPLETON_TOWER_LOCATION, order.getRestaurant().getLocation(), order.getOrderNo());
            assert pathToRestaurant != null;
            ArrayList<RouteNode> pathFromRestaurant;
            if (MovementUtil.isLastDelivery(pathToRestaurant.size(), remainingMoves)){
                isLastOrder = true;
                //Set order number to "no-order"
                pathFromRestaurant = MovementUtil.calculateRoute(noFlyZones, centralArea, order.getRestaurant().getLocation(), MovementUtil.APPLETON_TOWER_LOCATION, "no-order");
            }
            else{
                pathFromRestaurant = MovementUtil.calculateRoute(noFlyZones, centralArea, order.getRestaurant().getLocation(), MovementUtil.APPLETON_TOWER_LOCATION, order.getOrderNo());
            }
            remainingMoves -= pathToRestaurant.size() * 2;
            order.setOutcome(OrderOutcome.Delivered);
            //reverse the paths before adding them, as they are built from the last node backwards
            Collections.reverse(pathToRestaurant);
            assert pathFromRestaurant != null;
            Collections.reverse(pathFromRestaurant);
            allNodes.addAll(pathToRestaurant);
            allNodes.addAll(pathFromRestaurant);
        }
        return allNodes;
    }
}
