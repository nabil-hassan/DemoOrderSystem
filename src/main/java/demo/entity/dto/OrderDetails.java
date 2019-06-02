package demo.entity.dto;

import java.util.Date;
import java.util.List;

import demo.entity.persistent.OrderStatus;

public class OrderDetails extends OrderSummary {

    private List<BasketItem> items;

    public OrderDetails(Long orderId, String customerName, String deliveryAddress,
            OrderStatus status, Double totalCost, Date createdDate) {
        super(orderId, customerName, deliveryAddress, status, totalCost, createdDate);
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }
}
