package demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import demo.dao.ItemDAO;
import demo.entity.Item;

@RestController
@RequestMapping("/items")
public class ItemController {

    //TODO: implement

//    private ItemService itemService;

//    public ItemController(ItemDAO itemDAO) {
//        this.itemDAO = itemDAO;
//    }

//    @GetMapping
//    public List<Item> getAll() {
//        return itemDAO.findAll();
//    }
}
