package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;



/**
 * Record of immutable longitudes and latitudes. Record ensures coordinates of instances are kept constant, as
 * the program does not need them to change. The record has Json properties used to deserialize the central class
 * coordinates into an array of LngLat records.
 * @param lng longitude in degrees
 * @param lat latitude in degrees
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record LngLat(@JsonProperty("longitude") double lng, @JsonProperty("latitude") double lat){
    public static final double TOLERANCE = 0.00015;
    /**
     * Checks if this LngLat instance is in an area.To check if coordinates of instance are in polygon,
     * a horizontal line from the point to infinity has to intersect the polygon an odd number of times.
     * Method loops through all the lines in the polygon
     * and counts how many times it intersects it. Before checking for intersection, it checks whether the point is
     * actually on the line itself.
     *
     * @return Boolean. True if the number of intersects of the horizontal line drawn from the point to
     * infinity with the area polygon is odd, false otherwise.
     */
    public boolean inArea(Area area) {
        ArrayList<LngLat> points = area.getPoints();
        int numOfIntersects = 0;
        int numOfPoints = points.size();
        //Loop through all the points in the central area, and form lines from the point and the next point.
        for (int i = 0; i < numOfPoints; i++) {
            LngLat[] line = new LngLat[2];
            line[0] = points.get(i);
            //If it is the last point in the central area coordinates, form the line to the first point
            if (i == numOfPoints - 1) {
                line[1] = points.get(0);
            }
            //Form line of point and next point
            else {
                line[1] = points.get(i + 1);
            }
            if (pointOnLine(line)) {
                return true;
            }
            //Checking if latitude of the point is between the latitudes of the line
            if ((this.lat > line[0].lat) != (this.lat > line[1].lat)) {

                //If it is, then an infinite horizontal line from the point will intersect the line.
                // Check if intersect is after the longitude of the point
                if (this.lng < ((line[0].lng - line[1].lng) * (this.lat - line[1].lat) / (line[0].lat - line[1].lat) + line[1].lng)) {
                    numOfIntersects++;
                }
            }
        }
        return numOfIntersects % 2 == 1;
    }

    /**
     * Given a point and a line, checks if the point is on the line. Works out pythagorean distance between the point
     * and both points of the line. If the sum of th distances equals the length of the line, the point is on the line.
     * @param line that will be checked to see if the point is on it
     * @return Boolean true if the point is on the line, false otherwise.
     */
    private boolean pointOnLine(LngLat[] line) {
        double distance1 = distanceTo(line[0]);
        double distance2 = distanceTo(line[1]);
        //This would be done more easily if the distanceTo was not defined only to work with instances of this object,
        //and instead took another point as a parameter. Since it is not the case (from the spec), we have to work out
        //the distance covered by the line with the formula.
        double totalDistance = Math.sqrt((line[0].lng - line[1].lng) * (line[0].lng - line[1].lng)
                + (line[0].lat - line[1].lat) * (line[0].lat - line[1].lat));
        return distance1 + distance2 == totalDistance;
    }

    /**
     * Pythagorean distance from this object to another point = square root of the sum of the squared differences in
     * longitude and latitude
     *
     * @param point LngLat object to check the distance from this instance to that point
     * @return the pythagorean distance to the point
     */
    public double distanceTo(LngLat point) {
        double x = this.lng - point.lng;
        double y = this.lat - point.lat;
        return Math.sqrt(x * x + y * y);
    }

    /**
     * @param point LngLat object to check if this instance is close to that point.
     * @return True if the distance is strictly less than 0.00015 given in specification, false otherwise
     */
    public Boolean closeTo(LngLat point) {
        double distance = distanceTo(point);
        return distance <= LngLat.TOLERANCE;
    }

    /**
     * Takes a direction in degrees. Transforms it into radians, and does simple trigonometry to find the new position
     * after a displacement of 0.00015 degrees in a direction
     * @param direction in degrees to move in
     * @return New position of the object after moving 0.00015 degrees in the direction of the parameter
     */
    public LngLat nextPosition(CompassDirection direction) {
        double angleRadians = Math.toRadians(direction.angle());
        double latDisplacement = Drone.MOVE_DISTANCE * Math.cos(angleRadians);
        double lngDisplacement = Drone.MOVE_DISTANCE * Math.sin(angleRadians);
        return new LngLat(this.lng + lngDisplacement, this.lat + latDisplacement);
    }

}
