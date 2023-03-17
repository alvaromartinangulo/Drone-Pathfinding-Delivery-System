package uk.ac.ed.inf;

/**
 * Encapsulates all the possible order outcomes, which are validated in OrderUtil and set to
 * one of the enum values.
 */
public enum OrderOutcome {
    Delivered,
    ValidButNotDelivered,
    InvalidOrderDate,
    InvalidCardNumber,
    InvalidExpiryDate,
    InvalidCvv,
    InvalidTotal,
    InvalidPizzaNotDefined,
    InvalidPizzaCount,
    InvalidCombinationMultipleSuppliers
}