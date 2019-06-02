package demo.service;

import static java.util.Arrays.asList;
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
import demo.converter.OrderConverter;
import demo.dao.AddressDAO;
import demo.dao.CreditCardDAO;
import demo.dao.CustomerDAO;
import demo.dao.OrderDAO;
import demo.entity.dto.OrderDetails;
import demo.entity.dto.OrderSummary;
import demo.entity.persistent.Customer;
import demo.entity.persistent.Order;
import demo.exception.EntityNotFoundException;

public class OrderServiceTest extends BaseUnitTest {

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private OrderConverter orderConverter;

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

        OrderDetails orderDetails = mock(OrderDetails.class);
        when(orderConverter.toOrderDetails(eq(order))).thenReturn(orderDetails);

        OrderDetails actual = orderService.getOrder(orderId);
        assertEquals(orderDetails, actual);
    }

    @Test
    public void findAllForCustomer_expectEntityNotFoundException_whenCustomerNotExists() {
        Long customerId = 979L;
        EntityNotFoundException expected = new EntityNotFoundException(Customer.class, customerId);

        when(customerDAO.get(eq(customerId))).thenReturn(null);

        expectException(expected.getClass(), expected.getMessage());
        orderService.findAllForCustomer(customerId);
    }

    @Test
    public void findAllForCustomer_expectOrdersReturned_sortedByNewestDate_whenCustomerExists_withOrders() {
        long customerId = 7879L;
        long now = new Date().getTime();

        Date d1 = new Date(now - 600_000L);
        Date d2 = new Date(now - 300_000L);
        Date d3 = new Date(now - 60_000L);

        Order o1 = mock(Order.class);
        when(o1.getCreatedDate()).thenReturn(d1);
        OrderSummary os1 = mock(OrderSummary.class);
        when(orderConverter.toOrderSummary(eq(o1))).thenReturn(os1);

        Order o2 = mock(Order.class);
        when(o2.getCreatedDate()).thenReturn(d2);
        OrderSummary os2 = mock(OrderSummary.class);
        when(orderConverter.toOrderSummary(eq(o2))).thenReturn(os2);

        Order o3 = mock(Order.class);
        when(o3.getCreatedDate()).thenReturn(d3);
        OrderSummary os3 = mock(OrderSummary.class);
        when(orderConverter.toOrderSummary(eq(o3))).thenReturn(os3);

        Customer customer = mock(Customer.class);
        Set<Order> orders = new HashSet<>(asList(o2, o3, o1));
        when(customer.getOrders()).thenReturn(orders);
        when(customerDAO.get(customerId)).thenReturn(customer);

        List<OrderSummary> result = orderService.findAllForCustomer(customerId);
        assertEquals(3, result.size());
        assertEquals(os3, result.get(0));
        assertEquals(os2, result.get(1));
        assertEquals(os1, result.get(2));
    }
}
