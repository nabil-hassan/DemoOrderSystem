package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import demo.dao.CustomerDAO;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		LOG.info("Initialise Application Context");
		ApplicationContext ctx = new AnnotationConfigApplicationContext("demo.config");
		CustomerDAO dao = ctx.getBean("customerDAO", CustomerDAO.class);
		((ConfigurableApplicationContext) ctx).close();
	}
	
	
}
