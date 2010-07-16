/*
 * JBoss, Home of Professional Open Source
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package ru.versilov.extrastore;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="ORDERLINES")
public class OrderLine
    implements Serializable
{
    private static final long serialVersionUID = 207236100660985541L;

    long    lineId;
    int     position;
    Product product;
    int     quantity;
    Date    orderDate;
    Order   order;

    public OrderLine() {
        
    }

    public OrderLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    @Id @GeneratedValue
    @Column(name="ORDERLINEID")
    public long getLineId() {
        return lineId;
    }
    public void setLineId(long id) {
        this.lineId = id;
    }

    @Column(name="POS")
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    
    @ManyToOne
    @JoinColumn(name="ORDERID", nullable=false)
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    @ManyToOne
    @JoinColumn(name="PROD_ID",unique=false,nullable=false)
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product=product;
    }

    @Column(name="QUANTITY",nullable=false)
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void addQuantity(int howmany) {
        quantity += howmany;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderLine orderLine = (OrderLine) o;

        if (quantity != orderLine.quantity) return false;
        if (product != null ? !product.equals(orderLine.product) : orderLine.product != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;
        result = 31 * result + quantity;
        return result;
    }
}
