package demo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import demo.converter.OrderConverter;
import demo.dao.AddressDAO;
import demo.dao.CreditCardDAO;
import demo.dao.CustomerDAO;
import demo.dao.OrderDAO;
import demo.entity.dto.OrderDetails;
import demo.entity.persistent.Address;
import demo.entity.persistent.Basket;
import demo.entity.persistent.CreditCard;
import demo.entity.persistent.Customer;
import demo.entity.persistent.Item;
import demo.entity.persistent.Order;
import demo.entity.persistent.OrderStatus;
import demo.entity.dto.OrderSummary;
import demo.exception.BasketEmptyException;
import demo.exception.EntityNotAssociatedException;
import demo.exception.EntityNotFoundException;
import demo.exception.InsufficientStockException;

public class OrderService {

    private OrderConverter orderConverter;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;

    public OrderService(CustomerDAO customerDAO,
            OrderDAO orderDAO, OrderConverter orderConverter) {
        this.customerDAO = customerDAO;
        this.orderDAO = orderDAO;
        this.orderConverter = orderConverter;
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
    public OrderDetails getOrder(Long orderId) {
        Order order = orderDAO.get(orderId);

        if (order == null) {
            throw new EntityNotFoundException(Order.class, orderId);
        }

        order.getItems(); //initialises collection

        return orderConverter.toOrderDetails(order);
    }

    @Transactional
    public List<OrderSummary> findAll() {
        return orderDAO.findAll().stream().sorted(Comparator.comparing(Order::getCreatedDate).reversed())
                .map(orderConverter::toOrderSummary)
                .collect(Collectors.toList());
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
    public List<OrderSummary> findAllForCustomer(Long customerId) {
        Customer customer = customerDAO.get(customerId);

        if (customer == null) {
            throw new EntityNotFoundException(Customer.class, customerId);
        }

        return customer.getOrders().stream().sorted(Comparator.comparing(Order::getCreatedDate).reversed())
                .map(orderConverter::toOrderSummary)
                .collect(Collectors.toList());
    }

}
