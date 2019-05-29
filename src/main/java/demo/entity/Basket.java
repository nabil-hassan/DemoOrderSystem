package demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "basket")
public class Basket extends HibernateEntity {

    @OneToOne
    @JoinColumn(name="customer_id", referencedColumnName="id")
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "basket_items",
            joinColumns = @JoinColumn(name = "basket_id", referencedColumnName = "id", updatable = false, nullable = false),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id", updatable = false, nullable = false))
    private List<Item> items = new ArrayList<>();

    @SuppressWarnings("unused")
    public Basket() {
    }

    public Basket(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Item> getItems() {
        return items;
    }

    @Transient
    public boolean isEmpty() {
        return items.isEmpty();
    }

    private void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void clearItems() {
        items.clear();
    }

    public Map<Item, Integer> itemCountMap() {
        return items.stream().collect(Collectors.groupingBy(i -> i, Collectors.summingInt(x -> 1)));
    }
}
