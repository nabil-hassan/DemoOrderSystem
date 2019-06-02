package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import demo.converter.BasketConverter;
import demo.converter.CustomerConverter;
import demo.converter.ItemConverter;
import demo.converter.OrderConverter;
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
@Import({ConverterConfig.class, PersistenceConfig.class})
public class ServiceConfig {

    @Bean
    public BasketService basketService(AddressDAO addressDAO, BasketConverter basketConverter,
            CreditCardDAO creditCardDAO, CustomerDAO customerDAO, ItemConverter itemConverter, ItemDAO itemDAO,
            OrderConverter orderConverter, OrderDAO orderDAO) {
        return new BasketService(addressDAO, basketConverter, creditCardDAO, customerDAO, itemDAO, itemConverter,
                orderConverter, orderDAO);
    }

    @Bean
    public CustomerService customerService(CustomerDAO customerDAO, CustomerConverter customerConverter) {
        return new CustomerService(customerDAO, customerConverter);
    }

    @Bean
    public OrderService orderService(CustomerDAO customerDAO, OrderDAO orderDAO, OrderConverter orderConverter) {
        return new OrderService(customerDAO, orderDAO, orderConverter);
    }

    @Bean
    public ItemService itemService(ItemDAO itemDAO) {
        return new ItemService(itemDAO);
    }

}
