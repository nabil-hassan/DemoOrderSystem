package demo.dao;

import org.hibernate.SessionFactory;

import demo.entity.persistent.Customer;

public class CustomerDAO extends BaseDAO<Customer>{

	public CustomerDAO(SessionFactory sessionFactory) {
		super(sessionFactory, Customer.class);
	}
	
}
