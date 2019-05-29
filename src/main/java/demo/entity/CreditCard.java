package demo.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "credit_card")
public class CreditCard extends HibernateEntity {

	@ManyToOne
	@PrimaryKeyJoinColumn(name = "customer_id", referencedColumnName="id")
	private Customer customer;

	@Column
	private String cardHolderName;
	
	@Column
	private String cardNumber;
	
	@Column
	private String sortCode;
	
	@Column
	private String issueDate;
	
	@Column
	private String expiryDate;
	
	@Column
	private Integer issueNumber;
	
	@Column
	private Integer securityCode;

	@SuppressWarnings("unused")
	public CreditCard() {
	}

	public CreditCard(Customer customer, String cardHolderName, String cardNumber, String sortCode, String expiryDate,
			Integer securityCode) {
		this.customer = customer;
		this.cardHolderName = cardHolderName;
		this.cardNumber = cardNumber;
		this.sortCode = sortCode;
		this.expiryDate = expiryDate;
		this.securityCode = securityCode;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(Integer issueNumber) {
		this.issueNumber = issueNumber;
	}

	public Integer getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(Integer securityCode) {
		this.securityCode = securityCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cardHolderName, cardNumber, expiryDate, securityCode, sortCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CreditCard))
			return false;
		CreditCard other = (CreditCard) obj;
		return Objects.equals(cardHolderName, other.cardHolderName) && Objects.equals(cardNumber, other.cardNumber)
				&& Objects.equals(expiryDate, other.expiryDate) && Objects.equals(securityCode, other.securityCode)
				&& Objects.equals(sortCode, other.sortCode);
	}

}
	