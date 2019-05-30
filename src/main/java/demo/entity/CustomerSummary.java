package demo.entity;

/**
 * Non-persistent which summarises key customer details.
 */
public class CustomerSummary {

    private Long id;
    private Title title;
    private String forename;
    private String surname;

    public CustomerSummary(Customer customer) {
        this.id = customer.getId();
        this.title = customer.getTitle();
        this.forename = customer.getForename();
        this.surname = customer.getSurname();
    }

    public Long getId() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }
}
