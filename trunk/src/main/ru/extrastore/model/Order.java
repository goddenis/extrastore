package ru.extrastore.model;

import ru.extrastore.model.delivery.DeliveryType;
import ru.extrastore.model.payment.PaymentType;

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
@Table(name = "Orders") // Explicitly set, because Order is a keyword in ejb-ql and sql.
public class Order implements Serializable {
    @Id
    @GeneratedValue
    long id;

    @ManyToOne()
    @JoinColumn(name = "customerId")
    User customer;

    @OneToOne
    @JoinColumn(name = "deliveryTypeId")
    DeliveryType deliveryType;

    @OneToOne
    @JoinColumn(name = "paymentTypeId")
    PaymentType paymentType;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderLine> lines = new ArrayList<OrderLine>();

    long totalCost;

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

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
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

    public long getSubTotal() {
        long subTotal = 0;
        for (OrderLine l: lines) {
           subTotal += l.getProduct().getPrice()*l.getQuantity();
        }
        return subTotal;
    }

    public long getDeliveryCost() {
        if (this.getDeliveryType() != null) {
            return this.getDeliveryType().getCost(this);
        } else {
            return -1;
        }
    }


}
