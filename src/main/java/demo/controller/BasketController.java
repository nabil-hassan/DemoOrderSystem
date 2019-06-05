package demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import demo.entity.dto.BasketDetails;
import demo.entity.dto.OrderDetails;
import demo.entity.request.CheckoutRequest;
import demo.service.BasketService;

@RestController
@RequestMapping(path = "/basket")
public class BasketController {

    private BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/{customerId}")
    public BasketDetails getBasket(@PathVariable Long customerId) {
        return basketService.getBasketDetails(customerId);
    }

    @PostMapping("/{customerId}/addItem/{itemId}")
    public BasketDetails addToBasket(@PathVariable Long customerId, @PathVariable Long itemId) {
        return basketService.addItemToBasket(customerId, itemId);
    }

    @PostMapping("/{customerId}/removeItem/{itemId}")
    public BasketDetails removeFromBasket(@PathVariable Long customerId, @PathVariable Long itemId) {
        return basketService.removeItemFromBasket(customerId, itemId);
    }

    @PostMapping("/{customerId}/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetails checkout(@PathVariable Long customerId, @RequestBody CheckoutRequest checkoutRequest) {
        return basketService.checkout(customerId, checkoutRequest.getAddressId(), checkoutRequest.getCardId());
    }

}
