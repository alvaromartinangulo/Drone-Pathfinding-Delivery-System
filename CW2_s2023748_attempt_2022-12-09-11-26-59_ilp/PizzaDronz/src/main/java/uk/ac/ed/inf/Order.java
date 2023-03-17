package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.util.*;

/**
 * Class to represent orders made on the system
 */
public class Order {
    private String orderNo;
    private  String orderDate;
    private  String customer;
    private  String creditCardNumber;
    private  String creditCardExpiry;
    private  String cvv;
    private  int priceTotalInPence;
    private  String[] orderItems;
    private OrderOutcome outcome;
    private Restaurant restaurant;
    private double restaurantDistance;
    @JsonCreator
    public Order(@JsonProperty("orderNo") String orderNo, @JsonProperty("orderDate") String orderDate, @JsonProperty("customer") String customer,
                 @JsonProperty("creditCardNumber") String creditCardNumber, @JsonProperty("creditCardExpiry") String creditCardExpiry,
                 @JsonProperty("cvv") String cvv, @JsonProperty("priceTotalInPence") int priceTotalInPence, @JsonProperty("orderItems") String[] orderItems ) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.customer =  customer;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;
    }
    /**
     * Get the orders from the rest server by deserializing the orders endpoint plus the date.
     * @param baseUrl to form the base of the url and add the endpoint to it
     * @param date date to form the url to fetch from the rest server
     * @return an array of orders
     */
    public static Order[] getOrdersFromServer(URL baseUrl, String date){
        return new IlpRestClient(baseUrl).deserialize("orders/" + date, Order[].class);
    }

    /**
     * Set a restaurant for the order, so that the drone can find a route for the order
     * @param restaurants to find which restaurant this order is for
     */
    public void setOrderRestaurant(Restaurant[] restaurants){
        for (Restaurant restaurant: restaurants){
            if (List.of(this.orderItems).contains(restaurant.getMenu()[0].getName())){
                this.restaurant = restaurant;
                this.restaurantDistance = this.restaurant.getLocation().distanceTo(MovementUtil.APPLETON_TOWER_LOCATION);
            }
        }
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = String.valueOf(orderNo);
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }
    public String getCreditCardExpiry() {
        return creditCardExpiry;
    }
    public String getCvv() {
        return this.cvv;
    }
    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }
    public String[] getOrderItems() {
        return orderItems;
    }

    public OrderOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(OrderOutcome outcome) {
        this.outcome = outcome;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public double getRestaurantDistance() {
        return restaurantDistance;
    }
}