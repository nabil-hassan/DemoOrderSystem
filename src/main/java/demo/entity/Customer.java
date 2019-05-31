package demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="customer")
public class Customer extends HibernateEntity {

	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private Basket basket;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Order> orders = new HashSet<>();

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<CreditCard> cards = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "customer_addresses",
			joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id", updatable = false, nullable = false),
			inverseJoinColumns = @JoinColumn(name = "address_id", referencedColumnName = "id", updatable = false, nullable = false))
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private Set<Address> addresses = new HashSet<>();

	@Enumerated(EnumType.STRING)
	@Column
	private Title title;
	
	@Column
	private String forename;
	
	@Column
	private String surname;
	
	@Column
	private String contactNumber;
	
	@Column
	private String emailAddress;
	
	public Customer(Title title, String forename, String surname, String contactNumber, String emailAddress) {
		this.title = title;
		this.forename = forename;
		this.surname = surname;
		this.contactNumber = contactNumber;
		this.emailAddress = emailAddress;
	}

	@SuppressWarnings("unused")
	public Customer() {
	}

	public Basket getBasket() {
		return basket;
	}

	public void setBasket(Basket basket) {
		this.basket = basket;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	private void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Set<CreditCard> getCards() {
		return cards;
	}

	private void setCards(Set<CreditCard> cards) {
		this.cards = cards;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	private void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void addAddress(Address address) {
		addresses.add(address);
	}

	public void removeAddress(Address address) {
		addresses.remove(address);
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	public void removeOrder(Order order) {
		orders.remove(order);
	}
	
	public void addCreditCard(CreditCard card) {
		cards.add(card);
	}

	public void removeCreditCard(CreditCard card) {
		cards.remove(card);
	}

	@Transient
	public String getFullName() {
		return (title != null ? title.name() + " " : "")
				+ (forename != null ? forename + " " : "")
				+ (surname != null ? surname : "").trim();
	}
}
