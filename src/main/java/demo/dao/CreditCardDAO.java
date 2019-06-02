package demo.dao;

import org.hibernate.SessionFactory;

import demo.entity.persistent.CreditCard;

public class CreditCardDAO extends BaseDAO<CreditCard> {

	public CreditCardDAO(SessionFactory sessionFactory) {
		super(sessionFactory, CreditCard.class);
	}
	
}
