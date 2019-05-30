package demo.service;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.entity.Basket;
import demo.entity.Customer;
import demo.entity.Item;
import demo.exception.BasketItemException;
import demo.exception.EntityNotFoundException;

public class BasketService {

    private ItemDAO itemDAO;

    private CustomerDAO customerDAO;

    public BasketService(ItemDAO itemDAO, CustomerDAO customerDAO) {
        this.itemDAO = itemDAO;
        this.customerDAO = customerDAO;
    }

    @Transactional
    public Basket addItemToBasket(Long customerId, Long itemId) {
        Customer customer = customerDAO.get(customerId);
        if (customer == null) {
            throw new EntityNotFoundException(Customer.class, customerId);
        }

        Item item = itemDAO.get(itemId);
        if (item == null) {
            throw new EntityNotFoundException(Item.class, itemId);
        }

        Basket basket = customer.getBasket();
        if (basket == null) {
            basket = new Basket(customer);
            customer.setBasket(basket);
        }

        basket.addItem(item);
        return basket;
    }

    @Transactional
    public Basket removeItemFromBasket(Long customerId, Long itemId) {
        Customer customer = customerDAO.get(customerId);
        if (customer == null) {
            throw new EntityNotFoundException(Customer.class, customerId);
        }

        Item item = itemDAO.get(itemId);
        if (item == null) {
            throw new EntityNotFoundException(Item.class, itemId);
        }

        Basket basket = customer.getBasket();
        if (basket == null) {
            throw new IllegalStateException("Customer has no basket");
        }

        Set<Long> itemIds = basket.getItems().stream().map(Item::getId).collect(Collectors.toSet());
        if(!itemIds.contains(itemId)) {
            throw new BasketItemException(itemId);
        }

        basket.getItems().removeIf(i -> i.getId().equals(itemId));
        return basket;
    }

}
