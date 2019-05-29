package demo.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import demo.entity.Address;
import demo.entity.Basket;
import demo.entity.CreditCard;
import demo.entity.Customer;
import demo.entity.Item;
import demo.entity.Order;
import demo.entity.OrderStatus;
import demo.entity.Title;
import demo.verifier.CustomerVerifier;

public class CustomerDAOTest extends DAOTest {
	
	@Autowired
	private CustomerDAO dao;
	
	@Autowired
	private CustomerVerifier verifier;
	
	private Session session;
	
	@Before
	public void setup() {
		session = dao.getCurrentSession();
	}

	/**
	 * Verifies cascade settings when creating a customer.
	 *
	 * The expectation is that all associated entities should be automatically created when the customer is
	 * persisted, removing the need for each to be manually saved.
	 */
	@Test
	public void create_verifySuccessful() {
		Customer customer = buildCustomer();

		session.evict(customer);

		Customer retrieved = dao.get(customer.getId());
		verifier.verifyValuesMatch(customer, retrieved);
	}

	/**
	 * Verifies cascade settings when removing a customer.
	 *
	 * The expectation is that all associated entities should be removed, except addresses, which are a many:many, and
	 * thus may be in use elsewhere.
	 */
	@Test
	public void delete_verifyCascadeOptions() {
		Customer customer = buildCustomer();

		dao.create(customer);
		session.flush();

		dao.delete(customer);
		session.flush();

		int customerRowCount = countRowsInTable("customer");
		assertEquals(0, customerRowCount);

		int creditCardRowCount = countRowsInTable("credit_card");
		assertEquals(0, creditCardRowCount );

		int basketRowCount = countRowsInTable("basket");
		assertEquals(0, basketRowCount);

		int orderRowCount = countRowsInTable("orders");
		assertEquals(0, orderRowCount);

		int orderItemsRowCount = countRowsInTable("order_items");
		assertEquals(0, orderItemsRowCount);

		int basketItemsRowCount = countRowsInTable("basket_items");
		assertEquals(0, basketItemsRowCount);

		int addressRowCount = countRowsInTable("address");
		assertEquals(1, addressRowCount);
	}

	@Test
	public void update_verifyClearBasketItems_cascades() {
		Customer customer = buildCustomer();

		dao.create(customer);
		session.flush();

		customer.getBasket().clearItems();
		session.flush();

		int basketItemsRowCount = countRowsInTable("basket_items");
		assertEquals(0, basketItemsRowCount);
	}

	private Customer buildCustomer() {
		Item item = new Item("Sony", "SN9090", 99);
		session.save(item);

		Address address = new Address("UK", "Bedford", "Luton", "LU4 8AW", "1 Maple Road");

		Customer customer = new Customer(Title.DR, "Mantis", "Jones", "07909888888", "mantis.jones@gmail.com");
		customer.addAddress(address);
		dao.create(customer);

		CreditCard card = new CreditCard(customer, "Mr M Jones", "1020304050607080", "10-20-30", "09/19", 999);
		customer.addCreditCard(card);

		Basket basket = new Basket(customer);
		basket.addItem(item);

		customer.setBasket(basket);

		Order order = new Order(customer, new Date(), OrderStatus.NEW, address, 10.00, null);
		customer.addOrder(order);
		session.flush();
		return customer;
	}



}
