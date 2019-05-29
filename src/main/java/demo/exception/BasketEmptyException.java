package demo.exception;

public class BasketEmptyException extends RuntimeException {

    public BasketEmptyException(Long customerId) {
        super("No items in basket for customer " + customerId);
    }

}
