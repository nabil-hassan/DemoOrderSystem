package demo.service;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import demo.BaseUnitTest;
import demo.dao.AddressDAO;
import demo.dao.CreditCardDAO;
import demo.dao.CustomerDAO;
import demo.dao.OrderDAO;
import demo.entity.Address;
import demo.entity.Basket;
import demo.entity.CreditCard;
import demo.entity.Customer;
import demo.entity.Item;
import demo.entity.Order;
import demo.entity.OrderStatus;
import demo.exception.BasketEmptyException;
import demo.exception.EntityNotFoundException;
import demo.exception.InsufficientStockException;

public class OrderServiceTest extends BaseUnitTest {

    @Mock(lenient = true)
    private AddressDAO addressDAO;

    @Mock(lenient = true)
    private CreditCardDAO creditCardDAO;

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private OrderDAO orderDAO;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void getOrder_expectEntityNotFoundException_whenSpecifiedOrderNotExists() {
        Long orderId = 9090L;
        EntityNotFoundException expected = new EntityNotFoundException(Order.class, orderId);

        when(orderDAO.get(eq(orderId))).thenReturn(null);

        expectException(expected.getClass(), expected.getMessage());
        orderService.getOrder(orderId);
    }

    @Test
    public void getOrder_expectEntityReturned_whenExists() {
        Long orderId = 6080L;

        Order order = mock(Order.class);
        when(orderDAO.get(eq(orderId))).thenReturn(order);

        Order actual = orderService.getOrder(orderId);
        assertEquals(order, actual);
    }

    @Test
    public void getAllOrders_expectEntityNotFoundException_whenCustomerNotExists() {
        Long customerId = 979L;
        EntityNotFoundException expected = new EntityNotFoundException(Customer.class, customerId);

        when(customerDAO.get(eq(customerId))).thenReturn(null);

        expectException(expected.getClass(), expected.getMessage());
        orderService.getAllOrders(customerId);
    }

    @Test
    public void getAllOrders_expectOrdersReturned_sortedByDate_whenCustomerExists_withOrders() {
        long customerId = 7879L;
        long now = new Date().getTime();

        Date d1 = new Date(now - 600_000L);
        Date d2 = new Date(now - 300_000L);
        Date d3 = new Date(now - 60_000L);

        Order o1 = mock(Order.class);
        when(o1.getCreatedDate()).thenReturn(d1);

        Order o2 = mock(Order.class);
        when(o2.getCreatedDate()).thenReturn(d2);

        Order o3 = mock(Order.class);
        when(o3.getCreatedDate()).thenReturn(d3);

        Customer customer = mock(Customer.class);
        Set<Order> orders = new HashSet<>(asList(o2, o3, o1));
        when(customer.getOrders()).thenReturn(orders);
        when(customerDAO.get(customerId)).thenReturn(customer);

        List<Order> result = orderService.getAllOrders(customerId);
        assertEquals(3, result.size());
        assertEquals(d3, result.get(0).getCreatedDate());
        assertEquals(d2, result.get(1).getCreatedDate());
        assertEquals(d1, result.get(2).getCreatedDate());
    }

    @Test
    public void checkout_expectEntityNotFoundException_whenCustomerDoesNotExist() {
        Long addressId = 101L, cardId = 202L, customerId = 303L;

        when(addressDAO.get(addressId)).thenReturn(mock(Address.class));
        when(creditCardDAO.get(cardId)).thenReturn(mock(CreditCard.class));
        when(customerDAO.get(customerId)).thenReturn(null);

        EntityNotFoundException expected = new EntityNotFoundException(Customer.class, customerId);
        expectException(expected.getClass(), expected.getMessage());

        orderService.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectEntityNotFoundException_whenAddressDoesNotExist() {
        Long addressId = 404L, cardId = 505L, customerId = 606L;

        when(addressDAO.get(addressId)).thenReturn(null);
        when(creditCardDAO.get(cardId)).thenReturn(mock(CreditCard.class));
        when(customerDAO.get(customerId)).thenReturn(mock(Customer.class));

        EntityNotFoundException expected = new EntityNotFoundException(Address.class, addressId);
        expectException(expected.getClass(), expected.getMessage());

        orderService.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectEntityNotFoundException_whenCardDoesNotExist() {
        Long addressId = 707L, cardId = 808L, customerId = 909L;

        when(addressDAO.get(addressId)).thenReturn(mock(Address.class));
        when(creditCardDAO.get(cardId)).thenReturn(null);
        when(customerDAO.get(customerId)).thenReturn(mock(Customer.class));

        EntityNotFoundException expected = new EntityNotFoundException(CreditCard.class, cardId);
        expectException(expected.getClass(), expected.getMessage());

        orderService.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectBasketEmptyException_whenBasketIsEmpty() {
        Long addressId = 1010L, cardId = 1111L, customerId = 1212L;

        when(addressDAO.get(addressId)).thenReturn(mock(Address.class));
        when(creditCardDAO.get(cardId)).thenReturn(mock(CreditCard.class));
        Customer customer = mock(Customer.class);
        Basket basket = mock(Basket.class);
        when(customerDAO.get(customerId)).thenReturn(customer);
        when(customer.getBasket()).thenReturn(basket);
        when(basket.isEmpty()).thenReturn(true);

        BasketEmptyException expected = new BasketEmptyException(customerId);
        expectException(expected.getClass(), expected.getMessage());

        orderService.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectInsufficientStockException_whenBasketItemCountExceedsStockCount() {
        Long addressId = 1010L, cardId = 1111L, customerId = 1212L;

        Item i1 = mock(Item.class);
        when(i1.getStockQuantity()).thenReturn(99);
        when(i1.getCost()).thenReturn(90.00);

        Item i2 = mock(Item.class);
        when(i2.getStockQuantity()).thenReturn(1);
        when(i2.getCost()).thenReturn(12.00);

        when(addressDAO.get(addressId)).thenReturn(mock(Address.class));
        when(creditCardDAO.get(cardId)).thenReturn(mock(CreditCard.class));

        Customer customer = mock(Customer.class);
        when(customerDAO.get(customerId)).thenReturn(customer);

        Basket basket = new Basket();
        basket.getItems().addAll(asList(i1, i2, i2));
        when(customer.getBasket()).thenReturn(basket);

        InsufficientStockException expected = new InsufficientStockException(singletonList(i2.getId()));
        expectException(expected.getClass(), expected.getMessage());

        orderService.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectNewOrderReturned_whenValidationChecksPass() {
        Long addressId = 1313L, cardId = 1414L, customerId = 1515L;
        int i1Stock = 101, i2Stock = 333, i3Stock = 555;
        double i1Cost = 45.99, i2Cost = 23.45, i3Cost = 90.99;

        Item i1 = new Item();
        i1.setManufacturer("Sony");
        i1.setModelNumber("MN001");
        i1.setStockQuantity(i1Stock);
        i1.setCost(i1Cost);

        Item i2 = new Item();
        i2.setManufacturer("Philips");
        i2.setModelNumber("PP002");
        i2.setStockQuantity(i2Stock);
        i2.setCost(i2Cost);

        Item i3 = new Item();
        i3.setManufacturer("LG");
        i3.setModelNumber("LG003");
        i3.setStockQuantity(i3Stock);
        i3.setCost(i3Cost);

        Address address = mock(Address.class);
        when(addressDAO.get(addressId)).thenReturn(address);

        CreditCard card = mock(CreditCard.class);
        when(creditCardDAO.get(cardId)).thenReturn(card);

        Customer customer = mock(Customer.class);
        when(customerDAO.get(customerId)).thenReturn(customer);

        Basket basket = new Basket();
        basket.getItems().addAll(asList(i1, i2, i3, i3));
        when(customer.getBasket()).thenReturn(basket);

        Double expectedTotalCost = i1Cost + i2Cost + (i3Cost *2);

        Order order = orderService.checkout(customerId, addressId, cardId);
        assertEquals(OrderStatus.NEW, order.getStatus());
        assertEquals(address, order.getDeliveryAddress());
        assertEquals(card, order.getCreditCard());
        assertEquals(expectedTotalCost, order.getTotalCost());
        assertEquals(customer, order.getCustomer());

        Integer i1ExpectedStock = i1Stock - 1;
        assertEquals(i1ExpectedStock, i1.getStockQuantity());

        Integer i2ExpectedStock = i2Stock - 1;
        assertEquals(i2ExpectedStock, i2.getStockQuantity());

        Integer i3ExpectedStock = i3Stock - 2;
        assertEquals(i3ExpectedStock, i3.getStockQuantity());
    }

}
