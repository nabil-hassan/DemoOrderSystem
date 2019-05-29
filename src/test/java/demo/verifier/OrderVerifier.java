package demo.verifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import demo.entity.Address;
import demo.entity.CreditCard;
import demo.entity.Customer;
import demo.entity.Item;
import demo.entity.Order;

public class OrderVerifier extends Verifier<Order> {

	@Override
	public void verifyValuesMatch(Order expected, Order actual) {
		assertEquals(expected.getStatus(), actual.getStatus());
		assertEquals(expected.getCreatedDate(), actual.getCreatedDate());
		assertEquals(expected.getDeliveryEstimateDate(), actual.getDeliveryEstimateDate());
		assertEquals(expected.getDeliveredDate(), actual.getDeliveredDate());
		assertEquals(expected.getCancelledDate(), actual.getCancelledDate());
		assertEquals(expected.getTotalCost(), actual.getTotalCost());

		if (expected.getCustomer() != null) {
			assertNotNull(actual.getCustomer());
			assertEquals(expected.getCustomer().getId(), actual.getCustomer().getId());
		} else {
			assertNull(actual.getCustomer());
		}

		if (expected.getDeliveryAddress() != null) {
			assertNotNull(actual.getDeliveryAddress());
			assertEquals(expected.getDeliveryAddress().getId(), actual.getDeliveryAddress().getId());
		} else {
			assertNull(actual.getDeliveryAddress());
		}

		if (expected.getCreditCard() != null) {
			assertEquals(expected.getCreditCard().getId(), actual.getCreditCard().getId());
		} else {
			assertNull(actual.getCreditCard());
		}

		if (!expected.getItems().isEmpty()) {
			assertEquals(idSortList(expected.getItems()), idSortList(actual.getItems()));
		} else {
			assertTrue(actual.getItems().isEmpty());
		}
	}

}
