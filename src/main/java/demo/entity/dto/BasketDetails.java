package demo.entity.dto;

import java.util.List;

public class BasketDetails {

    private Long customerid;
    private String customerName;
    private List<BasketItem> items;

    public BasketDetails(Long customerid, String customerName, List<BasketItem> items) {
        this.customerid = customerid;
        this.customerName = customerName;
        this.items = items;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<BasketItem> getItems() {
        return items;
    }
}
