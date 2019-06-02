package demo.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.entity.dto.CustomerDetails;
import demo.entity.dto.CustomerSummary;
import demo.entity.persistent.Order;
import demo.entity.dto.OrderSummary;
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

    @GetMapping
    public List<CustomerSummary> findAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public CustomerDetails getCustomer(@PathVariable Long id) {
        return customerService.get(id);
    }

    @GetMapping(value = "/{id}/orders")
    public List<OrderSummary> getAllOrdersForCustomer(@PathVariable Long id) {
        return orderService.findAllForCustomer(id);
    }

}
