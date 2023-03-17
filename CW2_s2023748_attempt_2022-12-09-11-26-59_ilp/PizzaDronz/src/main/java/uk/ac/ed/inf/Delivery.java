package uk.ac.ed.inf;

public class Delivery {
    private String orderNo;
    private String outcome;
    private int costInPence;

    public Delivery(String orderNo, String outcome, int costInPence) {
        this.orderNo = orderNo;
        this.outcome = outcome;
        this.costInPence = costInPence;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public int getCostInPence() {
        return costInPence;
    }
}
