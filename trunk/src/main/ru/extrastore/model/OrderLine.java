package ru.extrastore.model;

import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;

/**
 * File profile
 * Creator: Catalyst
 * Date: 18.02.11
 * Time: 16:41
 */
@Entity
public class OrderLine implements Serializable {
    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "orderId")
    Order order;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "productId")
    Product product;

    long quantity;

    public OrderLine(Order order, Product p, long quantity) {
        this.product = p;
        this.quantity = quantity;
        this.order = order;
    }

    public OrderLine() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
