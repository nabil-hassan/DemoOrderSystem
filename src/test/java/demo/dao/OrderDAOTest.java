package demo.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import demo.entity.persistent.Address;
import demo.entity.persistent.CreditCard;
import demo.entity.persistent.Customer;
import demo.entity.persistent.Item;
import demo.entity.persistent.Order;
import demo.entity.persistent.OrderStatus;
import demo.entity.persistent.Title;
import demo.verifier.OrderVerifier;

public class OrderDAOTest extends DAOTest {

	@Autowired
	private OrderDAO dao;
	
	@Autowired
	private OrderVerifier verifier;
	
	private Session session;

	@Before
	public void setup() {
		session = dao.getCurrentSession();
	}

	/**
	 * Verifies creation of an Order entity, ensuring mappings are correct.
	 */
	@Test
	public void create_verifySuccessfulCreation() {
		Order order = buildOrder();
		Long orderId = (Long) dao.create(order);
		session.flush();

		session.evict(order);

		Order retrieved = dao.get(orderId);
		verifier.verifyValuesMatch(order, retrieved);
	}

	/**
	 * Verifies removal of an Order entity, ensuring cascade settings are correct.
	 *
	 * The order must be deleted by removing its association with the Customer.
	 *
	 * No associated entities should be removed when an order is removed.
	 */
	@Test
	public void delete_verifyCorrectCascade() {
		Order order = buildOrder();

		dao.create(order);
		session.flush();

		Customer customer = order.getCustomer();
		customer.removeOrder(order);

		session.flush();

		assertEquals(0, countRowsInTable("orders"));
		assertEquals(0, countRowsInTable("order_items"));
		assertEquals(1, countRowsInTable("customer"));
		assertEquals(1, countRowsInTable("address"));
		assertEquals(1, countRowsInTable("credit_card"));
		assertEquals(1, countRowsInTable("item"));
	}

	private Order buildOrder() {
		Item item = new Item("Sony", "SN9090", 99, 20.00);
		session.save(item);

		Address address = new Address("UK", "Bedford", "Luton", "LU4 8AW", "1 Maple Road");
		session.save(address);

		Customer customer = new Customer(Title.DR, "Mantis", "Jones", "07909888888", "mantis.jones@gmail.com");
		customer.addAddress(address);

		CreditCard card = new CreditCard(customer, "Mr M Jones", "1020304050607080", "10-20-30", "09/19", 999);
		customer.addCreditCard(card);

		session.save(customer);

		session.flush();

		Order order = new Order(customer, new Date(), OrderStatus.NEW, address, 200.00, card);
		order.addItem(item);
		order.addItem(item);
		customer.addOrder(order);
		return order;
	}

}
