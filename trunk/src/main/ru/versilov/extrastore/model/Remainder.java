package ru.versilov.extrastore.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 27.10.2010
 * Time: 0:21:03
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="REMAINS")
public class Remainder {
    private long id;

    private Resource resource;
    private Product product;
    private long quantity;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="RESOURCE_ID", nullable = false)
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @ManyToOne
    @JoinColumn(name="PRODUCT_ID", nullable = false)
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
