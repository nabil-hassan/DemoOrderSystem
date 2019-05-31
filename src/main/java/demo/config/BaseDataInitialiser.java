package demo.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;


import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.dao.OrderDAO;
import demo.entity.Address;
import demo.entity.CreditCard;
import demo.entity.Customer;
import demo.entity.Item;
import demo.entity.Order;
import demo.entity.OrderStatus;
import demo.entity.Title;

/**
 * Sets up some persistent base data.
 */
public class BaseDataInitialiser {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDataInitialiser.class);

    private static boolean initialised = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy HH:mm:ss");
    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;
    private OrderDAO orderDAO;

    public BaseDataInitialiser(CustomerDAO customerDAO, ItemDAO itemDAO, OrderDAO orderDAO) {
        this.customerDAO = customerDAO;
        this.itemDAO = itemDAO;
        this.orderDAO = orderDAO;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // ContextStartedEvent will not work here, since start() is never explicitly called on the context
        if (!initialised) {
            event.getApplicationContext().getBean(BaseDataInitialiser.class).setupDatabase();
            initialised = true;
        }
    }

    @Transactional
    public void setupDatabase() {
        // ITEMS
        Item item = new Item("Asus", "A01", 505, 20.00);
        itemDAO.create(item);

        Item item2 = new Item("Benq", "B01", 2, 45.00);
        itemDAO.create(item2);

        Item item3 = new Item("Casio", "C01", 3, 75.00);
        itemDAO.create(item3);

        Item item4 = new Item("Dulux", "D01", 100, 95.00);
        itemDAO.create(item4);

        Item item5 = new Item("Epsom", "E01", 100, 115.99);
        itemDAO.create(item5);

        // CUSTOMERS
        Address mantisAddress = new Address("UK", "Bedford", "Luton", "LU4 8AW", "1 Maple Road");
        Customer mantis = new Customer(Title.DR, "Mantis", "Jones", "07909888888", "mantis.jones@gmail.com");
        mantis.addAddress(mantisAddress);
        customerDAO.create(mantis);
        CreditCard mantisCard = new CreditCard(mantis, "Mr M Jones", "1020304050607080", "10-20-30", "09/19",
                999);
        mantis.addCreditCard(mantisCard);

        Address aliceAddress = new Address("UK", "Sussex", "Worthing", "BN133RB", "7 Cigent Estate");
        Customer alice = new Customer(Title.MS, "Alice", "Wright", "07999999999", "alice.wright@yahoo.com");
        alice.addAddress(aliceAddress);
        customerDAO.create(alice);
        CreditCard aliceCard = new CreditCard(alice, "Ms A Wright", "8070605040302010", "40-50-60", "11/19",
                677);
        alice.addCreditCard(aliceCard);

        //ORDERS
        Order mantisOrder1 = new Order(mantis, toDate("20/08/2018 23:11:12"), OrderStatus.DELIVERED,
                mantisAddress, 200.00, mantisCard);
        mantisOrder1.setDeliveredDate(toDate("21/08/2018 00:11:12"));
        mantisOrder1.addItem(item);
        mantisOrder1.addItem(item2);
        mantis.addOrder(mantisOrder1);

        Order mantisOrder2 = new Order(mantis, toDate("10/10/2017 20:11:12"), OrderStatus.CANCELLED,
                mantisAddress, 115.99, mantisCard);
        mantisOrder2.setCancelledDate(toDate("10/10/2017 21:31:12"));
        mantisOrder2.addItem(item5);
        mantis.addOrder(mantisOrder2);

        Order aliceOrder = new Order(alice, toDate("23/01/2019 19:00:32"), OrderStatus.DELIVERED,
                aliceAddress, 45.00, aliceCard);
        aliceOrder.setDeliveredDate(toDate("23/01/2019 123:00:32"));
        aliceOrder.addItem(item2);
        alice.addOrder(aliceOrder);
    }

    private Date toDate(String s) {
        try {
            return sdf.parse(s);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to parse provided date string", ex);
        }
    }

}
