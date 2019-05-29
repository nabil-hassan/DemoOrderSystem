package demo.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import demo.entity.HibernateEntity;

public abstract class BaseDAO<T extends HibernateEntity> {

	private SessionFactory sessionFactory;
	private Class<T> clazz;

	public BaseDAO(SessionFactory sessionFactory, Class<T> clazz) {
		super();
		this.sessionFactory = sessionFactory;
		this.clazz = clazz;
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public T get(Serializable id) {
		return (T) getCurrentSession().get(clazz, id);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public T load(Serializable id) {
		return (T) getCurrentSession().load(clazz, id);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public List<T> findAll() {
		return getCurrentSession().createQuery("from " + clazz.getName(), clazz).list();
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public Serializable create(T t) {
		return getCurrentSession().save(t);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void update(T t) {
		getCurrentSession().update(t);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void delete(T t) {
		getCurrentSession().delete(t);
	}
	
	public void flush() {
		getCurrentSession().flush();
	}

}
