package demo.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import demo.dao.ItemDAO;
import demo.entity.Item;

public class ItemService {

    private ItemDAO itemDAO;

    public ItemService(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Transactional
    public List<Item> findAll() {
        return itemDAO.findAll();
    }


}
