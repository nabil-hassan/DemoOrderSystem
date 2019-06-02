package demo.entity.dto;

import demo.entity.persistent.Title;
import demo.entity.persistent.Customer;

/**
 * Non-persistent which summarises key customer details.
 */
public class CustomerSummary {

    private Long id;
    private String title;
    private String forename;
    private String surname;

    public CustomerSummary(Long id, String title, String forename, String surname) {
        this.id = id;
        this.title = title;
        this.forename = forename;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }
}
