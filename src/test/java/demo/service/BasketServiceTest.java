package demo.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Comparator;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import demo.BaseUnitTest;
import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.entity.Basket;
import demo.entity.Customer;
import demo.entity.Item;
import demo.exception.BasketItemException;
import demo.exception.EntityNotFoundException;

public class BasketServiceTest extends BaseUnitTest {

    @Mock(lenient = true)
    private ItemDAO itemDAO;

    @Mock(lenient = true)
    private CustomerDAO customerDAO;

    @InjectMocks
    public BasketService service;

    @Test
    public void addToBasket_expectEntityNotFoundException_whenCustomerDoesNotExist() {
        Long customerId = 101L, itemId = 202L;

        when(customerDAO.get(eq(customerId))).thenReturn(null);
        when(itemDAO.get(eq(itemId))).thenReturn(mock(Item.class));

        EntityNotFoundException expected = new EntityNotFoundException(Customer.class, customerId);
        expectException(expected.getClass(), expected.getMessage());
        service.addItemToBasket(customerId, itemId);
    }

    @Test
    public void addToBasket_expectEntityNotFoundException_whenItemDoesNotExist() {
        Long customerId = 303L, itemId = 404L;

        when(customerDAO.get(eq(customerId))).thenReturn(mock(Customer.class));
        when(itemDAO.get(eq(itemId))).thenReturn(null);

        EntityNotFoundException expected = new EntityNotFoundException(Item.class, itemId);
        expectException(expected.getClass(), expected.getMessage());
        service.addItemToBasket(customerId, itemId);
    }

    @Test
    public void addToBasket_verifyNewBasketCreated_whenNoBasketExists() {
        Long customerId = 303L, itemId = 404L;

        Customer customer = new Customer();
        Item item = new Item("Sony", "VAIO01", 5);

        when(customerDAO.get(eq(customerId))).thenReturn(customer);
        when(itemDAO.get(eq(itemId))).thenReturn(item);

        Basket basket = service.addItemToBasket(customerId, itemId);
        assertEquals(customer, basket.getCustomer());
        assertEquals(1, basket.getItems().size());
        assertEquals(item, basket.getItems().get(0));
    }

    @Test
    public void addToBasket_verifyItemAddedToExisting_whenValid() {
        Long customerId = 505L, item2Id = 606L;

        Item item1 = new Item("LG", "WIDE01", 78);
        Item item2 = new Item("Samsung", "GALAXY01", 909);

        Customer customer = new Customer();
        Basket basket = new Basket(customer);
        customer.setBasket(basket);
        basket.addItem(item1);

        when(customerDAO.get(eq(customerId))).thenReturn(customer);
        when(itemDAO.get(eq(item2Id))).thenReturn(item2);

        Basket result = service.addItemToBasket(customerId, item2Id);
        assertEquals(customer, result.getCustomer());
        List<Item> items = result.getItems();
        assertEquals(2, items.size());
        items.sort(Comparator.comparing(Item::getManufacturer));
        assertEquals(item1, items.get(0));
        assertEquals(item2, items.get(1));
    }

    @Test
    public void removeFromBasket_expectEntityNotFoundException_whenCustomerNotExists() {
        Long customerId = 606L, itemId = 707L;

        when(customerDAO.get(customerId)).thenReturn(null);
        when(itemDAO.get(itemId)).thenReturn(mock(Item.class));

        EntityNotFoundException expected = new EntityNotFoundException(Customer.class, customerId);
        expectException(expected.getClass(), expected.getMessage());

        service.removeItemFromBasket(customerId, itemId);
    }

    @Test
    public void removeFromBasket_expectEntityNotFoundException_whenItemNotExists() {
        Long customerId = 808L, itemId = 909L;

        when(customerDAO.get(customerId)).thenReturn(mock(Customer.class));
        when(itemDAO.get(itemId)).thenReturn(null);

        EntityNotFoundException expected = new EntityNotFoundException(Item.class, itemId);
        expectException(expected.getClass(), expected.getMessage());

        service.removeItemFromBasket(customerId, itemId);
    }

    @Test
    public void removeFromBasket_expectIllegalStateException_whenCustomerHasNoBasket() {
        Long customerId = 1010L, itemId = 1111L;

        Customer customer = mock(Customer.class);
        when(customer.getBasket()).thenReturn(null);
        when(customerDAO.get(customerId)).thenReturn(customer);
        when(itemDAO.get(itemId)).thenReturn(mock(Item.class));

        expectException(IllegalStateException.class, "Customer has no basket");

        service.removeItemFromBasket(customerId, itemId);
    }

    @Test
    public void removeFromBasket_expectBasketItemException_whenBasketDoesNotContainItem() {
        Long customerId = 1111L, itemId = 1212L;

        Basket basket = new Basket();
        Customer customer = new Customer();
        customer.setBasket(basket);

        when(customerDAO.get(customerId)).thenReturn(customer);
        when(itemDAO.get(itemId)).thenReturn(mock(Item.class));

        BasketItemException expected = new BasketItemException(itemId);

        expectException(expected.getClass(), expected.getMessage());

        service.removeItemFromBasket(customerId, itemId);
    }

    @Test
    public void removeFromBasket_expectUpdatedBasketReturned_whenBasketContainsItem() {
        Long customerId = 2020L, itemId = 3030L;

        Item basketItem = new Item("Sharp", "TSA0001", 9090);
        ReflectionTestUtils.setField(basketItem, "id", itemId);

        Basket basket = new Basket();
        basket.addItem(basketItem);
        Customer customer = new Customer();
        customer.setBasket(basket);

        when(customerDAO.get(customerId)).thenReturn(customer);
        when(itemDAO.get(itemId)).thenReturn(basketItem);

        Basket result = service.removeItemFromBasket(customerId, itemId);
        assertEquals(0, result.getItems().size());
    }

}
