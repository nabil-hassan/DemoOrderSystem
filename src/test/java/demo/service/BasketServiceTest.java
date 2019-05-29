package demo.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import demo.BaseUnitTest;
import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.entity.Customer;
import demo.entity.Item;
import demo.exception.EntityNotFoundException;

public class BasketServiceTest extends BaseUnitTest {

    @Mock
    private ItemDAO itemDAO;

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    public BasketService basketService;

    @Test
    public void addToBasket_expectEntityNotFoundException_whenCustomerDoesNotExist() {
        Long customerId = 101L, itemId = 202L;

        when(customerDAO.get(eq(customerId))).thenReturn(null);
        when(itemDAO.get(eq(itemId))).thenReturn(mock(Item.class));

        EntityNotFoundException expected = new EntityNotFoundException(Customer.class, customerId);
        expectException(expected.getClass(), expected.getMessage());
        basketService.addItemToBasket(customerId, itemId);
    }

    @Test
    public void addToBasket_expectEntityNotFoundException_whenItemDoesNotExist() {
        Long customerId = 303L, itemId = 404L;

        when(customerDAO.get(eq(customerId))).thenReturn(mock(Customer.class));
        when(itemDAO.get(eq(itemId))).thenReturn(null);

        EntityNotFoundException expected = new EntityNotFoundException(Item.class, itemId);
        expectException(expected.getClass(), expected.getMessage());
        basketService.addItemToBasket(customerId, itemId);
    }

    @Test
    public void addToBasket_verifyNewBasketCreated_whenNoBasketExists() {

    }

    @Test
    public void addToBasket_verifyItemAdded_whenValid() {

    }

}
