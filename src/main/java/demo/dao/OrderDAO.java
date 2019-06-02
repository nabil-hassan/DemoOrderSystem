package demo.dao;

import org.hibernate.SessionFactory;

import demo.entity.persistent.Order;

public class OrderDAO extends BaseDAO<Order> {

	public OrderDAO(SessionFactory sessionFactory) {
		super(sessionFactory, Order.class);
	}
	
	
	

}
