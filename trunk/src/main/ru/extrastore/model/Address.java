package ru.extrastore.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * File profile
 * Creator: Catalyst
 * Date: 22.02.11
 * Time: 15:48
 */
@Entity
public class Address implements Serializable {
    @Id
    @GeneratedValue
    long id;

    @OneToOne
    @JoinColumn(name = "customerId")
    User customer;

    long zip;

    @Column(nullable = false)
    String region;
    String area;
    String town;

    @Column(nullable = false)
    String address; // Street, house, apartment/office

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public long getZip() {
        return zip;
    }

    public void setZip(long zip) {
        this.zip = zip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
