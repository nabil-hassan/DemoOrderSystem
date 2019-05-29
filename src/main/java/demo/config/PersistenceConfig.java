package demo.config;

import java.util.Properties;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import demo.dao.CustomerDAO;
import demo.dao.OrderDAO;

@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

	@Value("${database.driver}")
	private String databaseDriver;

	@Value("${database.url}")
	private String databaseUrl;

	@Value("${database.user}")
	private String databaseUser;

	@Value("${database.password}")
	private String databasePassword;

	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateAutoDDL;

	@Value("${hibernate.dialect}")
	private String hibernateDialect;

	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;

	@Bean
	public Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.hbm2ddl.auto", hibernateAutoDDL);
		properties.put("hibernate.dialect", hibernateDialect);
		properties.put("hibernate.connection.driver_class", databaseDriver);
		properties.put("hibernate.connection.url", databaseUrl);
		properties.put("hibernate.connection.username", databaseUser);
		properties.put("hibernate.connection.password", databasePassword);
		properties.put("hibernate.show_sql", hibernateShowSql);
		properties.put("hibernate.connection.provider_class", C3P0ConnectionProvider.class.getName());
		properties.put("hibernate.c3p0.min_size", 5);
		properties.put("hibernate.c3p0.max_size", 20);
		properties.put("hibernate.c3p0.timeout", 300);
		return properties;
	}

	@Bean
	public DataSource dataSource() {
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL(databaseUrl);
		ds.setUser(databaseUser);
		ds.setPassword(databasePassword);
		return ds;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan("demo.entity");
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}

	@Bean
	public CustomerDAO customerDAO(SessionFactory sessionFactory) {
		return new CustomerDAO(sessionFactory);
	}

	@Bean
	public OrderDAO OrderDAO(SessionFactory sessionFactory) {
		return new OrderDAO(sessionFactory);
	}

}
