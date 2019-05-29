package demo.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "basket")
public class Basket extends HibernateEntity {

    @Column
    private Date createdDate;

    @OneToOne
    @JoinColumn(name="customer_id", unique=true, referencedColumnName="id")
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "basket_items",
            joinColumns = @JoinColumn(name = "basket_id", referencedColumnName = "id", updatable = false, nullable = false),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id", updatable = false, nullable = false))
    private Set<Item> items = new HashSet<>();

    public Basket() {
    }

    public Basket(Date createdDate, Customer customer) {
        this.createdDate = createdDate;
        this.customer = customer;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Item> getItems() {
        return items;
    }

    private void setItems(Set<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
    }
}
