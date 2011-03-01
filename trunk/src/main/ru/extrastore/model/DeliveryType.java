package ru.extrastore.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * http://extrastore.ru
 * Created by Catalyst on 01.03.11 at 20:05
 */
@Entity
public abstract class DeliveryType implements Serializable{
    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    @JoinColumn(name = "storeId")
    Store store;

    String name;

    long price;

    long freeAfter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getFreeAfter() {
        return freeAfter;
    }

    public void setFreeAfter(long freeAfter) {
        this.freeAfter = freeAfter;
    }

    public long getCost(Order order) {
        long totalCost = order.getSubTotal();
        return (totalCost > 0 && totalCost < this.getFreeAfter() ? this.getPrice() : 0);
    }
}
