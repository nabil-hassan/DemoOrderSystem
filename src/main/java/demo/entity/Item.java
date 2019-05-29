package demo.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item extends HibernateEntity {
	
	public Item() {
	}

	public Item(String manufacturer, String modelNumber, Integer stockQuantity) {
		this.manufacturer = manufacturer;
		this.modelNumber = modelNumber;
		this.stockQuantity = stockQuantity;
	}

	@Column
	private String manufacturer;

	@Column
	private String modelNumber;
	
	@Column
	private String colour;
	
	@Column
	private Integer weightKg;
	
	@Column
	private String dimensions;
	
	@Column
	private String shortDescription;
	
	@Column
	private String fullDescription;
	
	@Column
	private Integer stockQuantity;

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public Integer getWeightKg() {
		return weightKg;
	}

	public void setWeightKg(Integer weightKg) {
		this.weightKg = weightKg;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(manufacturer, modelNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Item))
			return false;
		Item other = (Item) obj;
		return Objects.equals(manufacturer, other.manufacturer) && Objects.equals(modelNumber, other.modelNumber);
	}
	
}
