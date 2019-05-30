package demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "orders")
public class Order extends HibernateEntity {

	@ManyToOne
	@PrimaryKeyJoinColumn(name = "customer_id", referencedColumnName="id")
	private Customer customer;

	@ManyToMany
	@JoinTable(name = "order_items",
			joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id", updatable = false, nullable = false),
			inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id", updatable = false, nullable = false))
	private List<Item> items = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "delivery_address_id")
	private Address deliveryAddress;

	@ManyToOne
	@JoinColumn(name = "credit_card_id")
	private CreditCard creditCard;

	@Column
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column
	private Double totalCost;

	@Column
	private Date createdDate;

	@Column
	private Date deliveryEstimateDate;

	@Column
	private Date deliveredDate;

	@Column
	private Date cancelledDate;

	public Order(Customer customer, Date createdDate, OrderStatus status, Address deliveryAddress, Double totalCost,
			CreditCard creditCard) {
		this.customer = customer;
		this.deliveryAddress = deliveryAddress;
		this.createdDate = createdDate;
		this.status = status;
		this.totalCost = totalCost;
		this.creditCard = creditCard;
	}

	public Order() {
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Date getDeliveryEstimateDate() {
		return deliveryEstimateDate;
	}

	public void setDeliveryEstimateDate(Date deliveryEstimateDate) {
		this.deliveryEstimateDate = deliveryEstimateDate;
	}

	public Date getDeliveredDate() {
		return deliveredDate;
	}

	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	public Date getCancelledDate() {
		return cancelledDate;
	}

	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}

	public List<Item> getItems() {
		return items;
	}

	private void setItems(List<Item> items) {
		this.items = items;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order) o;
		return Objects.equals(customer, order.customer) &&
				Objects.equals(totalCost, order.totalCost) &&
				Objects.equals(createdDate, order.createdDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customer, totalCost, createdDate);
	}
}
