package ru.extrastore.model;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.NotNull;

import javax.persistence.*;
import java.io.Serializable;

/**
 * File profile
 * Creator: Catalyst
 * Date: 18.02.11
 * Time: 9:25
 */
@Entity
@Indexed
public class ProductProperty implements Serializable {
    @Id
    @GeneratedValue
    long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "productId")
    Product product;

    String name;

    String value;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
