package uk.ac.ed.inf;

import java.util.*;

/**
 * Class used to deal with all movements of the drone
 */
public final class MovementUtil{

    private MovementUtil(){}
    public static final LngLat APPLETON_TOWER_LOCATION = new LngLat(-3.186874, 55.944494);

    /**
     * Implementation of the A star pathfinding algoritm. Constructs open set of possible routes and builds
     * route of the best possible way to getting to each point. When it arrives at end location, builds
     * the final route using backtracking through previous routeNodes.
     * @param noFlyZones zones that are checked before creating a node. Nodes are not created if they are in the zone
     * @param centralArea zone that is checked before creating a node. Check that the drone does not enter and leave the central area
     *                    in the same iteration
     * @param startLocation location from which to start the path
     * @param endLocation location to finish the path. closeTo method from LngLat sufficient condition to end
     * @param orderNo to store in each node to create Move objects later
     * @return arraylist of the RouteNodes that compose of the route from the start point to end point
     */
    public static ArrayList<RouteNode> calculateRoute(Area[] noFlyZones, Area centralArea, LngLat startLocation, LngLat endLocation, String orderNo){
        Queue<RouteNode> openSet = new PriorityQueue<>();
        Map<LngLat, RouteNode> allNodes = new HashMap<>();
        long startTimeElapsed = System.nanoTime();
        RouteNode startPoint = new RouteNode(startLocation,null, 0d, APPLETON_TOWER_LOCATION.distanceTo(endLocation), true, orderNo);
        openSet.add(startPoint);
        allNodes.put(startPoint.getCurrent(), startPoint);
        while(!(openSet.isEmpty())){
            //Get node with the lowest estimated score
            RouteNode next = openSet.poll();
            //End condition, arrived at end location
            if (next.getCurrent().closeTo(endLocation)){
                ArrayList<RouteNode> route = new ArrayList<>();
                RouteNode current = next;
                //Backtracking to build route
                while (current != null){
                    route.add(current);
                    current = allNodes.get(current.getPrevious());
                }
                return route;
            }
            for (CompassDirection direction: CompassDirection.values()){
                //Get all points moving into the 16 directions
                LngLat newPos = next.getCurrent().nextPosition(direction);
                //get the node from all nodes if it exists, create one otherwise
                RouteNode nextNode = allNodes.getOrDefault(newPos, new RouteNode(newPos, direction, orderNo));
                if (nextNode.isValidNode(nextNode.getCurrent(),noFlyZones,centralArea)){
                    allNodes.put(newPos, nextNode);
                    double newRouteScore = next.getRouteScore() + Drone.MOVE_DISTANCE;
                    //If this node is better than the one that existed, add it to the open set
                    if (nextNode.getRouteScore() > newRouteScore){
                        nextNode.setPrevious(next.getCurrent());
                        nextNode.setRouteScore(newRouteScore);
                        double distanceToEnd = nextNode.getCurrent().distanceTo(endLocation);
                        nextNode.setEstimatedScore(newRouteScore + distanceToEnd);
                        long currentTimeElapsed = System.nanoTime() - startTimeElapsed;
                        nextNode.setTickSinceStartOfCalculation(nanoSecondsToTicks(currentTimeElapsed));
                        openSet.add(nextNode);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Creates an arraylist of Move objects from an arraylist of Route Nodes
     * @param routeNodes to create the moves from
     * @return the arraylist of moves
     */
    public static ArrayList<Move> createMovesFromRouteNodes(ArrayList<RouteNode> routeNodes){
        int len = routeNodes.size();
        ArrayList<Move> moves = new ArrayList<>();
        if (len <= 2){
            System.err.println("The Drone has not moved");
        }
        for(int i = 0; i < len - 1; i ++){
            RouteNode from = routeNodes.get(i);
            RouteNode to = routeNodes.get(i + 1);
            if (to.getDirection() == null){
                continue;
            }
            Move move = new Move(to.getOrderNo(), from.getCurrent().lng(), from.getCurrent().lat(), to.getDirection().angle(), to.getCurrent().lng(), to.getCurrent().lat(), to.getTicksSinceStartOfCalculation());
            moves.add(move);
        }
        return moves;
    }

    /**
     * Checks if this delivery is the last delivery the drone can take
     * @param movesNeeded to complete the current delivery
     * @param movesRemaining of the drone at this point
     * @return true if it will be the last delivery, false otherwise
     */
    public static Boolean isLastDelivery(int movesNeeded, int movesRemaining){
        //Moves remaining after this delivery less than another of these deliveries (Will always be more or the same)
        return movesNeeded * 2 > (movesRemaining - (movesNeeded * 2));
    }


    /**
     * Turns the nanoseconds into an integer and divides them by 100
     * @param nanoSecs to convert
     * @return the int ticks
     */
    private static int nanoSecondsToTicks(long nanoSecs){
        long longTicks = nanoSecs / 100;
        return (int) longTicks;
    }
}
