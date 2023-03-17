package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class Area {
    private final ArrayList<LngLat> points;

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)
    public Area(@JsonProperty("coordinates")double[][] coordinates){
        this.points = new ArrayList<>();
        for (double[] coordinate: coordinates){
            this.points.add(new LngLat(coordinate[0], coordinate[1]));
        }
    }

    public Area(LngLat[] points){
        this.points = new ArrayList<>(Arrays.asList(points));
    }


    public ArrayList<LngLat> getPoints(){
        return this.points;
    }
}
