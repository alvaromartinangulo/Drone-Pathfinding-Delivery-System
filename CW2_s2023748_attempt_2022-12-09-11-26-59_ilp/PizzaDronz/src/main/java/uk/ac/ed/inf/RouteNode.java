package uk.ac.ed.inf;

public class RouteNode implements Comparable<RouteNode>{
    private final LngLat current;
    private LngLat previous;
    private double routeScore;
    private double estimatedScore;
    private Boolean leftCentralArea;
    private int ticksSinceStartOfCalculation;
    private CompassDirection direction;
    private final String orderNo;

    public RouteNode(LngLat current, LngLat previous, double routeScore, double estimatedScore, Boolean leftCentralArea, String orderNo) {
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
        this.leftCentralArea = leftCentralArea;
        this.orderNo = orderNo;
    }

    public RouteNode(LngLat current, CompassDirection direction, String orderNo) {
        this.current = current;
        this.orderNo = orderNo;
        this.previous = null;
        this.routeScore = Double.POSITIVE_INFINITY;
        this.estimatedScore = Double.POSITIVE_INFINITY;
        this.leftCentralArea = false;
        this.direction = direction;
    }

    /**
     * Overriding compareTo based on the estimated route score of the nodes
     * @param other the object to be compared.
     * @return 1,0,-1 depending on the outcome -> -1 if the estimated score of this is smaller, 0 if equal, 1 otherwise
     */
    @Override
    public int compareTo(RouteNode other) {
        return Double.compare(this.estimatedScore, other.estimatedScore);
    }

    /**
     * Checks that the node is not in a no fly zone, and that it has not left and gone back in the
     * central area
     * @param point to check coordinates
     * @param noFlyZones to check that point is not in
     * @param centralArea to check if point has left and entered
     * @return true if it is valid, false otherwise
     */
    public Boolean isValidNode(LngLat point, Area[] noFlyZones, Area centralArea){
        if (!point.inArea(centralArea)){
            if (!this.leftCentralArea){
                this.leftCentralArea = true;
            }
        }
        else{
            if (this.leftCentralArea){
                return false;
            }
        }
        for (Area noFlyZone : noFlyZones){
            if (point.inArea(noFlyZone)){
                return false;
            }
        }
        return true;
    }

    public LngLat getCurrent() {
        return current;
    }

    public LngLat getPrevious() {
        return previous;
    }

    public double getRouteScore() {
        return routeScore;
    }

    public void setEstimatedScore(double estimatedScore) {
        this.estimatedScore = estimatedScore;
    }

    public void setRouteScore(double routeScore) {
        this.routeScore = routeScore;
    }

    public void setPrevious(LngLat previous) {
        this.previous = previous;
    }

    public int getTicksSinceStartOfCalculation() {
        return ticksSinceStartOfCalculation;
    }

    public void setTickSinceStartOfCalculation(int ticks) {
        this.ticksSinceStartOfCalculation = ticks;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public CompassDirection getDirection() {
        return direction;
    }
}
