package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.converter.BasketConverter;
import demo.converter.CustomerConverter;
import demo.converter.ItemConverter;
import demo.converter.OrderConverter;

@Configuration
public class ConverterConfig {

    @Bean
    public BasketConverter basketConverter(ItemConverter itemConverter) {
        return new BasketConverter(itemConverter);
    }

    @Bean
    public CustomerConverter customerConverter() {
        return new CustomerConverter();
    }

    @Bean
    public ItemConverter itemConverter() {
        return new ItemConverter();
    }

    @Bean
    public OrderConverter orderConverter(ItemConverter itemConverter) {
        return new OrderConverter(itemConverter);
    }


}
