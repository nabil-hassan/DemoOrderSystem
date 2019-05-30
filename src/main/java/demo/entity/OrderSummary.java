package demo.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Non-persistent entity - summarises key order details.
 */
public class OrderSummary {

    private Long id;
    private String customer;
    private Address deliveryAddress;
    private OrderStatus status;
    private Double totalCost;
    private Date createdDate;
    @JsonInclude(Include.NON_NULL)
    private Date deliveryEstimateDate;
    @JsonInclude(Include.NON_NULL)
    private Date deliveredDate;

    public OrderSummary(Order order) {
        id = order.getId();
        customer = order.getCustomer() != null ? order.getCustomer().getFullName() : null;
        deliveryAddress = order.getDeliveryAddress();
        status = order.getStatus();
        totalCost = order.getTotalCost();
        createdDate = order.getCreatedDate();
        deliveryEstimateDate = order.getDeliveryEstimateDate();
        deliveredDate = order.getDeliveredDate();
    }

    public Long getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getDeliveryEstimateDate() {
        return deliveryEstimateDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }
}
