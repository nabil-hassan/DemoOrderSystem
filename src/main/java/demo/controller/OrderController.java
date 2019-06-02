package demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.entity.dto.OrderDetails;
import demo.entity.persistent.Order;
import demo.entity.dto.OrderSummary;
import demo.service.OrderService;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{id}")
    public OrderDetails getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<OrderSummary> findAllOrders() {
        return orderService.findAll();
    }

}
