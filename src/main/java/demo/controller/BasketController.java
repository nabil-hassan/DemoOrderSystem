package demo.controller;

import demo.service.BasketService;

public class BasketController {

    private BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }


    public void addToBasket(Long customerId, Long itemId) {

    }

    public void removeToBasket(Long customerId, Long itemId) {

    }



}
