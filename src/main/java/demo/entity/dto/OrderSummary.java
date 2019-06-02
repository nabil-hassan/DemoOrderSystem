package demo.entity.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import demo.entity.persistent.OrderStatus;

/**
 * Non-persistent entity - summarises key order details.
 */
public class OrderSummary {

    private Long id;

    private String customerName;

    private String deliveryAddress;

    private OrderStatus status;

    private Double totalCost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdDate;

    @JsonInclude(Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date deliveryEstimateDate;

    @JsonInclude(Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date deliveredDate;

    @JsonInclude(Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date cancelledDate;

    public OrderSummary(Long orderId, String customerName, String deliveryAddress, OrderStatus status,
            Double totalCost, Date createdDate) {
        this.id = orderId;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.totalCost = totalCost;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDeliveryAddress() {
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

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setDeliveryEstimateDate(Date deliveryEstimateDate) {
        this.deliveryEstimateDate = deliveryEstimateDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }
}
