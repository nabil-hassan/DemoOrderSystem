package demo.dao;

import org.hibernate.SessionFactory;

import demo.entity.persistent.Item;

public class ItemDAO extends BaseDAO<Item> {

	public ItemDAO(SessionFactory sessionFactory) {
		super(sessionFactory, Item.class);
	}
	
}
