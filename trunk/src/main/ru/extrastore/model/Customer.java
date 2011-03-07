package ru.extrastore.model;

import javax.persistence.*;
import java.util.Set;

/**
 * http://extrastore.ru
 * Created by Catalyst on 22.02.11 at 16:12
 */
@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User{
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    Address address;

    String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    Set<Order> orders;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
