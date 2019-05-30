package demo.exception;

public class BasketItemException extends RuntimeException {

    public BasketItemException(Long itemId) {
        super("Item with id " + itemId + " is not in the customer's basket");
    }

}
