package demo.exception;

import java.util.Comparator;
import java.util.List;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(List<Long> itemIds) {
        super("The following items are out of stock:" + itemIds);
        itemIds.sort(Comparator.naturalOrder());
    }

}
