package demo.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import demo.entity.dto.BasketDetails;
import demo.entity.dto.BasketItem;
import demo.entity.persistent.Basket;
import demo.entity.persistent.Customer;
import demo.entity.persistent.Item;

public class BasketConverter {

    private ItemConverter itemConverter;

    public BasketConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
    }

    public BasketDetails toBasketDetails(Basket basket) {
        Customer customer = basket.getCustomer();
        List<BasketItem> basketItems = itemConverter.toBasketItems(basket.getItems());
        BasketDetails details = new BasketDetails(customer.getId(), customer.getFullName(), basketItems);
        return details;
    }

}
