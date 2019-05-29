package demo.verifier;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import demo.entity.HibernateEntity;

/**
 * Verifies a persisted Hibernate object matches expected values.
 * 
 * Uses both the notion of business equality and persistent identity.
 */
public abstract class Verifier<T extends HibernateEntity> {

	abstract void verifyValuesMatch(T expected, T actual);

	public <V extends HibernateEntity> Set<V> idSortedSet(Set<V> elements) {
		return new TreeSet<>(Comparator.comparing(HibernateEntity::getId));
	}
	
}
