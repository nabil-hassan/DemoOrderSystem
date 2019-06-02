package demo.entity.dto;

import java.util.List;

import demo.entity.persistent.Title;

public class CustomerDetails {

    private Long id;

    private String title;

    private String forename;

    private String surname;

    private String contactNumber;

    private String emailAddress;

    private List<CreditCardSummary> cards;

    private List<AddressSummary> addresses;

    public CustomerDetails(Long id, String title, String forename, String surname) {
        this.id = id;
        this.title = title;
        this.forename = forename;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
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

    public List<CreditCardSummary> getCards() {
        return cards;
    }

    public void setCards(List<CreditCardSummary> cards) {
        this.cards = cards;
    }

    public List<AddressSummary> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressSummary> addresses) {
        this.addresses = addresses;
    }
}
