package uk.ac.ed.inf;

/**
 * Menu class used for writing to the flightpaths file
 */
public class Move {
    //"no-order" if the move is being done to return to appleton tower for the last time
    private String orderNo;
    private double fromLongitude;
    private double fromLatitude;
    private final double angle;
    private double toLongitude;
    private double toLatitude;
    private long ticksSinceStartOfCalculation;

    public Move(String orderNo, double fromLongitude, double fromLatitude,double angle, double toLongitude, double toLatitude, int ticksSinceStartOfCalculation) {
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
        this.ticksSinceStartOfCalculation = ticksSinceStartOfCalculation;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getFromLongitude() {
        return fromLongitude;
    }

    public double getFromLatitude() {
        return fromLatitude;
    }

    public void setFromLatitude(double fromLatitude) {
        this.fromLatitude = fromLatitude;
    }

    public double getAngle() {
        return angle;
    }

    public double getToLongitude() {
        return toLongitude;
    }

    public void setToLongitude(double toLongitude) {
        this.toLongitude = toLongitude;
    }

    public double getToLatitude() {
        return toLatitude;
    }

    public void setToLatitude(double toLatitude) {
        this.toLatitude = toLatitude;
    }

    public long getTicksSinceStartOfCalculation() {
        return ticksSinceStartOfCalculation;
    }

    public void setTicksSinceStartOfCalculation(int ticksSinceStartOfCalculation) {
        this.ticksSinceStartOfCalculation = ticksSinceStartOfCalculation;
    }
}
