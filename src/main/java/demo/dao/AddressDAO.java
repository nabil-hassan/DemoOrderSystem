package demo.dao;

import org.hibernate.SessionFactory;

import demo.entity.Address;

public class AddressDAO extends BaseDAO<Address> {

	public AddressDAO(SessionFactory sessionFactory) {
		super(sessionFactory, Address.class);
	}
	
}