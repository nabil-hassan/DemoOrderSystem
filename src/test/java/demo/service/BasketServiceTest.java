package demo.service;

import static demo.entity.persistent.OrderStatus.NEW;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import demo.BaseUnitTest;
import demo.converter.BasketConverter;
import demo.converter.ItemConverter;
import demo.converter.OrderConverter;
import demo.dao.AddressDAO;
import demo.dao.CreditCardDAO;
import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.dao.OrderDAO;
import demo.entity.dto.BasketDetails;
import demo.entity.persistent.Address;
import demo.entity.persistent.Basket;
import demo.entity.persistent.CreditCard;
import demo.entity.persistent.Customer;
import demo.entity.persistent.Item;
import demo.entity.persistent.Order;
import demo.exception.BasketEmptyException;
import demo.exception.ItemNotInBasketException;
import demo.exception.EntityNotAssociatedException;
import demo.exception.EntityNotFoundException;
import demo.exception.InsufficientStockException;

public class BasketServiceTest extends BaseUnitTest {

    @Mock
    private ItemDAO itemDAO;

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private AddressDAO addressDAO;

    @Mock
    private BasketConverter basketConverter;

    @Mock
    private CreditCardDAO creditCardDAO;

    @Mock
    private ItemConverter itemConverter;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private OrderConverter orderConverter;

    @InjectMocks
    public BasketService service;

    @Test
    public void addItemToBasket_expectEntityNotFoundException_whenCustomerDoesNotExist() {
        Long customerId = 101L, itemId = 202L;

        when(customerDAO.get(eq(customerId))).thenReturn(null);

        EntityNotFoundException expected = new EntityNotFoundException(Customer.class, customerId);
        expectException(expected.getClass(), expected.getMessage());
        service.addItemToBasket(customerId, itemId);
    }

    @Test
    public void addItemToBasket_expectEntityNotFoundException_whenItemDoesNotExist() {
        Long customerId = 303L, itemId = 404L;

        when(customerDAO.get(eq(customerId))).thenReturn(mock(Customer.class));
        when(itemDAO.get(eq(itemId))).thenReturn(null);

        EntityNotFoundException expected = new EntityNotFoundException(Item.class, itemId);
        expectException(expected.getClass(), expected.getMessage());
        service.addItemToBasket(customerId, itemId);
    }

    @Test
    public void addItemToBasket_verifyItemAddedToExisting_whenValid() {
        Long customerId = 505L, item2Id = 707L;

        Item i2 = mock(Item.class);

        Customer c = mock(Customer.class);
        Basket b = mock(Basket.class);
        when(c.getBasket()).thenReturn(b);

        when(customerDAO.get(eq(customerId))).thenReturn(c);
        when(itemDAO.get(eq(item2Id))).thenReturn(i2);

        BasketDetails bd = mock(BasketDetails.class);
        when(basketConverter.toBasketDetails(eq(b))).thenReturn(bd);

        BasketDetails result = service.addItemToBasket(customerId, item2Id);
        assertEquals(bd, result);
        verify(b).addItem(eq(i2));
    }

    @Test
    public void getBasketDetails_verifyEntityNotFoundExceptionThrown_ifCustomerNotExists() {
        Long customerId = 90238L;

        when(customerDAO.get(eq(customerId))).thenReturn(null);

        EntityNotFoundException ex = new EntityNotFoundException(Customer.class, customerId);
        expectException(ex.getClass(), ex.getMessage());

        service.getBasketDetails(customerId);
    }

    @Test
    public void getBasketDetails_verifyCorrectDetailsReturned_whenEntityExists() {
        Long customerId = 890L;

        Customer c = mock(Customer.class);
        Basket b = mock(Basket.class);
        when(c.getBasket()).thenReturn(b);

        when(customerDAO.get(eq(customerId))).thenReturn(c);

        BasketDetails bd = mock(BasketDetails.class);
        when(basketConverter.toBasketDetails(eq(b))).thenReturn(bd);

        BasketDetails result = service.getBasketDetails(customerId);
        assertEquals(bd, result);
    }

    @Test
    public void removeFromBasket_expectEntityNotFoundException_whenCustomerNotExists() {
        Long customerId = 606L, itemId = 707L;

        when(customerDAO.get(customerId)).thenReturn(null);

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
        Long customerId = 1111L, itemId = 1212L, otherItemId = 9090L;

        Item other = mock(Item.class);
        when(other.getId()).thenReturn(otherItemId);

        Basket b = mock(Basket.class);
        Customer c = mock(Customer.class);
        when(c.getBasket()).thenReturn(b);
        when(b.getItems()).thenReturn(singletonList(other));

        when(customerDAO.get(customerId)).thenReturn(c);
        when(itemDAO.get(itemId)).thenReturn(mock(Item.class));

        ItemNotInBasketException expected = new ItemNotInBasketException(itemId);
        expectException(expected.getClass(), expected.getMessage());

        service.removeItemFromBasket(customerId, itemId);
    }

    @Test
    public void removeFromBasket_expectItemRemovedOnce_whenBasketContainsItemQtyGt1() {
        Long customerId = 1111L, itemId = 1212L;

        Item i = mock(Item.class);
        when(i.getId()).thenReturn(itemId);

        Basket b = mock(Basket.class);
        Customer c = mock(Customer.class);
        when(c.getBasket()).thenReturn(b);

        List<Item> items = new ArrayList<>();
        items.add(i);
        items.add(i);
        when(b.getItems()).thenReturn(items);

        when(customerDAO.get(customerId)).thenReturn(c);
        when(itemDAO.get(itemId)).thenReturn(mock(Item.class));

        service.removeItemFromBasket(customerId, itemId);
        assertEquals(1, b.getItems().size());
        assertEquals(i, b.getItems().get(0));
    }

    @Test
    public void checkout_expectEntityNotFoundException_whenCustomerDoesNotExist() {
        Long addressId = 101L, cardId = 202L, customerId = 303L;

        EntityNotFoundException expected = new EntityNotFoundException(Customer.class, customerId);
        expectException(expected.getClass(), expected.getMessage());

        service.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectEntityNotFoundException_whenAddressDoesNotExist() {
        Long addressId = 404L, cardId = 505L, customerId = 606L;

        when(addressDAO.get(addressId)).thenReturn(null);
        when(customerDAO.get(customerId)).thenReturn(mock(Customer.class));

        EntityNotFoundException expected = new EntityNotFoundException(Address.class, addressId);
        expectException(expected.getClass(), expected.getMessage());

        service.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectEntityNotFoundException_whenCardDoesNotExist() {
        Long addressId = 707L, cardId = 808L, customerId = 909L;

        Address address = mock(Address.class);
        when(addressDAO.get(addressId)).thenReturn(address);

        Customer customer = new Customer();
        when(customerDAO.get(customerId)).thenReturn(customer);
        customer.addAddress(address);

        EntityNotFoundException expected = new EntityNotFoundException(CreditCard.class, cardId);
        expectException(expected.getClass(), expected.getMessage());

        service.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectIllegalStateException_whenNoBasket() {
        Long addressId = 1010L, cardId = 1111L, customerId = 1212L;

        Address address = mock(Address.class);
        when(addressDAO.get(addressId)).thenReturn(address);
        CreditCard card = mock(CreditCard.class);
        when(creditCardDAO.get(cardId)).thenReturn(card);

        Customer customer = mock(Customer.class);
        when(customer.getAddresses()).thenReturn(Collections.singleton(address));
        when(customer.getCards()).thenReturn(Collections.singleton(card));
        when(customer.getBasket()).thenReturn(null);
        when(customerDAO.get(customerId)).thenReturn(customer);

        expectException(IllegalStateException.class, "Customer has no basket");

        service.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectBasketEmptyException_whenBasketIsEmpty() {
        Long addressId = 1010L, cardId = 1111L, customerId = 1212L;

        Address address = mock(Address.class);
        when(addressDAO.get(addressId)).thenReturn(address);
        CreditCard card = mock(CreditCard.class);
        when(creditCardDAO.get(cardId)).thenReturn(card);

        Customer customer = mock(Customer.class);
        when(customer.getAddresses()).thenReturn(Collections.singleton(address));
        when(customer.getCards()).thenReturn(Collections.singleton(card));
        when(customerDAO.get(customerId)).thenReturn(customer);

        Basket basket = mock(Basket.class);
        when(customer.getBasket()).thenReturn(basket);
        when(basket.isEmpty()).thenReturn(true);

        BasketEmptyException expected = new BasketEmptyException(customerId);
        expectException(expected.getClass(), expected.getMessage());

        service.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectEntityNotAssociatedException_whenAddressNotAssociatedWithCustomer() {
        Long addressId = 1010L, cardId = 1111L, customerId = 1212L;

        Customer c = new Customer();
        when(customerDAO.get(customerId)).thenReturn(c);
        when(addressDAO.get(addressId)).thenReturn(mock(Address.class));

        EntityNotAssociatedException expected =
                new EntityNotAssociatedException(Customer.class, customerId, Address.class, addressId);
        expectException(expected.getClass(), expected.getMessage());

        service.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectEntityNotAssociatedException_whenCardNotAssociatedWithCustomer() {
        Long addressId = 1010L, cardId = 1111L, customerId = 1212L;

        Address address = mock(Address.class);
        when(addressDAO.get(addressId)).thenReturn(address);
        when(creditCardDAO.get(cardId)).thenReturn(mock(CreditCard.class));

        Customer c = new Customer();
        when(customerDAO.get(customerId)).thenReturn(c);
        c.addAddress(address);

        EntityNotAssociatedException expected =
                new EntityNotAssociatedException(Customer.class, customerId, CreditCard.class, cardId);
        expectException(expected.getClass(), expected.getMessage());

        service.checkout(customerId, addressId, cardId);
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

        Address address = mock(Address.class);
        when(addressDAO.get(addressId)).thenReturn(address);
        CreditCard card = mock(CreditCard.class);
        when(creditCardDAO.get(cardId)).thenReturn(card);

        Customer customer = mock(Customer.class);
        when(customer.getAddresses()).thenReturn(Collections.singleton(address));
        when(customer.getCards()).thenReturn(Collections.singleton(card));
        when(customerDAO.get(customerId)).thenReturn(customer);

        Basket basket = mock(Basket.class);
        List<Item> items = asList(i1, i2, i2);
        when(basket.getItems()).thenReturn(items);
        when(customer.getBasket()).thenReturn(basket);

        Map<Item, Integer> itemCountMap = new HashMap<>();
        itemCountMap.put(i1, 1);
        itemCountMap.put(i2, 2);
        when(itemConverter.toItemCountMap(items)).thenReturn(itemCountMap);

        InsufficientStockException expected = new InsufficientStockException(singletonList(i2.getId()));
        expectException(expected.getClass(), expected.getMessage());

        service.checkout(customerId, addressId, cardId);
    }

    @Test
    public void checkout_expectOrderAdded_itemQuantityDecremented_basketCleared_whenCheckoutSuccess() {
        Long addressId = 1010L, cardId = 1111L, customerId = 1212L;

        Item i1 = mock(Item.class);
        when(i1.getStockQuantity()).thenReturn(55);
        when(i1.getCost()).thenReturn(90.00);

        Item i2 = mock(Item.class);
        when(i2.getStockQuantity()).thenReturn(101);
        when(i2.getCost()).thenReturn(12.00);

        Address address = mock(Address.class);
        when(addressDAO.get(addressId)).thenReturn(address);
        CreditCard card = mock(CreditCard.class);
        when(creditCardDAO.get(cardId)).thenReturn(card);

        Customer customer = mock(Customer.class);
        when(customer.getAddresses()).thenReturn(Collections.singleton(address));
        when(customer.getCards()).thenReturn(Collections.singleton(card));
        when(customerDAO.get(customerId)).thenReturn(customer);

        Basket basket = mock(Basket.class);
        List<Item> items = asList(i1, i2, i2);
        when(basket.getItems()).thenReturn(items);
        when(customer.getBasket()).thenReturn(basket);

        Map<Item, Integer> itemCountMap = new HashMap<>();
        itemCountMap.put(i1, 1);
        itemCountMap.put(i2, 2);
        when(itemConverter.toItemCountMap(items)).thenReturn(itemCountMap);

        service.checkout(customerId, addressId, cardId);

        // verify order created with correct details
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(customer).addOrder(orderCaptor.capture());
        assertEquals(1, orderCaptor.getAllValues().size());
        Order order = orderCaptor.getValue();
        assertEquals(NEW, order.getStatus());
        assertEquals(customer, order.getCustomer());
        assertEquals(address, order.getDeliveryAddress());
        assertEquals(card, order.getCreditCard());
        assertEquals(new Double(114.0), order.getTotalCost());

        // verify item quantities decremented
        verify(i1).setStockQuantity(eq(54));
        verify(i2).setStockQuantity(eq(99));

        // verify basket cleared
        verify(basket).clearItems();
    }

}
