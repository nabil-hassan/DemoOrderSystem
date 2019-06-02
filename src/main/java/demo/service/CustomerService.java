package demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import demo.converter.CustomerConverter;
import demo.dao.CustomerDAO;
import demo.entity.dto.CustomerDetails;
import demo.entity.dto.CustomerSummary;
import demo.entity.persistent.Customer;
import demo.exception.EntityNotFoundException;

public class CustomerService {

    private CustomerConverter customerConverter;
    private CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO, CustomerConverter customerConverter) {
        this.customerDAO = customerDAO;
        this.customerConverter = customerConverter;
    }

    @Transactional
    public List<CustomerSummary> findAll(){
        return customerDAO.findAll().stream().map(customerConverter::toSummary).collect(Collectors.toList());
    }

    @Transactional
    public CustomerDetails get(Long id) {
        Customer customer = customerDAO.get(id);
        if (customer == null) {
            throw new EntityNotFoundException(Customer.class, id);
        }
        return customerConverter.toDetails(customer);
    }

}
