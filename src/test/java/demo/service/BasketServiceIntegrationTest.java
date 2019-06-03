package demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.dao.OrderDAO;
import demo.entity.persistent.Address;
import demo.entity.persistent.Basket;
import demo.entity.persistent.CreditCard;
import demo.entity.persistent.Customer;
import demo.entity.persistent.Item;
import demo.entity.persistent.Order;
import demo.entity.persistent.OrderStatus;
import demo.entity.persistent.Title;

public class BasketServiceIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    private BasketService service;

    @Autowired
    private ItemDAO itemDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private OrderDAO orderDAO;

    private Session session;

    @Before
    public void setUp() throws Exception {
        this.session = sessionFactory.getCurrentSession();
    }

    @Test
    public void addItemToBasket_verifyBasketContentsUpdated() {
        Item item = new Item("Asus", "A01", 505, 20.00);
        itemDAO.create(item);

        Address address = new Address("UK", "Bedford", "Luton", "LU4 8AW", "1 Maple Road");
        Customer customer = new Customer(Title.DR, "Mantis", "Jones", "07909888888", "mantis.jones@gmail.com");
        customer.setBasket(new Basket(customer));
        customer.addAddress(address);
        CreditCard card = new CreditCard(customer, "Mr M Jones", "1020304050607080", "10-20-30", "09/19", 999);
        customer.addCreditCard(card);
        customerDAO.create(customer);

        session.flush();

        service.addItemToBasket(customer.getId(), item.getId());

        session.flush();
        session.evict(customer);

        Customer persisted = customerDAO.get(customer.getId());
        Basket persistedBasket = persisted.getBasket();
        assertEquals(1, persistedBasket.getItems().size());
        Item persistedItem = persistedBasket.getItems().get(0);
        assertEquals(item.getId(), persistedItem.getId());
    }

    @Test
    public void removeItemFromBasket_verifyBasketContentsUpdated() {
        Item item = new Item("Asus", "A01", 505, 20.00);
        itemDAO.create(item);

        Item item2 = new Item("Booth", "B01", 707, 90.00);
        itemDAO.create(item2);

        Address address = new Address("UK", "Bedford", "Luton", "LU4 8AW", "1 Maple Road");
        Customer customer = new Customer(Title.DR, "Mantis", "Jones", "07909888888", "mantis.jones@gmail.com");
        Basket basket = new Basket(customer);
        basket.addItem(item);
        basket.addItem(item2);
        customer.setBasket(basket);
        customer.addAddress(address);
        CreditCard card = new CreditCard(customer, "Mr M Jones", "1020304050607080", "10-20-30", "09/19", 999);
        customer.addCreditCard(card);
        customerDAO.create(customer);

        session.flush();

        service.removeItemFromBasket(customer.getId(), item2.getId());

        session.flush();
        session.evict(customer);

        Customer persisted = customerDAO.get(customer.getId());
        Basket persistedBasket = persisted.getBasket();
        assertEquals(1, persistedBasket.getItems().size());
        Item persistedItem = persistedBasket.getItems().get(0);
        assertEquals(item.getId(), persistedItem.getId());
    }

    @Test
    public void checkout_verifyOrderCreated() {
        Item item = new Item("Asus", "A01", 505, 20.00);
        itemDAO.create(item);

        Item item2 = new Item("Booth", "B01", 707, 90.00);
        itemDAO.create(item2);

        Address address = new Address("UK", "Bedford", "Luton", "LU4 8AW", "1 Maple Road");
        Customer customer = new Customer(Title.DR, "Mantis", "Jones", "07909888888", "mantis.jones@gmail.com");
        Basket basket = new Basket(customer);
        basket.addItem(item);
        basket.addItem(item2);
        customer.setBasket(basket);
        customer.addAddress(address);
        CreditCard card = new CreditCard(customer, "Mr M Jones", "1020304050607080", "10-20-30", "09/19", 999);
        customer.addCreditCard(card);
        customerDAO.create(customer);

        session.flush();

        service.checkout(customer.getId(), address.getId(), card.getId());

        session.flush();
        session.evict(customer);
        session.evict(item);
        session.evict(item2);

        // verify basket was cleared
        Customer persistedCustomer = customerDAO.get(customer.getId());
        Basket persistedCustomerBasket = persistedCustomer.getBasket();
        assertTrue(persistedCustomerBasket.isEmpty());

        // verify item quantities were decremented
        Item persistedItem1 = itemDAO.get(item.getId());
        assertEquals(new Integer(504), persistedItem1.getStockQuantity());

        Item persistedItem2 = itemDAO.get(item2.getId());
        assertEquals(new Integer(706), persistedItem2.getStockQuantity());

        // verify order was created correctly
        List<Order> orders = orderDAO.findAll();
        assertEquals(1, orders.size());
        Order persistedOrder = orders.get(0);
        assertEquals(OrderStatus.NEW, persistedOrder.getStatus());
        assertEquals(customer.getId(), persistedOrder.getCustomer().getId());
        assertEquals(address, persistedOrder.getDeliveryAddress());
        assertEquals(card, persistedOrder.getCreditCard());
        List<Item> orderItems = persistedOrder.getItems();
        assertEquals(2, orderItems.size());
        orderItems.sort(Comparator.comparing(Item::getId));
        assertEquals(item.getId(), orderItems.get(0).getId());
        assertEquals(item2.getId(), orderItems.get(1).getId());
        assertEquals(new Double(110.00), persistedOrder.getTotalCost());
    }

}
