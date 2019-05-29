package demo.service;

import org.springframework.transaction.annotation.Transactional;

import demo.dao.CustomerDAO;
import demo.dao.ItemDAO;

public class BasketService {

    private ItemDAO itemDAO;

    private CustomerDAO customerDAO;

    public BasketService(ItemDAO itemDAO, CustomerDAO customerDAO) {
        this.itemDAO = itemDAO;
        this.customerDAO = customerDAO;
    }

    @Transactional
    public void addItemToBasket(Long customerId, Long itemId) {

        // verify customer and item exist

        // if basket does not exist, create it


    }

    @Transactional
    public void removeItemFromBasket(Long customerId, Long itemId) {
        // verify customer and basket exist

        // if basket does not exist, create it

        // Update total cost of basket
    }

}
