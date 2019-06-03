package demo.service;

import org.hibernate.SessionFactory;
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
import demo.categories.ServiceTest;
import demo.config.ConverterConfig;
import demo.config.PersistenceConfig;
import demo.config.PropertyConfig;
import demo.config.ServiceConfig;
import demo.config.TestConfig;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/demo/test.properties")
@ContextConfiguration(classes = { ConverterConfig.class, PersistenceConfig.class, PropertyConfig.class,
        ServiceConfig.class, TestConfig.class})
@Category(ServiceTest.class)
@Ignore
public abstract class ServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected SessionFactory sessionFactory;

}
