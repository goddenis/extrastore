package ru.versilov.extrastore.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Catalyst
 * Date: 20.07.2010
 * Time: 14:35:00
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="BATCHES")
public class Batch {
    long id;
    Date shipmentDate;
    int ops;    // Index of Sender's Postal Division
    List<Order> orders = new ArrayList<Order>();

    public Batch() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DAY_OF_YEAR, 1);
        shipmentDate = c.getTime();
        ops = 443961;
    }

    @Id
    @GeneratedValue
    @Column(name="ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name="SHIPMENT_DATE")
    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    @Column(name="OPS")
    public int getOps() {
        return ops;
    }

    public void setOps(int ops) {
        this.ops = ops;
    }

    @OneToMany(mappedBy="batch", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Transient
    public BigDecimal getTotalAmount() {
        BigDecimal totalAmount = new BigDecimal(0);
        for (Order o: orders) {
            totalAmount = totalAmount.add(o.getTotalAmount());
        }

        return totalAmount;
    }

    @Transient
    public int getTotalGoodsQuantity() {
        int totalQuantity = 0;
        for (Order o: orders) {
            totalQuantity += o.getItemsQuantity();
        }
        return totalQuantity;
    }
    
}
