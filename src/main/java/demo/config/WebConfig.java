package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import demo.controller.CustomerController;
import demo.controller.OrderController;
import demo.service.CustomerService;
import demo.service.OrderService;

@EnableWebMvc
@Configuration
@Import({PersistenceConfig.class, ServiceConfig.class})
public class WebConfig {

    @Bean
    public CustomerController customerController(CustomerService customerService, OrderService orderService) {
        return new CustomerController(customerService, orderService);
    }

//    @Bean
//    public ItemController itemController(ItemDAO itemDAO) {
//        return new ItemController(itemDAO);
//    }

    @Bean
    public OrderController orderController(OrderService orderService) {
        return new OrderController(orderService);
    }

}

