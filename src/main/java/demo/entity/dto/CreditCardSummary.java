package demo.entity.dto;

public class CreditCardSummary {

    private Long id;
    private String details;

    public CreditCardSummary(Long id, String details) {
        this.id = id;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
