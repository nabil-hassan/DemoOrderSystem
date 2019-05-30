package demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.entity.Order;
import demo.service.OrderService;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }


}
