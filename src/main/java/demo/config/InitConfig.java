package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;
import demo.dao.OrderDAO;

@Configuration
@Import(PersistenceConfig.class)
public class InitConfig {

    @Bean
    public BaseDataInitialiser dbSetupPostProcessor(CustomerDAO customerDAO,
            ItemDAO itemDAO, OrderDAO orderDAO) {
        return new BaseDataInitialiser(customerDAO, itemDAO, orderDAO);
    }
}
