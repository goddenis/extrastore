package ru.extrastore.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * http://extrastore.ru
 * Created by Catalyst on 23.02.11 at 19:49
 */
@Entity
public class Price implements Serializable, StoreConstraint {
    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    Product product;

    @ManyToOne
    @JoinColumn(name = "storeId", nullable = true)
    Store store;

    long value;

    String description;

    boolean old;

    boolean main;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOld() {
        return old;
    }

    public void setOld(boolean old) {
        this.old = old;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}
