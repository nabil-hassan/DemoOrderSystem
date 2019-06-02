package demo.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import demo.entity.persistent.Item;

/**
 * A view of an item for a basket or order. Includes the item details, as well as the item quantity and total cost.
 */
public class BasketItem {

    private Long itemId;

    private String manufacturer;

    private String modelNumber;

    private int quantity;

    private Double totalcost;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer weightKg;

    private Double unitCost;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dimensions;

    public BasketItem(Item item, int quantity, Double totalCost) {
        this.quantity = quantity;
        this.itemId = item.getId();
        this.manufacturer = item.getManufacturer();
        this.totalcost = totalCost;
        this.modelNumber = item.getModelNumber();
        this.weightKg = item.getWeightKg();
        this.unitCost = item.getCost();
        this.dimensions = item.getDimensions();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public Double getTotalcost() {
        return totalcost;
    }

    public Integer getWeightKg() {
        return weightKg;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public String getDimensions() {
        return dimensions;
    }
}
