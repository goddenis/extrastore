package ru.versilov.extrastore.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 29.10.2010
 * Time: 1:43:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="SHIPMENTLINES")
public class ShipmentLine {
    long id;
    Shipment shipment;
    Product product;
    int quantity;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="shipment_id")
    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    @ManyToOne
    @JoinColumn(name="product_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
