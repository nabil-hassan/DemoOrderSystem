package demo.exception;

public class ItemNotInBasketException extends RuntimeException {

    public ItemNotInBasketException(Long itemId) {
        super("Item with id " + itemId + " is not in the customer's basket");
    }

}
