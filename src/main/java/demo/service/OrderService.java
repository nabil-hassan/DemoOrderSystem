package demo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import demo.dao.AddressDAO;
import demo.dao.CreditCardDAO;
import demo.dao.CustomerDAO;
import demo.dao.OrderDAO;
import demo.entity.Address;
import demo.entity.Basket;
import demo.entity.CreditCard;
import demo.entity.Customer;
import demo.entity.HibernateEntity;
import demo.entity.Item;
import demo.entity.Order;
import demo.entity.OrderStatus;
import demo.exception.BasketEmptyException;
import demo.exception.EntityNotFoundException;
import demo.exception.InsufficientStockException;

public class OrderService {

    private AddressDAO addressDAO;
    private CreditCardDAO creditCardDAO;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    public OrderService(AddressDAO addressDAO, CreditCardDAO creditCardDAO, CustomerDAO customerDAO,
            OrderDAO orderDAO) {
        this.addressDAO = addressDAO;
        this.creditCardDAO = creditCardDAO;
        this.customerDAO = customerDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Retrieves a single order from the system.
     * <p>
     * If the specified order does not exist, and {@link IllegalArgumentException} is thrown
     *
     * @param orderId the customer id
     * @return the orders
     */
    @Transactional
    public Order getOrder(Long orderId) {
        Order order = orderDAO.get(orderId);

        if (order == null) {
            throw new EntityNotFoundException(Order.class, orderId);
        }

        return order;
    }

    /**
     * Retrieves all orders associated with the specified customer, sorted in descending order of creation date.
     * <p>
     * If the specified customer does not exist, and {@link EntityNotFoundException} is thrown
     *
     * @param customerId the customer id
     * @return the orders
     */
    @Transactional
    public List<Order> getAllOrders(Long customerId) {
        Customer customer = customerDAO.get(customerId);

        if (customer == null) {
            throw new EntityNotFoundException(Customer.class, customerId);
        }

        return customer.getOrders().stream()
                .sorted(Comparator.comparing(Order::getCreatedDate).reversed()).collect(Collectors.toList());
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
    public Order checkout(Long customerId, Long addressId, Long cardId) {
        Customer customer = customerDAO.get(customerId);
        if (customer == null) {
            throw new EntityNotFoundException(Customer.class, customerId);
        }

        Address address = addressDAO.get(addressId);
        if (address == null) {
            throw new EntityNotFoundException(Address.class, addressId);
        }

        CreditCard card = creditCardDAO.get(cardId);
        if (card == null) {
            throw new EntityNotFoundException(CreditCard.class, cardId);
        }

        Basket basket = customer.getBasket();
        if (basket == null) {
            throw new IllegalStateException("Customer has no basket");
        }
        if (basket.isEmpty()) {
            throw new BasketEmptyException(customerId);
        }

        // Check stock quantities, update stock counts, calculate total cost
        Map<Item, Integer> basketItemCountMap = basket.itemCountMap();
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

        return new Order(customer, new Date(), OrderStatus.NEW, address, totalCost, card);
    }

}
