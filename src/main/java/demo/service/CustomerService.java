package demo.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import demo.dao.CustomerDAO;
import demo.entity.Customer;

public class CustomerService {

    private CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Transactional
    public List<Customer> findAll(){
        return customerDAO.findAll();
    }

    @Transactional
    public Customer get(Long id) {
        return customerDAO.get(id);
    }

}
