package uk.ac.ed.inf;

/**
 * enum class that contains 16 directions in the compass along with angles each direction represents in degrees.
 * Used for robustness and readability of code in other methods that take compass directions as parameters.
 */
public enum CompassDirection {
    NORTH(0d),
    NORTHNORTHEAST(22.5d),
    NORTHEAST(45d),
    EASTNORTHEAST(67.5d),
    EAST(90d),
    EASTSOUTHEEAST(112.5d),
    SOUTHEAST(135d),
    SOUTHSOUTHEAST(157.5d),
    SOTH(180d),
    SOUTHSOUTHWEST(202.5d),
    SOTHWEST(225d),
    WESTSOUTHWEST(247.5d),
    WEST(270d),
    NORTHWESTNORTH(292.5d),
    NORTHWEST(315d),
    NORTHNORTHWEST(337.5d);

    private final double angle; //degrees

    CompassDirection(Double angle) {
        this.angle = angle;
    }

    //Return the angle of the enum direction.
    public double angle() {
        return angle;
    }
}
