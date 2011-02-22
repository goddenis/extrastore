package ru.extrastore.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * File profile
 * Creator: Catalyst
 * Date: 18.02.11
 * Time: 16:39
 */
@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {
    @Id
    @GeneratedValue
    long id;

    @ManyToOne()
    @JoinColumn(name = "customerId")
    Customer customer;

    int deliveryType = 1;

    @OneToMany(mappedBy = "order")
    List<OrderLine> lines = new ArrayList<OrderLine>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public void setLines(List<OrderLine> lines) {
        this.lines = lines;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(int deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void addProduct(Product p, long quantity) {
        for (OrderLine l: lines) {
            if (l.getProduct().equals(p)) {
                l.setQuantity(l.getQuantity()+quantity);
                return;
            }
        }
        lines.add(new OrderLine(this, p, quantity));
    }

    public long getTotalCost() {
        long totalCost = 0;
        for (OrderLine l: lines) {
           totalCost += l.getProduct().getPrice()*l.getQuantity();
        }
        return totalCost;
    }


}
