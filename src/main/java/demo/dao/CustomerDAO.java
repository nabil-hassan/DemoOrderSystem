package demo.dao;

import org.hibernate.SessionFactory;

import demo.entity.Customer;

public class CustomerDAO extends BaseDAO<Customer>{

	public CustomerDAO(SessionFactory sessionFactory) {
		super(sessionFactory, Customer.class);
	}
	
}
