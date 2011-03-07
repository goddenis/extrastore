package ru.extrastore.model.payment;

import ru.extrastore.model.Store;

import javax.persistence.*;
import java.io.Serializable;

/**
 * http://extrastore.ru
 * Created by Catalyst on 05.03.11 at 23:44
 */
@Entity
public abstract class PaymentType implements Serializable {
    @Id
    @GeneratedValue
    long id;

    @Column(nullable = false, length = 16)
    String alias;

    @ManyToOne
    @JoinColumn(name = "storeId")
    Store store;

    @Column(nullable = false)
    String name;

    @Column(nullable = true, length = 512)
    String description;

    float fee;  // In percent

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }
}
