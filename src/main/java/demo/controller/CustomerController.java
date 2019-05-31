package demo.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.entity.Customer;
import demo.entity.CustomerSummary;
import demo.entity.Order;
import demo.entity.OrderSummary;
import demo.service.CustomerService;
import demo.service.OrderService;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

    private CustomerService customerService;
    private OrderService orderService;

    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.get(id);
    }

    @GetMapping
    public List<CustomerSummary> findAllCustomers() {
        return customerService.findAll().stream().map(CustomerSummary::new).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}/orders")
    @Transactional
    public List<OrderSummary> getAllOrdersForCustomer(@PathVariable Long id) {
        return orderService.getAllOrders(id).stream()
                .sorted(Comparator.comparing(Order::getCreatedDate).reversed())
                .map(OrderSummary::new).collect(Collectors.toList());
    }

}
