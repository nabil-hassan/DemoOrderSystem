package demo.converter;

import java.util.stream.Collectors;

import demo.entity.dto.AddressSummary;
import demo.entity.dto.CreditCardSummary;
import demo.entity.dto.CustomerDetails;
import demo.entity.dto.CustomerSummary;
import demo.entity.persistent.Address;
import demo.entity.persistent.CreditCard;
import demo.entity.persistent.Customer;

public class CustomerConverter {

    public CustomerDetails toDetails(Customer customer) {
        CustomerDetails details = new CustomerDetails(customer.getId(), nullSafeTitleString(customer),
                customer.getForename(), customer.getSurname());
        details.setContactNumber(customer.getContactNumber());
        details.setEmailAddress(customer.getEmailAddress());
        details.setCards(customer.getCards().stream().map(this::summariseCard).collect(Collectors.toList()));
        details.setAddresses(customer.getAddresses().stream().map(this::summariseAddress).collect(Collectors.toList()));
        return details;
    }

    public CustomerSummary toSummary(Customer customer) {
        return new CustomerSummary(customer.getId(),
                nullSafeTitleString(customer), customer.getForename(), customer.getSurname());
    }

    private String nullSafeTitleString(Customer customer) {
        return customer.getTitle() != null ? customer.getTitle().getValue() : null;
    }

    private CreditCardSummary summariseCard(CreditCard card) {
        String cardNumber = card.getCardNumber();
        CreditCardSummary summary = new CreditCardSummary(card.getId(),
                "XXXX-" + cardNumber.substring(cardNumber.length() -4, cardNumber.length()));
        return summary;
    }

    private AddressSummary summariseAddress(Address address) {
        return new AddressSummary(address.getId(), address.toString());
    }

}
