package demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import demo.converter.BasketConverter;
import demo.converter.ItemConverter;
import demo.converter.OrderConverter;
import demo.dao.AddressDAO;
import demo.dao.CreditCardDAO;
import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.dao.OrderDAO;
import demo.entity.dto.BasketDetails;
import demo.entity.dto.OrderDetails;
import demo.entity.persistent.Address;
import demo.entity.persistent.Basket;
import demo.entity.persistent.CreditCard;
import demo.entity.persistent.Customer;
import demo.entity.persistent.Item;
import demo.entity.persistent.Order;
import demo.entity.persistent.OrderStatus;
import demo.exception.BasketEmptyException;
import demo.exception.ItemNotInBasketException;
import demo.exception.EntityNotAssociatedException;
import demo.exception.EntityNotFoundException;
import demo.exception.InsufficientStockException;

public class BasketService {

    private AddressDAO addressDAO;
    private BasketConverter basketConverter;
    private CreditCardDAO creditCardDAO;
    private CustomerDAO customerDAO;
    private ItemConverter itemConverter;
    private ItemDAO itemDAO;
    private OrderDAO orderDAO;
    private OrderConverter orderConverter;

    public BasketService(AddressDAO addressDAO, BasketConverter basketConverter, CreditCardDAO creditCardDAO,
            CustomerDAO customerDAO, ItemDAO itemDAO, ItemConverter itemConverter, OrderConverter orderConverter,
            OrderDAO orderDAO) {
        this.addressDAO = addressDAO;
        this.basketConverter = basketConverter;
        this.creditCardDAO = creditCardDAO;
        this.customerDAO = customerDAO;
        this.itemConverter = itemConverter;
        this.itemDAO = itemDAO;
        this.orderConverter = orderConverter;
        this.orderDAO = orderDAO;
    }

    @Transactional
    public BasketDetails addItemToBasket(Long customerId, Long itemId) {
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
        return basketConverter.toBasketDetails(basket);
    }

    @Transactional
    public BasketDetails getBasketDetails(Long customerId) {
        Customer customer = customerDAO.get(customerId);
        if (customer == null) {
            throw new EntityNotFoundException(Customer.class, customerId);
        }
        if (customer.getBasket() == null) {
            customer.setBasket(new Basket(customer));
        }
        return basketConverter.toBasketDetails(customer.getBasket());
    }

    @Transactional
    public BasketDetails removeItemFromBasket(Long customerId, Long itemId) {
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

        Iterator<Item> itr = basket.getItems().iterator();
        boolean found = false;
        while (itr.hasNext()) {
            Item i = itr.next();
            if (i.getId().equals(itemId)) {
                itr.remove();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ItemNotInBasketException(itemId);
        }
        return basketConverter.toBasketDetails(basket);
    }

    /**
     * Checks out the contents of a customer's basket to create a new order.
     *
     * If the customer or does not exist, an {@link EntityNotFoundException} is thrown.
     *
     * If the customer has no associated basket, an {@link BasketEmptyException} is thrown.
     *
     * If the customer's basket is empty,
     *
     * If any items are no longer in stock, an {@link InsufficientStockException} is thrown.
     *
     * @param customerId the id of the customer
     * @param addressId the id of the address
     * @param cardId the id of the payment method
     * @return the created order
     */
    @Transactional
    public OrderDetails checkout(Long customerId, Long addressId, Long cardId) {
        Customer customer = customerDAO.get(customerId);
        if (customer == null) {
            throw new EntityNotFoundException(Customer.class, customerId);
        }

        Address address = addressDAO.get(addressId);
        if (address == null) {
            throw new EntityNotFoundException(Address.class, addressId);
        }
        if (!customer.getAddresses().contains(address)) {
            throw new EntityNotAssociatedException(Customer.class, customerId, Address.class, addressId);
        }

        CreditCard card = creditCardDAO.get(cardId);
        if (card == null) {
            throw new EntityNotFoundException(CreditCard.class, cardId);
        }
        if (!customer.getCards().contains(card)) {
            throw new EntityNotAssociatedException(Customer.class, customerId, CreditCard.class, cardId);
        }

        Basket basket = customer.getBasket();
        if (basket == null) {
            throw new IllegalStateException("Customer has no basket");
        }
        if (basket.isEmpty()) {
            throw new BasketEmptyException(customerId);
        }

        // Check stock quantities, update stock counts, calculate total cost
        Map<Item, Integer> basketItemCountMap = itemConverter.toItemCountMap(basket.getItems());
        Map<Item, Integer> newItemStockCount = new HashMap<>();
        List<Long> insufficientStockIds = new ArrayList<>();
        Double totalCost = 0d;
        for (Map.Entry<Item, Integer> entry : basketItemCountMap.entrySet()) {
            Item item = entry.getKey();
            Integer itemCount = entry.getValue();
            int newCount = item.getStockQuantity() - itemCount;
            if (newCount < 0) {
                insufficientStockIds.add(item.getId());
            }
            newItemStockCount.put(item, newCount);
            totalCost += (item.getCost() * itemCount);
        }
        if (insufficientStockIds.size() > 0) {
            throw new InsufficientStockException(insufficientStockIds);
        }
        newItemStockCount.forEach(Item::setStockQuantity);

        Order order = new Order(customer, new Date(), OrderStatus.NEW, address, totalCost, card);
        order.addItems(basket.getItems());
        customer.addOrder(order);
        basket.clearItems();

        // yes, would be created by dirty-checking the customer, but this ensures the order details returned to the
        // customer have the id for the order!
        orderDAO.create(order);

        return orderConverter.toOrderDetails(order);
    }

}
