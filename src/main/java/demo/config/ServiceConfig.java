package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import demo.dao.AddressDAO;
import demo.dao.CreditCardDAO;
import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.dao.OrderDAO;
import demo.service.BasketService;
import demo.service.CustomerService;
import demo.service.ItemService;
import demo.service.OrderService;

@Configuration
@Import(PersistenceConfig.class)
public class ServiceConfig {

    @Bean
    public BasketService basketService(ItemDAO itemDAO, CustomerDAO customerDAO) {
        return new BasketService(itemDAO, customerDAO);
    }

    @Bean
    public CustomerService customerService(CustomerDAO customerDAO) {
        return new CustomerService(customerDAO);
    }

    @Bean
    public OrderService orderService(AddressDAO addressDAO, CreditCardDAO creditCardDAO, CustomerDAO customerDAO,
            OrderDAO orderDAO) {
        return new OrderService(addressDAO, creditCardDAO, customerDAO, orderDAO);
    }

    @Bean
    public ItemService itemService(ItemDAO itemDAO) {
        return new ItemService(itemDAO);
    }

}
