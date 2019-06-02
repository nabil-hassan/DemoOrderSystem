package demo.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import demo.entity.dto.BasketItem;
import demo.entity.dto.OrderDetails;
import demo.entity.dto.OrderSummary;
import demo.entity.persistent.Item;
import demo.entity.persistent.Order;

public class OrderConverter {

    private ItemConverter itemConverter;

    public OrderConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
    }

    public OrderSummary toOrderSummary(Order o) {
        String customerName = o.getCustomer() != null ? o.getCustomer().getFullName() : null;
        String deliveryAddress = o.getDeliveryAddress() != null ? o.getDeliveryAddress().toString() : null;
        OrderSummary result = new OrderSummary(o.getId(), customerName, deliveryAddress, o.getStatus(),
                o.getTotalCost(), o.getCreatedDate());
        result.setDeliveredDate(o.getDeliveredDate());
        result.setDeliveryEstimateDate(o.getDeliveryEstimateDate());
        result.setCancelledDate(o.getCancelledDate());
        return result;
    }

    public OrderDetails toOrderDetails(Order order) {
        OrderSummary o = toOrderSummary(order);
        OrderDetails result = new OrderDetails(o.getId(), o.getCustomerName(), o.getDeliveryAddress(), o.getStatus(),
                o.getTotalCost(), o.getCreatedDate());
        List<BasketItem> items = itemConverter.toBasketItems(order.getItems());
        result.setItems(items);
        return result;
    }
}
