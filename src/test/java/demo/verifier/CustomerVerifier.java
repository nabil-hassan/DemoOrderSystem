package demo.verifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import demo.entity.persistent.Customer;

public class CustomerVerifier extends Verifier<Customer> {

	@Override
	public void verifyValuesMatch(Customer expected, Customer actual) {
		assertEquals(expected.getTitle(), actual.getTitle());
		assertEquals(expected.getForename(), actual.getForename());
		assertEquals(expected.getSurname(), actual.getSurname());
		assertEquals(expected.getContactNumber(), actual.getContactNumber());
		assertEquals(expected.getEmailAddress(), actual.getEmailAddress());
		
		if (expected.getBasket() != null) {
			assertNotNull(actual.getBasket());
			assertEquals(expected.getBasket().getId(), actual.getBasket().getId());
		} else {
			assertNull(expected.getBasket());
		}

		if (!expected.getAddresses().isEmpty()) {
			assertEquals(idSortedSet(expected.getAddresses()), idSortedSet(actual.getAddresses()));
		} else {
			assertTrue(actual.getAddresses().isEmpty());
		}

		if (!expected.getCards().isEmpty()) {
			assertEquals(idSortedSet(expected.getCards()), idSortedSet(actual.getCards()));
		} else {
			assertTrue(actual.getCards().isEmpty());
		}

		if (!expected.getOrders().isEmpty()) {
			assertEquals(idSortedSet(expected.getOrders()), idSortedSet(actual.getOrders()));
		} else {
			assertTrue(actual.getOrders().isEmpty());
		}
	}

}
