package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import demo.controller.BasketController;
import demo.controller.CustomerController;
import demo.controller.ItemController;
import demo.controller.OrderController;
import demo.service.BasketService;
import demo.service.CustomerService;
import demo.service.ItemService;
import demo.service.OrderService;

@EnableWebMvc
@Configuration
@Import({PersistenceConfig.class, ServiceConfig.class})
public class WebConfig {

    @Bean
    public BasketController basketController(BasketService basketService) {
        return new BasketController(basketService);
    }

    @Bean
    public CustomerController customerController(CustomerService customerService, OrderService orderService) {
        return new CustomerController(customerService, orderService);
    }

    @Bean
    public ItemController itemController(ItemService itemService) {
        return new ItemController(itemService);
    }

    @Bean
    public OrderController orderController(OrderService orderService) {
        return new OrderController(orderService);
    }

}

