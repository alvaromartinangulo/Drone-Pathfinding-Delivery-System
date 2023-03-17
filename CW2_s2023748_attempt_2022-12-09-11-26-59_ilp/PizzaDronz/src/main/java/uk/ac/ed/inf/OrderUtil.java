package uk.ac.ed.inf;

import java.text.ParseException;
import java.util.*;

/**
 * Class to deal with managing and validating orders
 */
public final class OrderUtil {
    public static final int DELIVERY_COST = 100;

    private OrderUtil(){}

    /**
     * Validates orders, setting an outcome before the drone delivers the orders. SGets the valid orders
     * sets a restaurant and returns them sorted on the distance to Appleton tower.
     * @param orders array to check and build valid orders
     * @param dateValidator to validate dates of the orders
     * @param restaurants to check if pizzas of orders are valid
     * @param date to check if the date of the orders is a valid date
     * @return an Arraylist of valid orders sorted on their distance to appleton tower
     */
    public static ArrayList<Order> getSortedValidOrdersFromOrders(Order[] orders, DateValidator dateValidator, Restaurant[] restaurants, Date date){
        ArrayList<Order> validOrders = new ArrayList<>();
        for (Order order : orders){
            checkOrder(order, dateValidator, restaurants, date);
            if (order.getOutcome().equals(OrderOutcome.ValidButNotDelivered)){
                validOrders.add(order);
            }
        }
        for (Order validOrder: validOrders){
            validOrder.setOrderRestaurant(restaurants);
        }
        validOrders.sort(Comparator.comparing(Order::getRestaurantDistance));
        return validOrders;
    }

    /**
     * Checks an order, and assigns a value from OrderOutcome.
     * @param order to check
     * @param dateValidator to check for date formats
     * @param restaurants to check order is from a single restaurant
     * @param dateArgument to check order date is valid
     */
    public static void checkOrder(Order order, DateValidator dateValidator, Restaurant[] restaurants, Date dateArgument){
        if (!isValidCardNo(order.getCreditCardNumber())){
            order.setOutcome(OrderOutcome.InvalidCardNumber);
        }
        else if (!isValidCvv(order.getCvv())){
            order.setOutcome(OrderOutcome.InvalidCvv);
        }
        else if (!isValidOrderDate(order.getOrderDate(), dateValidator, dateArgument)){
            order.setOutcome(OrderOutcome.InvalidOrderDate);
        }
        else if (!isValidCardExpiry(order.getCreditCardExpiry(), dateArgument)){
            order.setOutcome(OrderOutcome.InvalidExpiryDate);
        }
        else if (!isValidPizzaNumber(order.getOrderItems())){
            order.setOutcome(OrderOutcome.InvalidPizzaCount);
        }
        else if (!isPizzasDefined(restaurants, order.getOrderItems())){
            order.setOutcome(OrderOutcome.InvalidPizzaNotDefined);
        }
        else if (!isValidPizzaCombination(restaurants, order.getOrderItems())){
            order.setOutcome(OrderOutcome.InvalidCombinationMultipleSuppliers);
        }
        else if (!isValidTotal(restaurants, order.getOrderItems(), order.getPriceTotalInPence())){
            order.setOutcome(OrderOutcome.InvalidTotal);
        }
        else{
            order.setOutcome(OrderOutcome.ValidButNotDelivered);
        }
    }

    /**
     * Finds out the delivery cost given an array of pizzas and restaurant. Only ran after
     * order is ensured to have a single restaurant
     * @param restaurants to find order restaurant
     * @param pizzas to check for price in restaurant menu
     * @return price in pence of the delivery cost
     */
    public static int getDeliveryCost(Restaurant[] restaurants, String[] pizzas) {
        int costInPence = DELIVERY_COST;
        boolean foundRestaurant = false;
        for (Restaurant restaurant: restaurants){
            if (List.of(pizzas).contains(restaurant.getMenu()[0].getName())){
                foundRestaurant = true;
            }
            HashMap<String, Integer> pizzaPrice = new HashMap<>();
            if (foundRestaurant){
                for (Menu item:restaurant.getMenu()){
                    pizzaPrice.put(item.getName(),item.getPriceInPence());
                }
            }
            for (String pizza:pizzas){
                if (pizzaPrice.containsKey(pizza)){
                    costInPence += pizzaPrice.get(pizza);
                }
            }
        }
        return costInPence;
    }

    /**
     * Checks if all the pizzas are of a single restaurant
     * @param restaurants to check for pizzas
     * @param pizzas to check in restaurants
     * @return true if the combination is valid, false otherwise
     */
    private static Boolean isValidPizzaCombination(Restaurant[] restaurants, String[] pizzas){
        HashMap<Restaurant,ArrayList<String>> restaurantsPizzas = new HashMap<>();
        for (Restaurant restaurant: restaurants){
            ArrayList<String> restaurantPizzas = new ArrayList<>();
            for (Menu menu: restaurant.getMenu()){
                restaurantPizzas.add(menu.getName());
            }
            restaurantsPizzas.put(restaurant, restaurantPizzas);
        }
        for (ArrayList<String> restaurantPizzas: restaurantsPizzas.values()){
            if (restaurantPizzas.containsAll(List.of(pizzas))){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all the pizzas are defined in an array of restaurants
     * @param restaurants to check for the pizzas
     * @param pizzas to check in restaurants
     * @return true if all pizzas are found in the restaurant menus
     */
    private static Boolean isPizzasDefined(Restaurant[] restaurants, String[] pizzas){
        ArrayList<String> allPizzas = new ArrayList<>();
        for (Restaurant restaurant: restaurants){
            for (Menu menu : restaurant.getMenu()){
                allPizzas.add(menu.getName());
            }
        }
        return allPizzas.containsAll(List.of(pizzas));
    }

    /**
     * Checks if the total specified in the order is the same as that calculated at runtime by
     * checking the restaurant list
     * @param restaurants to check for price
     * @param pizzas to check for price
     * @param orderPrice price in the order
     * @return true if the total amount is the same, false otherwise
     */
    private static Boolean isValidTotal(Restaurant[] restaurants, String[] pizzas, int orderPrice){
        if (orderPrice < DELIVERY_COST){
            return false;
        }
        return orderPrice == (getDeliveryCost(restaurants, pizzas));
    }

    /**
     * Checks that there are between 1 and 4 pizzas in an order
     * @param pizzas to check
     * @return true if there are between 1 and 4 pizzas, false otherwise
     */
    private static Boolean isValidPizzaNumber(String[] pizzas){
        int len = pizzas.length;
        return (len <= 4) && (len >= 1);
    }

    /**
     * Checks that the date provided at runtime is the same as the date of the order
     * @param dateStr Order date
     * @param dateValidator to check format
     * @param dateArgument date passed as argument of program execution
     * @return true if the dates are equal, false otherwise
     */
    private static Boolean isValidOrderDate(String dateStr, DateValidator dateValidator, Date dateArgument){
        try{
            Date date = dateValidator.stringToDate(dateStr);
            if (dateArgument.equals(date)){
                return true;
            }
        }
        catch (ParseException e){
            return false;
        }
        return false;
    }

    /**
     * Checks if the credit card expiry is valid
     * @param dateStr to check for validity
     * @param dateParameter to check that it is after this date
     * @return true if the date is valid, false otherwise
     */
    private static Boolean isValidCardExpiry(String dateStr, Date dateParameter){
        DateValidator cardExpiryDates = new DateValidator("MM/yy");
        try {
            Date dateCreditCardExpiry = cardExpiryDates.stringToDate(dateStr);
            if (dateCreditCardExpiry.after(dateParameter)){
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;
    }

    /**
     * Checks that the cvv has 3 numbers
     * @param cvv to check
     * @return true if cvv has 3 numbers, false otherwise
     */
    private static Boolean isValidCvv(String cvv){
        int len = cvv.length();
        if(len != 3){
            return false;
        }
        for (int i = 0; i < len; i++){
            if ((cvv.charAt(i) < '0') || (cvv.charAt(i) > '9')){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks card number has between 13 and 16 numbers
     * @param cardNumber to check
     * @return true if it has between 13 and 16 numbers, false otherwise
     */
    private static Boolean isValidCardNo(String cardNumber){
        int len = cardNumber.length();
        if ((len < 13) || (len > 19)){
            return false;
        }
        for (int i = 0; i < len; i++){
            if ((cardNumber.charAt(i) < '0') || (cardNumber.charAt(i) > '9')){
                return false;
            }
        }
        return true;
    }

    /**
     * Creates an arraylist of Delievry objects form an arraylist of Order object
     * @param orders to create deliveries from
     * @return arraylist of deliveries created from orders
     */
    public static ArrayList<Delivery> createDeliveriesFromOrders(ArrayList<Order> orders){
        ArrayList<Delivery> deliveries = new ArrayList<>();
        for(Order order: orders){
            Delivery delivery = new Delivery(order.getOrderNo(), order.getOutcome().toString(), order.getPriceTotalInPence());
            deliveries.add(delivery);
        }
        return deliveries;
    }
}