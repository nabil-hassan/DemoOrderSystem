package demo.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.categories.PersistenceTest;
import demo.config.PersistenceConfig;
import demo.config.PropertyConfig;
import demo.config.TestConfig;

/**
 * Base class for all persistence layer tests.
 * 
 *  Tests of this type should verify Hibernate mappings, cascade settings etc for a specific entity type, as well as
 *  any entity-specific queries.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/demo/test.properties")
@ContextConfiguration(classes = {PersistenceConfig.class, PropertyConfig.class, TestConfig.class})
@Category(PersistenceTest.class)
@Ignore
public abstract class DAOTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	protected SessionFactory sessionFactory;
	
}
